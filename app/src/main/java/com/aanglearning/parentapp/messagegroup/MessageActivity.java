package com.aanglearning.parentapp.messagegroup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.DeletedMessageDao;
import com.aanglearning.parentapp.dao.MessageDao;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.DeletedMessage;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.EndlessRecyclerViewScrollListener;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PermissionUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity implements MessageView,
        ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.noMessage) LinearLayout noMessage;

    private MessagePresenter presenter;
    private Groups group;
    private MessageAdapter adapter;
    private ChildInfo childInfo;

    private static final int WRITE_STORAGE_PERMISSION = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group = (Groups) extras.getSerializable("group");
        }
        getSupportActionBar().setTitle(group.getName());

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new MessagePresenterImpl(this, new MessageInteractorImpl());

        setupRecyclerView();

        if (PermissionUtil.isStoragePermissionGranted(this, WRITE_STORAGE_PERMISSION)) {
            getBackupMessages();
        }
    }

    private void getBackupMessages() {
        List<Message> messages = MessageDao.getGroupMessages(group.getId());
        adapter.setDataSet(messages);
        if (NetworkUtil.isNetworkAvailable(this)) {
            if (messages.size() == 0) {
                presenter.getMessages(group.getId());
            } else {
                presenter.getRecentMessages(group.getId(), adapter.getDataSet().get(0).getId());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getBackupMessages();
        } else {
            showSnackbar("Permission has been denied");
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setItemViewCacheSize(10);
        /*recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);*/

        adapter = new MessageAdapter(getApplicationContext(), new ArrayList<Message>(0), SharedPreferenceUtil.getProfile(this).getSchoolId(),
                onItemClickListener);
        recyclerView.setAdapter(adapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                /*if (NetworkUtil.isNetworkAvailable(MessageActivity.this)) {
                    presenter.getFollowupMessages(group.getId(), adapter.getDataSet().get(adapter.getDataSet().size() - 1).getId());
                }*/
                List<Message> messages = MessageDao.getGroupMessagesFromId(group.getId(),
                        adapter.getDataSet().get(adapter.getDataSet().size() - 1).getId());
                if(messages.size() > 0) {
                    adapter.updateDataSet(messages);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBackupMessages();
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        refreshLayout.setRefreshing(false);
        showSnackbar(message);
    }

    @Override
    public void showRecentMessages(List<Message> messages) {
        adapter.insertDataSet(messages);
        recyclerView.smoothScrollToPosition(0);
        syncDeletedMessages();
        backupMessages(messages);
        saveMessageRecipient();
    }

    @Override
    public void showMessages(List<Message> messages) {
        if(messages.size() == 0) {
            noMessage.setVisibility(View.VISIBLE);
        } else {
            noMessage.setVisibility(View.GONE);
            adapter.setDataSet(messages);
            recyclerView.smoothScrollToPosition(0);
            backupMessages(messages);
            saveMessageRecipient();
        }
        syncDeletedMessages();
    }

    private void saveMessageRecipient() {
        Intent i = new Intent(this, MessageRecipientIntentService.class);
        i.putExtra("recipient_id", childInfo.getStudentId());
        i.putExtra("recipient_name", childInfo.getName());
        i.putExtra("group_id", group.getId());
        startService(i);
    }

    private void backupMessages(final List<Message> messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageDao.insertGroupMessages(messages);
            }
        }).start();
    }

    private void syncDeletedMessages() {
        DeletedMessage deletedMessage = DeletedMessageDao.getNewestDeletedMessage(group.getId());
        if(deletedMessage.getGroupId() != 0) {
            presenter.getRecentDeletedMessages(group.getId(), deletedMessage.getId());
        } else {
            presenter.getDeletedMessages(group.getId());
        }
    }

    MessageAdapter.OnItemClickListener onItemClickListener = new MessageAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Message message) {
            Intent intent = new Intent(MessageActivity.this, MessageViewActivity.class);
            Bundle args = new Bundle();
            if (group != null) {
                args.putSerializable("message", message);
            }
            intent.putExtras(args);
            startActivity(intent);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.releaseLoaders();
    }
}

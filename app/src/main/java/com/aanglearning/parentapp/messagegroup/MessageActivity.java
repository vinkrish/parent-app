package com.aanglearning.parentapp.messagegroup;

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

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.MessageDao;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.EndlessRecyclerViewScrollListener;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity implements MessageView,
        ActivityCompat.OnRequestPermissionsResultCallback  {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private MessagePresenter presenter;
    private Groups group;
    private MessageAdapter adapter;

    private static final int WRITE_STORAGE_PERMISSION = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group = new Groups();
            group.setId(extras.getLong("groupId"));
            group.setName(extras.getString("groupName"));
        }
        getSupportActionBar().setTitle(group.getName());

        presenter = new MessagePresenterImpl(this, new MessageInteractorImpl());

        setupRecyclerView();

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getMessages(group.getId());
            }
        });

        if(NetworkUtil.isNetworkAvailable(this)) {
            if(PermissionUtil.isStoragePermissionGranted(this, WRITE_STORAGE_PERMISSION)) {
                presenter.getMessages(group.getId());
            }
        } else {
            List<Message> messages = MessageDao.getGroupMessages(group.getId());
            adapter.setDataSet(messages);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            presenter.getMessages(group.getId());
        } else {
            showSnackbar("Permission has been denied");
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MessageAdapter(this, new ArrayList<Message>(0));
        recyclerView.setAdapter(adapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(NetworkUtil.isNetworkAvailable(MessageActivity.this)) {
                    presenter.getFollowupMessages(group.getId(), adapter.getDataSet().get(adapter.getDataSet().size()-1).getId());
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
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
    public void onMessageSaved(Message message) {
        adapter.insertDataSet(message);
    }

    @Override
    public void showMessages(ArrayList<Message> messages) {
        refreshLayout.setRefreshing(false);
        adapter.setDataSet(messages);
        backupMessages(messages);
    }

    private void backupMessages(final List<Message> messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageDao.clearGroupMessages(group.getId());
                MessageDao.insertGroupMessages(messages);
            }
        }).start();
    }

    @Override
    public void showFollowupMessages(ArrayList<Message> msgs) {
        adapter.updateDataSet(msgs);
    }

}
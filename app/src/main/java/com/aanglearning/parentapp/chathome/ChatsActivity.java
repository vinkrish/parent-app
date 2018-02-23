package com.aanglearning.parentapp.chathome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.aanglearning.parentapp.BaseActivity;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.chat.ChatActivity;
import com.aanglearning.parentapp.dao.ChatDao;
import com.aanglearning.parentapp.model.Chat;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.Conversion;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PaddedItemDecoration;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsActivity extends BaseActivity implements ChatsView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.no_chats) LinearLayout noChats;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ChildInfo childInfo;
    private ChatsPresenter presenter;
    private ChatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new ChatsPresenterImpl(this, new ChatsInteractorImpl());

        setupRecyclerView();

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getChats(childInfo.getStudentId());
            }
        });

        setNavigationItem(7);

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getChats(childInfo.getStudentId());
        } else {
            showOfflineData();
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new PaddedItemDecoration(this, Conversion.dpToPx(68, getApplicationContext())));

        adapter = new ChatsAdapter(new ArrayList<Chat>(0), mItemListener);
        recyclerView.setAdapter(adapter);
    }

    private void showOfflineData() {
        List<Chat> chats = ChatDao.getChats();
        if(chats.size() == 0) {
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(chats);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
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
        showSnackbar(message);
        showOfflineData();
    }

    @Override
    public void setChats(List<Chat> chats) {
        if(chats.size() == 0) {
            ChatDao.clear();
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(chats);
            backupChats(chats);
        }
        refreshLayout.setRefreshing(false);
    }

    private void backupChats(final List<Chat> chats) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatDao.clear();
                ChatDao.insertMany(chats);
            }
        }).start();
    }

    ChatsAdapter.OnItemClickListener mItemListener = new ChatsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Chat chat) {
            Intent intent = new Intent(ChatsActivity.this, ChatActivity.class);
            intent.putExtra("recipientId", chat.getTeacherId());
            intent.putExtra("recipientName", chat.getTeacherName());
            intent.putExtra("recipientRole", chat.getCreatorRole());
            startActivity(intent);
            overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}

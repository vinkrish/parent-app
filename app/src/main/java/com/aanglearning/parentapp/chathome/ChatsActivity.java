package com.aanglearning.parentapp.chathome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.chat.ChatActivity;
import com.aanglearning.parentapp.model.Chat;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsActivity extends AppCompatActivity implements ChatsView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ChildInfo childInfo;
    private ChatsPresenter presenter;
    private ChatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new ChatsPresenterImpl(this, new ChatsInteractorImpl());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new ChatsAdapter(new ArrayList<Chat>(0), mItemListener);
        recyclerView.setAdapter(adapter);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getChats(childInfo.getStudentId());
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
    public void hideProgess() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        refreshLayout.setRefreshing(false);
        showSnackbar(message);
    }

    @Override
    public void setGroups(List<Chat> chats) {
        refreshLayout.setRefreshing(false);
        adapter.setDataSet(chats);
    }

    ChatsAdapter.OnItemClickListener mItemListener = new ChatsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Chat chat) {
            Intent intent = new Intent(ChatsActivity.this, ChatActivity.class);
            intent.putExtra("recipientId", chat.getTeacherId());
            intent.putExtra("recipientName", chat.getTeacherName());
            startActivity(intent);
        }
    };

}

package com.aanglearning.parentapp.messagegroup;

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
import android.view.View;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.GroupDao;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity implements MessageView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progressBar;

    private MessagePresenter presenter;
    private Groups group;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new MessagePresenterImpl(this, new MessageInteractorImpl());

        setupRecyclerView();
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
                presenter.getFollowupMessages(group.getId(), adapter.getDataSet().get(adapter.getDataSet().size()-1).getId());
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
                presenter.getMessages(group.getId());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        group = GroupDao.getGroup();
        getSupportActionBar().setTitle(group.getName());
        presenter.getMessages(group.getId());
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
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
    }

    @Override
    public void showFollowupMessages(ArrayList<Message> msgs) {
        adapter.updateDataSet(msgs);
    }

}

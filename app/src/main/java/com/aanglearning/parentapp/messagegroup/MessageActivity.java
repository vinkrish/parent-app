package com.aanglearning.parentapp.messagegroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.GroupDao;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.EndlessRecyclerViewScrollListener;
import com.aanglearning.parentapp.util.ImageUploadActivity;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity implements MessageView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private static final String TAG = "MessageActivity";
    private MessagePresenter presenter;
    private Groups group;
    private ChildInfo childInfo;
    private ArrayList<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private FloatingActionButton fabButton;
    final static int REQ_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new MessagePresenterImpl(this, new MessageInteractorImpl());

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MessageAdapter(this, messages);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getFollowupMessages(group.getId(), messages.get(messages.size()-1).getId());
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        group = GroupDao.getGroup();
        //groupName.setText(group.getName());
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
        showSnackbar(message);
    }

    @Override
    public void onMessageSaved(Message message) {
        adapter.insertDataSet(message);
    }

    @Override
    public void showMessages(ArrayList<Message> messages) {
        this.messages = messages;
        adapter.setDataSet(messages);
    }

    @Override
    public void showFollowupMessages(ArrayList<Message> msgs) {
        adapter.updateDataSet(msgs);
        this.messages = adapter.getDataSet();
    }

}

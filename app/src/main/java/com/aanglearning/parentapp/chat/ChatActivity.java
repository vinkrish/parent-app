package com.aanglearning.parentapp.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.MessageDao;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.EndlessRecyclerViewScrollListener;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.no_chats) LinearLayout noChats;
    @BindView(R.id.new_msg) EditText newMsg;
    @BindView(R.id.enter_msg) ImageView enterMsg;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private long recipientId;
    private String recipientName;
    private String recipientRole;
    private ChildInfo childInfo;
    private ChatPresenter presenter;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipientId = getIntent().getLongExtra("recipientId", 0);
            recipientName = extras.getString("recipientName", "");
            recipientRole = extras.getString("recipientRole", "");
        }
        getSupportActionBar().setTitle(recipientName);

        presenter = new ChatPresenterImpl(this, new ChatInteractorImpl());

        setupRecyclerView();

        newMsg.addTextChangedListener(newMsgWatcher);

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getMessages("student", childInfo.getStudentId(), recipientRole, recipientId);
        } else {
            List<Message> messages = MessageDao.getMessages(childInfo.getStudentId(), "student", recipientId, recipientRole);
            if(messages.size() == 0) {
                noChats.setVisibility(View.VISIBLE);
            } else {
                noChats.setVisibility(View.INVISIBLE);
                adapter.setDataSet(messages);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ChatAdapter(new ArrayList<Message>(0));
        recyclerView.setAdapter(adapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
                    presenter.getFollowupMessages("student", childInfo.getStudentId(), recipientRole, recipientId,
                            adapter.getDataSet().get(adapter.getDataSet().size()-1).getId());
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }, 100);
                }
            }
        });
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
        noChats.setVisibility(View.INVISIBLE);
        adapter.insertDataSet(message);
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void showMessages(List<Message> messages) {
        if(messages.size() == 0) {
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(messages);
            backupChats(messages);
        }
    }

    private void backupChats(final List<Message> messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageDao.clearChatMessages(childInfo.getStudentId(), "student", recipientId, "teacher");
                MessageDao.insertChatMessages(messages);
            }
        }).start();
    }

    @Override
    public void showFollowupMessages(List<Message> msgs) {
        adapter.updateDataSet(msgs);
    }

    public void newMsgSendListener (View view) {
        sendMessage("text", "");
        newMsg.setText("");
    }

    private void sendMessage(String messageType, String imgUrl) {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if(newMsg.getText().toString().trim().isEmpty()) {
            showSnackbar("Please enter message");
        } else {
            if (NetworkUtil.isNetworkAvailable(this)) {
                Message message = new Message();
                message.setSenderId(childInfo.getStudentId());
                message.setSenderName(childInfo.getName());
                message.setSenderRole("student");
                message.setRecipientId(recipientId);
                message.setRecipientRole(recipientRole);
                message.setGroupId(0);
                message.setMessageType(messageType);
                message.setImageUrl(imgUrl);
                message.setMessageBody(newMsg.getText().toString());
                message.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                presenter.saveMessage(message);
            } else {
                showSnackbar("You are offline,check your internet.");
            }
        }
    }

    private final TextWatcher newMsgWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (newMsg.getText().toString().equals("")) {
            } else {
                enterMsg.setImageResource(R.drawable.ic_chat_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                enterMsg.setImageResource(R.drawable.ic_chat_send);
            }else{
                enterMsg.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };

}

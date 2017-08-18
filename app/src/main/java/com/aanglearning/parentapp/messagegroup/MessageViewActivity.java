package com.aanglearning.parentapp.messagegroup;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.aanglearning.parentapp.util.YouTubeHelper;
import com.aanglearning.parentapp.util.YoutubeDeveloperKey;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageViewActivity extends AppCompatActivity
        implements YouTubePlayer.OnInitializedListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.shared_image)
    PhotoView sharedImage;
    @BindView(R.id.message)
    TextView messageTV;

    private Message message;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = (Message) extras.getSerializable("message");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(message.getSenderName());
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
        getSupportActionBar().setSubtitle(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));

        if(!message.getMessageBody().equals("")) {
            messageTV.setVisibility(View.VISIBLE);
            messageTV.setText(message.getMessageBody());
        }

        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_view);
        FragmentManager fm = getSupportFragmentManager();

        if(message.getVideoUrl() != null && !message.getVideoUrl().equals("")) {
            YouTubeHelper youTubeHelper = new YouTubeHelper();
            videoId = youTubeHelper.extractVideoIdFromUrl(message.getVideoUrl());

            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .show(frag)
                    .commit();
            frag.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, this);
        } else {
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                    .hide(frag)
                    .commit();
        }

        if (message.getImageUrl() != null && !message.getImageUrl().equals("")) {
            sharedImage.setVisibility(View.VISIBLE);
        }

        File file = new File(Environment.getExternalStorageDirectory().getPath(),
                "Shikshitha/Parent/" + SharedPreferenceUtil.getProfile(this).getSchoolId() + "/" + message.getImageUrl());
        if (file.exists()) {
            sharedImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored && videoId != null) {
            player.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
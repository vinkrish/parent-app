package com.aanglearning.parentapp.messagegroup;

import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.model.Service;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.aanglearning.parentapp.util.YouTubeHelper;
import com.aanglearning.parentapp.util.YoutubeDeveloperKey;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyPresigningClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechPresignRequest;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageViewActivity extends AppCompatActivity
        implements YouTubePlayer.OnInitializedListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.shared_image) PhotoView sharedImage;
    @BindView(R.id.message) TextView messageTV;

    private static final String COGNITO_POOL_ID = "us-west-2:6e697a2f-1eed-457e-ad34-5df567b1f0be";
    private static final Regions MY_REGION = Regions.US_WEST_2;
    CognitoCachingCredentialsProvider credentialsProvider;

    private Menu menu;
    private ChildInfo childInfo;
    private Message message;
    private String videoId;

    private AmazonPollyPresigningClient client;
    MediaPlayer mediaPlayer;

    private Toast myToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.speak_overflow, menu);
        this.menu = menu;
        return true;
    }

    private void init() {
        childInfo = SharedPreferenceUtil.getProfile(getApplicationContext());

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

        checkSpeakService();

        initPollyClient();

        setupNewMediaPlayer();
    }

    void initPollyClient() {
        // Initialize the Amazon Cognito credentials provider.
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                COGNITO_POOL_ID,
                MY_REGION
        );

        // Create a client that supports generation of presigned URLs.
        client = new AmazonPollyPresigningClient(credentialsProvider);
    }

    public void checkSpeakService() {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<Service> queue = api.getSpeakService(childInfo.getSchoolId());
        queue.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if(response.isSuccessful() && response.body().isSpeak()) {
                    if(!message.getMessageBody().equals("")) {
                        menu.findItem(R.id.action_speak).setVisible(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {}
        });
    }

    public void readMessage(MenuItem item) {
        menu.findItem(R.id.action_speak).setVisible(false);
        new setupBackground().execute(message.getMessageBody());
        showToast("Reading the message...");
    }

    private class setupBackground extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            SynthesizeSpeechPresignRequest synthesizeSpeechPresignRequest =
                    new SynthesizeSpeechPresignRequest()
                            .withText(params[0])
                            .withVoiceId("Aditi")
                            .withOutputFormat(OutputFormat.Mp3);

            URL presignedSynthesizeSpeechUrl =
                    client.getPresignedSynthesizeSpeechUrl(synthesizeSpeechPresignRequest);

            if (mediaPlayer.isPlaying()) {
                setupNewMediaPlayer();
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(presignedSynthesizeSpeechUrl.toString());
            } catch (IOException e) {
                Log.e("TAG", "Unable to set data source for the media player! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mediaPlayer.prepareAsync();
        }
    }

    void setupNewMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                setupNewMediaPlayer();
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                //menu.findItem(R.id.action_speak).setVisible(true);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                menu.findItem(R.id.action_speak).setVisible(false);
                return false;
            }
        });
    }

    private void showToast(String msg){
        if(myToast !=null){
            myToast.cancel();
        }
        myToast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        myToast.show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored && videoId != null) {
            player.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}

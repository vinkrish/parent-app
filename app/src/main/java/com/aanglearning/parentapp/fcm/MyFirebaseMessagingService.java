package com.aanglearning.parentapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.chat.ChatActivity;
import com.aanglearning.parentapp.chathome.ChatsActivity;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.MessageEvent;
import com.aanglearning.parentapp.util.AppGlobal;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        AppGlobal.setSqlDbHelper(getApplicationContext());

        if (remoteMessage.getData().size() > 0) {
            ArrayList<ChildInfo> childInfos = ChildInfoDao.getChildInfos();
            for (ChildInfo childInfo : childInfos) {
                if(childInfo.getName().equals(remoteMessage.getData().get("recipient_name"))) {
                    SharedPreferenceUtil.saveProfile(getApplicationContext(), childInfo);
                    break;
                }
            }
            if(App.isActivityVisible()) {
                EventBus.getDefault().post(new MessageEvent(remoteMessage.getData().get("message"),
                        Long.valueOf(remoteMessage.getData().get("sender_id"))));
            } else {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_stat_notify)
                                .setContentTitle(remoteMessage.getData().get("sender_name"))
                                .setContentText(remoteMessage.getData().get("message"))
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")));

                mBuilder.setAutoCancel(true);

                Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                resultIntent.putExtra("recipientId", Long.valueOf(remoteMessage.getData().get("sender_id")));
                resultIntent.putExtra("recipientName", remoteMessage.getData().get("sender_name"));
                resultIntent.putExtra("recipientRole", remoteMessage.getData().get("sender_role"));
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(ChatsActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }
}

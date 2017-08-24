package com.aanglearning.parentapp.messagegroup;

import android.app.IntentService;
import android.content.Intent;

import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.dao.MessageRecipientDao;
import com.aanglearning.parentapp.model.MessageRecipient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class MessageRecipientIntentService extends IntentService {

    public MessageRecipientIntentService() {
        super("MessageRecipientIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long recipientId = intent.getLongExtra("recipient_id", 0);
        String recipientName = intent.getStringExtra("recipient_name");
        long groupId = intent.getLongExtra("group_id", 0);

        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        List<MessageRecipient> mrList = MessageRecipientDao.getMessageRecipients(recipientId, recipientName, groupId);

        if(mrList.size() > 0) {
            Call<List<MessageRecipient>> queue = api.saveMessageRecipient(mrList);
            queue.enqueue(new Callback<List<MessageRecipient>>() {
                @Override
                public void onResponse(Call<List<MessageRecipient>> call, Response<List<MessageRecipient>> response) {
                    if(response.isSuccessful()) {
                        MessageRecipientDao.insertMany(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<MessageRecipient>> call, Throwable t) {

                }
            });
        }

    }

}

package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.DeletedMessage;
import com.aanglearning.parentapp.model.Message;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 07-04-2017.
 */

class MessageInteractorImpl implements MessageInteractor {

    @Override
    public void getRecentMessages(long groupId, long messageId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<ArrayList<Message>> queue = api.getGroupMessagesAboveId(groupId, messageId);
        queue.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if(response.isSuccessful()) {
                    listener.onRecentMessagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getMessages(long groupId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<ArrayList<Message>> queue = api.getGroupMessages(groupId);
        queue.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if(response.isSuccessful()) {
                    listener.onMessageReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentDeletedMessages(long groupId, long id, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<ArrayList<DeletedMessage>> queue = api.getDeletedMessagesAboveId(groupId, id);
        queue.enqueue(new Callback<ArrayList<DeletedMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<DeletedMessage>> call, Response<ArrayList<DeletedMessage>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedMessagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeletedMessage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getDeletedMessages(long groupId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<ArrayList<DeletedMessage>> queue = api.getDeletedMessages(groupId);
        queue.enqueue(new Callback<ArrayList<DeletedMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<DeletedMessage>> call, Response<ArrayList<DeletedMessage>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedMessagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeletedMessage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

}

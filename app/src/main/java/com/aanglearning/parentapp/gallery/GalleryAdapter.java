package com.aanglearning.parentapp.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Album;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 31-10-2017.
 */

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
    private Context context;
    private long schoolId;
    private List<Album> items;

    GalleryAdapter(Context context, long schoolId, List<Album> items) {
        this.context = context;
        this.schoolId = schoolId;
        this.items = items;
    }

    List<Album> getDataSet() {
        return items;
    }

    @UiThread
    void replaceData(List<Album> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @UiThread
    void updateDataSet(List<Album> itms) {
        int pos = items.size();
        this.items.addAll(itms);
        notifyItemRangeInserted(pos, items.size() - 1);
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new GalleryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cover_pic) ImageView coverPic;
        @BindView(R.id.album_name) TextView albumName;
        @BindView(R.id.card_view) RelativeLayout cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Album album) {
            albumName.setText(album.getName());
            if(album.getCoverPic().equals("")) {
                coverPic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder));
            } else {
                setImage(album.getCoverPic());
            }
        }

        private void setImage(String coverPhoto) {
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Parent/" + schoolId);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, coverPhoto);
            if(file.exists()) {
                coverPic.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(context)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + schoolId + "/" + coverPhoto)
                        .placeholder(R.drawable.placeholder)
                        .into(coverPic, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable)coverPic.getDrawable()).getBitmap();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {
                                coverPic.setImageResource(R.drawable.placeholder);
                            }
                        });
            }
        }

    }
}

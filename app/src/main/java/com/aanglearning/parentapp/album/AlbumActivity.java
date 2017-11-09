package com.aanglearning.parentapp.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.AlbumImageDao;
import com.aanglearning.parentapp.dao.DeletedAlbumImageDao;
import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.AlbumImage;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.DeletedAlbumImage;
import com.aanglearning.parentapp.util.GridSpacingItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.RecyclerItemClickListener;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends AppCompatActivity implements AlbumView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.noGroups) LinearLayout noGroups;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Album album;
    private ChildInfo childInfo;
    private AlbumAdapter adapter;
    private AlbumPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            album = (Album) extras.getSerializable("album");
            getSupportActionBar().setTitle(album.getName());
        }

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new AlbumPresenterImpl(this, new AlbumInteractorImpl());

        setupRecyclerView();

        loadOfflineData();

        if(NetworkUtil.isNetworkAvailable(this)) {
            if(adapter.getDataSet().size() == 0) {
                presenter.getAlbumImages(album.getId());
            } else {
                presenter.getAlbumImagesAboveId(album.getId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
        }
    }

    private void setupRecyclerView() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true));
        adapter = new AlbumAdapter(getApplicationContext(), childInfo.getSchoolId(),
                new ArrayList<AlbumImage>(0));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AlbumActivity.this, ImageSlideActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("album", album);
                args.putInt("position", position);
                intent.putExtras(args);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void loadOfflineData() {
        ArrayList<AlbumImage> albumImages = AlbumImageDao.getAlbumImages(album.getId());
        if(albumImages.size() == 0) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.setDataSet(albumImages);
        }
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
    public void setRecentAlbumImages(ArrayList<AlbumImage> albumImages) {
        adapter.updateDataSet(albumImages);
        backupAlbumImages(albumImages);
        syncDeletedAlbums();
    }

    @Override
    public void setAlbumImages(ArrayList<AlbumImage> albumImages) {
        if(albumImages.size() == 0) {
            recyclerView.invalidate();
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.setDataSet(albumImages);
            backupAlbumImages(albumImages);
        }
        syncDeletedAlbums();
    }

    @Override
    public void onDeletedAlbumImageSync() {
        presenter.getAlbumUpdate(album.getId());
    }

    private void backupAlbumImages(final List<AlbumImage> albumImages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlbumImageDao.insert(albumImages);
            }
        }).start();
    }

    private void syncDeletedAlbums() {
        DeletedAlbumImage deletedAlbum = DeletedAlbumImageDao.getNewestDeletedAlbumImage();
        if(deletedAlbum.getId() == 0) {
            presenter.getDeletedAlbumImages(album.getId());
        } else {
            presenter.getRecentDeletedAlbumImages(album.getId(), deletedAlbum.getId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}

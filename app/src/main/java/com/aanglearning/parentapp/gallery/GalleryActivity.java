package com.aanglearning.parentapp.gallery;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.album.AlbumActivity;
import com.aanglearning.parentapp.dao.AlbumDao;
import com.aanglearning.parentapp.dao.DeletedAlbumDao;
import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.DeletedAlbum;
import com.aanglearning.parentapp.util.GridSpacingItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PermissionUtil;
import com.aanglearning.parentapp.util.RecyclerItemClickListener;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements GalleryView,
        ActivityCompat.OnRequestPermissionsResultCallback{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.noGroups) LinearLayout noGroups;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ChildInfo childInfo;
    private GalleryPresenter presenter;
    private GalleryAdapter adapter;

    private static final int WRITE_STORAGE_PERMISSION = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new GalleryPresenterImpl(this, new GalleryInteractorImpl());

        setupRecyclerView();

        loadOfflineData();

        if (PermissionUtil.isStoragePermissionGranted(this, WRITE_STORAGE_PERMISSION)) {
            syncGallery();
        }
    }

    private void syncGallery() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            if(adapter.getDataSet().size() == 0) {
                presenter.getAlbums(childInfo);
            } else {
                presenter.getAlbumsAboveId(childInfo, adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            syncGallery();
        } else {
            showSnackbar("Permission has been denied");
        }
    }

    private void loadOfflineData() {
        List<Album> albums = AlbumDao.getAlbums();
        if(albums.size() == 0) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums);
        }
    }

    private void setupRecyclerView() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        adapter = new GalleryAdapter(getApplicationContext(), childInfo.getSchoolId(), new ArrayList<Album>(0));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GalleryActivity.this, AlbumActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("album", adapter.getDataSet().get(position));
                intent.putExtras(args);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
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
    public void setAlbum(Album album) {
        if(adapter.getDataSet().size() == 0) {
            presenter.getAlbums(childInfo);
        } else {
            presenter.getAlbumsAboveId(childInfo, adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
        }
    }

    @Override
    public void setRecentAlbums(List<Album> albums) {
        adapter.updateDataSet(albums);
        backupAlbums(albums);
        syncDeletedAlbums();
    }

    @Override
    public void setAlbums(List<Album> albums) {
        if(albums.size() == 0) {
            recyclerView.invalidate();
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums);
            backupAlbums(albums);
        }
        syncDeletedAlbums();
    }

    private void backupAlbums(final List<Album> albums) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlbumDao.insert(albums);
            }
        }).start();
    }

    private void syncDeletedAlbums() {
        DeletedAlbum deletedAlbum = DeletedAlbumDao.getNewestDeletedAlbum();
        if(deletedAlbum.getId() == 0) {
            presenter.getDeletedAlbums(childInfo);
        } else {
            presenter.getRecentDeletedAlbums(childInfo, deletedAlbum.getId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}

package com.aanglearning.parentapp.album;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.AlbumImageDao;
import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.AlbumImage;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageSlideActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pager) ViewPager viewPager;

    private Album album;
    private int startPosition;
    private long schoolId;
    private ArrayList<AlbumImage> albumImages;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slide);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            album = (Album) extras.getSerializable("album");
            startPosition = extras.getInt("position");
            schoolId = extras.getLong("schoolId");
            getSupportActionBar().setTitle(album.getName());
        }

        albumImages = AlbumImageDao.getAlbumImages(album.getId());
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(imageFragmentPagerAdapter);
        viewPager.setCurrentItem(startPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return albumImages.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SwipeFragment.newInstance(albumImages.get(position).getName(), schoolId);
        }
    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.image_swipe, container, false);
            PhotoView imageView = swipeView.findViewById(R.id.imageView);

            Bundle bundle = getArguments();
            String imageName = bundle.getString("imageName");
            long schoolId = bundle.getLong("schoolId");

            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Parent/" + schoolId );
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, imageName);
            if (file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            return swipeView;
        }

        static SwipeFragment newInstance(String imageName, long schoolId) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("imageName", imageName);
            bundle.putLong("schoolId", schoolId);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }
}

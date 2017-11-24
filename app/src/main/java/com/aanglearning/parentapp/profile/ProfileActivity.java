package com.aanglearning.parentapp.profile;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dao.GroupDao;
import com.aanglearning.parentapp.dao.StudentDao;
import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Student;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PermissionUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements ProfileView,
        ActivityCompat.OnRequestPermissionsResultCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.clas) TextView clas;
    @BindView(R.id.section) TextView section;
    @BindView(R.id.sectionLayout) TextInputLayout sectionLayout;
    @BindView(R.id.school) TextView school;
    @BindView(R.id.father) TextView father;
    @BindView(R.id.mother) TextView mother;
    @BindView(R.id.dob) TextView dob;
    @BindView(R.id.mobile) TextView mobile;

    private ChildInfo childInfo;
    private Student student;
    private ProfilePresenter presenter;

    private static final int WRITE_STORAGE_PERMISSION = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        name.setText(childInfo.getName());
        clas.setText(childInfo.getClassName());
        section.setText(childInfo.getSectionName());
        school.setText(childInfo.getSchoolName());

        presenter = new ProfilePresenterImpl(this, new ProfileInteractorImpl());

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getStudent(childInfo.getStudentId());
        } else {
            student = StudentDao.getStudent(childInfo.getStudentId());
            viewStudent();
            setImage();
        }

        if(childInfo.getSectionName().equals("none")) {
            sectionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setImage();
        } else {
            showSnackbar("Permission has been denied");
        }
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        refreshLayout.setRefreshing(false);
        showSnackbar(message);
    }

    @Override
    public void setProfile(Student student) {
        this.student = student;
        viewStudent();
        if(PermissionUtil.isStoragePermissionGranted(this, WRITE_STORAGE_PERMISSION)) {
            setImage();
        }
        ChildInfoDao.update(student.getId(), student.getImage());
        SharedPreferenceUtil.updateProfile(this, student.getImage());
        backupStudent(student);
    }

    private void viewStudent() {
        father.setText(student.getFatherName());
        mother.setText(student.getMotherName());
        dob.setText(student.getDateOfBirth());
        mobile.setText(student.getMobile1());
    }

    private void backupStudent(final Student student) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StudentDao.clear(childInfo.getStudentId());
                StudentDao.insert(student);
            }
        }).start();
    }

    private void setImage() {
        File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Parent/" + student.getSchoolId());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File file = new File(dir, student.getImage());
        if(file.exists()) {
            profileImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            Picasso.with(this)
                    .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + student.getSchoolId() + "/" + student.getImage())
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable)profileImage.getDrawable()).getBitmap();
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError() {
                            profileImage.setImageResource(R.drawable.ic_account_black);
                        }
                    });
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}

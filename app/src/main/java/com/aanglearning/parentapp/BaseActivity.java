package com.aanglearning.parentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aanglearning.parentapp.attendance.AbsentViewActivity;
import com.aanglearning.parentapp.calendar.CalendarActivity;
import com.aanglearning.parentapp.chathome.ChatsActivity;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dao.ServiceDao;
import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.gallery.GalleryActivity;
import com.aanglearning.parentapp.homework.HomeworkActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Service;
import com.aanglearning.parentapp.profile.ProfileActivity;
import com.aanglearning.parentapp.reportcard.ReportActivity;
import com.aanglearning.parentapp.sqlite.SqlDbHelper;
import com.aanglearning.parentapp.timetable.TimetableActivity;
import com.aanglearning.parentapp.util.PermissionUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

public class BaseActivity extends AppCompatActivity {
    public NavigationView navigationView;
    public Spinner spinner;
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ArrayList<ChildInfo> childInfos;
    private ChildInfo childInfo;
    private long schoolId, groupId;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResId) {
        drawerLayout = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_base, null);
        navigationView = drawerLayout.findViewById(R.id.navigation_view);
        toolbar = drawerLayout.findViewById(R.id.toolbar);
        spinner = drawerLayout.findViewById(R.id.spinner_nav);
        FrameLayout activityContainer = drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(drawerLayout);

        setupDrawerContent(navigationView);

        setUpDrawerToggle();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setupHeaderAndSpinner();

        hideDrawerItem();
    }

    private void setUpDrawerToggle() {
        actionBarDrawerToggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard_item:
                                startActivity(new Intent(BaseActivity.this, DashboardActivity.class));
                                finish();
                                break;
                            case R.id.attendance_item:
                                startActivity(new Intent(BaseActivity.this, AbsentViewActivity.class));
                                finish();
                                break;
                            case R.id.homework_item:
                                startActivity(new Intent(BaseActivity.this, HomeworkActivity.class));
                                finish();
                                break;
                            case R.id.timetable_item:
                                startActivity(new Intent(BaseActivity.this, TimetableActivity.class));
                                finish();
                                break;
                            case R.id.event_item:
                                startActivity(new Intent(BaseActivity.this, CalendarActivity.class));
                                break;
                            case R.id.result_item:
                                startActivity(new Intent(BaseActivity.this, ReportActivity.class));
                                finish();
                                break;
                            case R.id.gallery_item:
                                startActivity(new Intent(BaseActivity.this, GalleryActivity.class));
                                finish();
                                break;
                            case R.id.chat_item:
                                startActivity(new Intent(BaseActivity.this, ChatsActivity.class));
                                finish();
                                break;
                            case R.id.profile_item:
                                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                                finish();
                                break;
                            case R.id.logout_item:
                                logout();
                                break;
                        }
                        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        return false;
                    }
                });
    }

    protected void setNavigationItem(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    private void setupHeaderAndSpinner() {
        childInfos = ChildInfoDao.getChildInfos();
        ArrayList<String> names = new ArrayList<>();
        for (ChildInfo childInfo : childInfos) {
            names.add(childInfo.getName());
        }

        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this, R.layout.spinner_header, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        childInfo = SharedPreferenceUtil.getProfile(this);
        if (childInfo.getName().equals("") && childInfos.size()>0) {
            SharedPreferenceUtil.saveProfile(this, childInfos.get(0));
            childInfo = SharedPreferenceUtil.getProfile(this);
        } else {
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(childInfo.getName()));
        }

        setProfile();

        hideDrawerItem();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if(check) {
                    SharedPreferenceUtil.saveProfile(getApplicationContext(), childInfos.get(pos));
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    check = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setProfile() {
        View hView = navigationView.inflateHeaderView(R.layout.header);
        final ImageView imageView = hView.findViewById(R.id.user_image);
        TextView tv = hView.findViewById(R.id.name);
        tv.setText(childInfo.getName());
        schoolId = childInfo.getSchoolId();

        if(PermissionUtil.getStoragePermissionStatus(this)) {
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Parent/"+childInfo.getSchoolId());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, childInfo.getImage());
            if(file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void hideDrawerItem() {
        Menu menu = navigationView.getMenu();
        Service service = ServiceDao.getServices(schoolId);
        if(!service.isAttendance()) menu.findItem(R.id.attendance_item).setVisible(false);
        if(!service.isHomework()) menu.findItem(R.id.homework_item).setVisible(false);
        if(!service.isReport())menu.findItem(R.id.result_item).setVisible(false);
        if(!service.isChat()) menu.findItem(R.id.chat_item).setVisible(false);
        if (!service.isTimetable()) menu.findItem(R.id.timetable_item).setVisible(false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpDrawerToggle();
        actionBarDrawerToggle.syncState();
    }

    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferenceUtil.logout(BaseActivity.this);
                SharedPreferenceUtil.clearProfile(BaseActivity.this);
                SqlDbHelper.getInstance(BaseActivity.this).deleteTables();
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        }
    }
}

package com.aanglearning.parentapp.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.attendance.AttendanceActivity;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dao.ServiceDao;
import com.aanglearning.parentapp.homework.HomeworkActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Service;
import com.aanglearning.parentapp.util.AppGlobal;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity
        implements DashboardView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.spinner_nav)
    Spinner spinner;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    private ArrayList<ChildInfo> childInfos;
    private long schoolId;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        check = false;
        ButterKnife.bind(this);
        
        AppGlobal.setSqlDbHelper(getApplicationContext());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard_item:
                                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                                break;
                            case R.id.attendance_item:
                                startActivity(new Intent(DashboardActivity.this, AttendanceActivity.class));
                                break;
                            case R.id.homework_item:
                                startActivity(new Intent(DashboardActivity.this, HomeworkActivity.class));
                                break;
                            case R.id.chat_item:
                                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                                break;
                            case R.id.logout_item:
                                SharedPreferenceUtil.logout(DashboardActivity.this);
                                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                                finish();
                                break;
                            default:
                                break;
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        ActionBarDrawerToggle actionBarDrawerToggle = new
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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        childInfos = ChildInfoDao.getChildInfos();
        ArrayList<String> names = new ArrayList<>();
        for (ChildInfo childInfo : childInfos) {
            names.add(childInfo.getName());
        }

        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this, R.layout.spinner_header, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ChildInfo childInfo = SharedPreferenceUtil.getProfile(this);
        if (childInfo.getName().equals("")) {
            SharedPreferenceUtil.saveProfile(this, childInfos.get(0));
        } else {
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(childInfo.getName()));
        }

        View hView = navigationView.inflateHeaderView(R.layout.header);
        ImageView imageView = (ImageView) hView.findViewById(R.id.user_image);
        TextView tv = (TextView) hView.findViewById(R.id.name);
        imageView.setImageResource(R.drawable.child);
        tv.setText(childInfo.getName());
        schoolId = childInfo.getSchoolId();

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

    @Override
    public void onResume() {
        super.onResume();
        check = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
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
        if(!service.getIsAttendance()) menu.findItem(R.id.attendance_item).setVisible(false);
        if(!service.getIsHomework()) menu.findItem(R.id.homework_item).setVisible(false);
        if(!service.getIsChat()) menu.findItem(R.id.chat_item).setVisible(false);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgess() {

    }

    @Override
    public void showError(String message) {

    }
}

package com.aanglearning.parentapp.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.attendance.AbsentViewActivity;
import com.aanglearning.parentapp.calendar.CalendarActivity;
import com.aanglearning.parentapp.chathome.ChatsActivity;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dao.GroupDao;
import com.aanglearning.parentapp.dao.ServiceDao;
import com.aanglearning.parentapp.homework.HomeworkActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.messagegroup.MessageActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Service;
import com.aanglearning.parentapp.profile.ProfileActivity;
import com.aanglearning.parentapp.sqlite.SqlDbHelper;
import com.aanglearning.parentapp.timetable.TimetableActivity;
import com.aanglearning.parentapp.util.AppGlobal;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PermissionUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements GroupView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.spinner_nav) Spinner spinner;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.noGroups) LinearLayout noGroups;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

    private ArrayList<ChildInfo> childInfos;
    private ChildInfo childInfo;
    private long schoolId;
    private boolean check;

    private GroupPresenter presenter;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        check = false;
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        presenter = new GroupPresenterImpl(this, new GroupInteractorImpl());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new GroupAdapter(new ArrayList<Groups>(0), mItemListener);
        recyclerView.setAdapter(adapter);

        setupDrawerContent(navigationView);

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

        setupHeaderAndSpinner();

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check = false;
                presenter.getGroups(childInfo.getStudentId());
            }
        });

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(NetworkUtil.isNetworkAvailable(DashboardActivity.this)) {
            presenter.getGroups(childInfo.getStudentId());
        } else {
            loadOfflineData();
        }
    }

    private void loadOfflineData() {
        List<Groups> groups = GroupDao.getGroups(childInfo.getClassId());
        if(groups.size() == 0) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.replaceData(groups);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
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
        if (!service.getIsTimetable()) menu.findItem(R.id.timetable_item).setVisible(false);
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
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
        showSnackbar(message);
        loadOfflineData();
    }

    @Override
    public void setGroups(List<Groups> groups) {
        if(groups.size() == 0) {
            recyclerView.invalidate();
            GroupDao.clear(childInfo.getSectionId());
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.replaceData(groups);
            backupGroups(groups);
        }
        refreshLayout.setRefreshing(false);
        updateFcmToken();
    }

    private void backupGroups(final List<Groups> groups) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GroupDao.clear(childInfo.getSectionId());
                GroupDao.insertMany(groups);
            }
        }).start();
    }

    private void updateFcmToken() {
        if(!SharedPreferenceUtil.isFcmTokenSaved(DashboardActivity.this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    presenter.updateFcmToken(SharedPreferenceUtil.getAuthorization(DashboardActivity.this));
                }
            }).start();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard_item:
                                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                                break;
                            case R.id.attendance_item:
                                startActivity(new Intent(DashboardActivity.this, AbsentViewActivity.class));
                                break;
                            case R.id.homework_item:
                                startActivity(new Intent(DashboardActivity.this, HomeworkActivity.class));
                                break;
                            case R.id.timetable_item:
                                startActivity(new Intent(DashboardActivity.this, TimetableActivity.class));
                                break;
                            case R.id.event_item:
                                startActivity(new Intent(DashboardActivity.this, CalendarActivity.class));
                                break;
                            case R.id.chat_item:
                                startActivity(new Intent(DashboardActivity.this, ChatsActivity.class));
                                break;
                            case R.id.profile_item:
                                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                                break;
                            case R.id.logout_item:
                                logout();
                                break;
                            default:
                                break;
                        }
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        return false;
                    }
                });
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
        final ImageView imageView = (ImageView) hView.findViewById(R.id.user_image);
        TextView tv = (TextView) hView.findViewById(R.id.name);
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
            } else {
                Picasso.with(this)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + childInfo.getSchoolId() + "/" + childInfo.getImage())
                        .placeholder(R.drawable.splash_image)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {
                                imageView.setImageResource(R.drawable.ic_account_black);
                            }
                        });
            }
        }
    }

    GroupAdapter.OnItemClickListener mItemListener = new GroupAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Groups group) {
            Intent intent = new Intent(DashboardActivity.this, MessageActivity.class);
            Bundle args = new Bundle();
            if(group != null){
                args.putSerializable("group", group);
            }
            intent.putExtras(args);
            startActivity(intent);
        }
    };

    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferenceUtil.logout(DashboardActivity.this);
                SharedPreferenceUtil.clearProfile(DashboardActivity.this);
                SqlDbHelper.getInstance(DashboardActivity.this).deleteTables();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dbBackup();
            }
        });
        alertDialog.show();
    }

    private void dbBackup() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.aanglearning.parentapp" + "/databases/parent.db";
        String backupDBPath = "parent";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

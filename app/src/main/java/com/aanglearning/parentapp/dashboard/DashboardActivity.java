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

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.attendance.AbsentViewActivity;
import com.aanglearning.parentapp.calendar.CalendarActivity;
import com.aanglearning.parentapp.chathome.ChatsActivity;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dao.DeletedGroupDao;
import com.aanglearning.parentapp.dao.GroupDao;
import com.aanglearning.parentapp.dao.MessageRecipientDao;
import com.aanglearning.parentapp.dao.ServiceDao;
import com.aanglearning.parentapp.gallery.GalleryActivity;
import com.aanglearning.parentapp.homework.HomeworkActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.messagegroup.MessageActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.DeletedGroup;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.MessageRecipient;
import com.aanglearning.parentapp.model.Service;
import com.aanglearning.parentapp.profile.ProfileActivity;
import com.aanglearning.parentapp.reportcard.ReportActivity;
import com.aanglearning.parentapp.settings.SettingsActivity;
import com.aanglearning.parentapp.sqlite.SqlDbHelper;
import com.aanglearning.parentapp.timetable.TimetableActivity;
import com.aanglearning.parentapp.util.Conversion;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.PaddedItemDecoration;
import com.aanglearning.parentapp.util.PermissionUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private long schoolId, groupId;
    private boolean check;
    private boolean isNotified;

    private GroupPresenter presenter;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isNotified = true;
            groupId = extras.getLong("group_id", 0);
        }

        presenter = new GroupPresenterImpl(this, new GroupInteractorImpl());

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

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new PaddedItemDecoration(this, Conversion.dpToPx(70, getApplicationContext())));

        adapter = new GroupAdapter(new ArrayList<Groups>(0), mItemListener);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check = false;
                if(GroupDao.getGroups(childInfo.getClassId()).size() == 0) {
                    presenter.getGroups(childInfo.getStudentId());
                } else {
                    presenter.getGroupsAboveId(childInfo.getStudentId(), GroupDao.getRecentGroup(childInfo.getClassId()).getId());
                }
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            if(!SharedPreferenceUtil.isMessageRecipientsSaved(getApplicationContext())) {
                presenter.getMessageRecipients(childInfo.getStudentId());
            } else loadGroups();
        } else {
            loadOfflineData();
        }
    }

    private void loadGroups() {
        loadOfflineData();
        if(isNotified) {
            Groups group = GroupDao.getGroup(groupId);
            if(group.getId() == 0) {
                presenter.getGroup(groupId);
            } else {
                setGroup(group);
            }
        } else {
            if(GroupDao.getGroups(childInfo.getClassId()).size() == 0) {
                presenter.getGroups(childInfo.getStudentId());
            } else {
                presenter.getGroupsAboveId(childInfo.getStudentId(), GroupDao.getRecentGroup(childInfo.getClassId()).getId());
            }
        }
    }

    private void loadOfflineData() {
        adapter.replaceData(GroupDao.getGroups(childInfo.getClassId()));
        if(adapter.getItemCount() == 0) {
            adapter.replaceData(GroupDao.getSchoolGroups());
        } else {
            adapter.updateDataSet(GroupDao.getSchoolGroups());
        }
        toggleNoGroupsVisibility();
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
        if (!service.isAttendance()) menu.findItem(R.id.attendance_item).setVisible(false);
        if (!service.isHomework()) menu.findItem(R.id.homework_item).setVisible(false);
        if (!service.isTimetable()) menu.findItem(R.id.timetable_item).setVisible(false);
        if (!service.isReport())menu.findItem(R.id.result_item).setVisible(false);
        if (!service.isGallery())menu.findItem(R.id.gallery_item).setVisible(false);
        if (!service.isChat()) menu.findItem(R.id.chat_item).setVisible(false);
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
    }

    @Override
    public void backupGroup(Groups group) {
        GroupDao.insertMany(Collections.singletonList(group));
    }

    @Override
    public void setGroup(Groups group) {
        loadOfflineData();
        Intent intent = new Intent(DashboardActivity.this, MessageActivity.class);
        Bundle args = new Bundle();
        if(group != null){
            args.putSerializable("group", group);
        }
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void setRecentSchoolGroups(List<Groups> groups) {
        if(groups.size() > 0) {
            adapter.updateDataSet(groups);
            backupGroups(groups);
        }
        syncDeletedGroups();
    }

    @Override
    public void setSchoolGroups(List<Groups> groups) {
        if(adapter.getItemCount() == 0 && groups.size() > 0) {
            adapter.replaceData(groups);
        } else if(groups.size() > 0) {
            adapter.updateDataSet(groups);
        }
        toggleNoGroupsVisibility();
        backupGroups(groups);
        syncDeletedGroups();
    }

    @Override
    public void setRecentGroups(List<Groups> groups) {
        if(groups.size() > 0) {
            adapter.updateDataSet(groups);
            backupGroups(groups);
        }
        getSchoolGroups();
    }

    @Override
    public void setGroups(List<Groups> groups) {
        if(adapter.getDataSet().size() > 0 && groups.size() > 0) {
            adapter.updateDataSet(groups);
        } else if(groups.size() > 0){
            adapter.replaceData(groups);
        }
        backupGroups(groups);
        getSchoolGroups();
    }

    private void getSchoolGroups() {
        if(GroupDao.getSchoolGroups().size() == 0) {
            presenter.getSchoolGroups(childInfo.getSchoolId());
        } else {
            presenter.getSchoolGroupsAboveId(childInfo.getSchoolId(), GroupDao.getRecentSchoolGroup().getId());
        }
    }

    private void toggleNoGroupsVisibility() {
        if(adapter.getItemCount() == 0) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setMessageRecipients(List<MessageRecipient> mrList) {
        MessageRecipientDao.insertMany(mrList);
        SharedPreferenceUtil.messageRecipientsSaved(getApplicationContext());
        hideProgress();
        loadGroups();
    }

    private void backupGroups(final List<Groups> groups) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GroupDao.insertMany(groups);
            }
        }).start();
    }

    private void syncDeletedGroups() {
        DeletedGroup deletedGroup = DeletedGroupDao.getNewestDeletedGroup();
        if(deletedGroup.getId() == 0) {
            presenter.getDeletedGroups(childInfo.getSchoolId());
        } else {
            presenter.getRecentDeletedGroups(childInfo.getSchoolId(), deletedGroup.getId());
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                                finish();
                                break;
                            case R.id.attendance_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, AbsentViewActivity.class));
                                finish();
                                break;
                            case R.id.homework_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, HomeworkActivity.class));
                                finish();
                                break;
                            case R.id.timetable_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, TimetableActivity.class));
                                finish();
                                break;
                            case R.id.event_item:
                                drawerLayout.closeDrawers();
                                startActivity(new Intent(DashboardActivity.this, CalendarActivity.class));
                                break;
                            case R.id.result_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, ReportActivity.class));
                                finish();
                                break;
                            case R.id.gallery_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, GalleryActivity.class));
                                finish();
                                break;
                            case R.id.chat_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, ChatsActivity.class));
                                finish();
                                break;
                            case R.id.profile_item:
                                menuItem.setChecked(true);
                                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                                finish();
                                break;
                            case R.id.settings_item:
                                drawerLayout.closeDrawers();
                                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
                                break;
                            case R.id.logout_item:
                                logout();
                                break;
                        }
                        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
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
            } else {
                Picasso.with(this)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + childInfo.getSchoolId() + "/" + childInfo.getImage())
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
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
            overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
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
            }
        });
        alertDialog.show();
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
        }
    }

}

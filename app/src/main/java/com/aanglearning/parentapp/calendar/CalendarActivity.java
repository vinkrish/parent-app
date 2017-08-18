package com.aanglearning.parentapp.calendar;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Evnt;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity implements EventView{
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private EventPresenter presenter;
    private ChildInfo childInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        presenter = new EventPresenterImpl(this, new EventInteractorImpl());

        setUpViewPager();
        childInfo = SharedPreferenceUtil.getProfile(this);

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getEvents(childInfo.getSchoolId());
        } else {
            //loadOfflineData();
        }
    }

    private void setUpViewPager() {
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgess() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void setEvents(List<Evnt> events) {
        Evnts evnts = new Evnts();
        evnts.setEvents(events);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), evnts);
        viewPager.setAdapter(adapter);
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
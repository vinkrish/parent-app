package com.aanglearning.parentapp.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aanglearning.parentapp.dashboard.attendance.AttendanceFragment;
import com.aanglearning.parentapp.dashboard.homework.HomeworkFragment;

/**
 * Created by Vinay on 22-02-2017.
 */

class DashboardPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "Attendance", "Homework" };

    DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AttendanceFragment.newInstance();
            case 1:
                return HomeworkFragment.newInstance();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

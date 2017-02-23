package com.aanglearning.parentapp.dashboard.homework;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by Vinay on 23-02-2017.
 */

public class HomeworkView implements Parent<String> {
    private String subjectName;
    private List<String> homeworks;

    public HomeworkView(String subjectName, List<String> homeworks) {
        this.subjectName = subjectName;
        this.homeworks = homeworks;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<String> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<String> homeworks) {
        this.homeworks = homeworks;
    }

    @Override
    public List<String> getChildList() {
        return homeworks;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}

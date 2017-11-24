package com.aanglearning.parentapp.reportcard;

public class StudentScore {
    private long schId;
    private String schName;
    private float maxMark;
    private float mark;
    private String grade;

    public long getSchId() {
        return schId;
    }

    public void setSchId(long schId) {
        this.schId = schId;
    }

    public String getSchName() {
        return schName;
    }

    public void setSchName(String schName) {
        this.schName = schName;
    }

    public float getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(float maxMark) {
        this.maxMark = maxMark;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}

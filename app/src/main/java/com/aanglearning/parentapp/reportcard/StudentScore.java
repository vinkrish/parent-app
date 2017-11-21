package com.aanglearning.parentapp.reportcard;

public class StudentScore {
    private long schId;
    private String schName;
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

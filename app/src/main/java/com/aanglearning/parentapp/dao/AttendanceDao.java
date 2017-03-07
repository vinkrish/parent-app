package com.aanglearning.parentapp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Homework;
import com.aanglearning.parentapp.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 25-02-2017.
 */

public class AttendanceDao {

    public static int insert(List<Attendance> attendances) {
        String sql = "insert into attendance(Id, SectionId, StudentId, StudentName, SubjectId, Type, Session, DateAttendance, TypeOfLeave) " +
                "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Attendance attendance : attendances) {
                stmt.bindLong(1, attendance.getId());
                stmt.bindLong(2, attendance.getSectionId());
                stmt.bindLong(3, attendance.getStudentId());
                stmt.bindString(4, attendance.getStudentName());
                stmt.bindLong(5, attendance.getSubjectId());
                stmt.bindString(6, attendance.getType());
                stmt.bindLong(7, attendance.getSession());
                stmt.bindString(8, attendance.getDateAttendance());
                stmt.bindString(9, attendance.getTypeOfLeave());
                stmt.execute();
                stmt.clearBindings();
            }
        } catch (Exception e) {
            db.endTransaction();
            return 0;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return 1;
    }

    public static Attendance getAttendance(String date) {
        Attendance attendance = new Attendance();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from homework where HomeworkDate = '" + date + "'" , null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            attendance.setId(c.getLong(c.getColumnIndex("Id")));
            attendance.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            attendance.setStudentId(c.getLong(c.getColumnIndex("StudentId")));
            attendance.setStudentName(c.getString(c.getColumnIndex("StudentName")));
            attendance.setSubjectId(c.getLong(c.getColumnIndex("SubjectId")));
            attendance.setType(c.getString(c.getColumnIndex("Type")));
            attendance.setSession(c.getInt(c.getColumnIndex("Session")));
            attendance.setDateAttendance(c.getString(c.getColumnIndex("DateAttendance")));
            attendance.setTypeOfLeave(c.getString(c.getColumnIndex("TypeOfLeave")));
            c.moveToNext();
        }
        c.close();
        return attendance;
    }
}
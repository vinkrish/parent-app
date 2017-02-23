package com.aanglearning.parentapp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.AppGlobal;

import java.util.ArrayList;

/**
 * Created by Vinay on 21-02-2017.
 */

public class ChildInfoDao {

    public static int insert(ArrayList<ChildInfo> childInfos) {
        String sql = "insert into child_info(SchoolId, SchoolName, ClassId, ClassName, SectionId, SectionName, Name) " +
                "values(?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (ChildInfo info : childInfos) {
                stmt.bindLong(1, info.getSchoolId());
                stmt.bindString(2, info.getSchoolName());
                stmt.bindLong(3, info.getClassId());
                stmt.bindString(4, info.getClassName());
                stmt.bindLong(5, info.getSectionId());
                stmt.bindString(6, info.getSectionName());
                stmt.bindString(7, info.getName());
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

    public static ArrayList<ChildInfo> getChildInfos() {
        ArrayList<ChildInfo> childInfos = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from child_info", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            ChildInfo childInfo = new ChildInfo();
            childInfo.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            childInfo.setSchoolName(c.getString(c.getColumnIndex("SchoolName")));
            childInfo.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            childInfo.setClassName(c.getString(c.getColumnIndex("ClassName")));
            childInfo.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            childInfo.setSectionName(c.getString(c.getColumnIndex("SectionName")));
            childInfo.setName(c.getString(c.getColumnIndex("Name")));
            childInfos.add(childInfo);
            c.moveToNext();
        }
        c.close();
        return childInfos;
    }

    public static int clear() {
        String sql = "delete from child_info";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.execute();
            stmt.clearBindings();
        } catch (Exception e) {
            db.endTransaction();
            return 0;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return 1;
    }

}

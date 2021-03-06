package com.aanglearning.parentapp.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 03-04-2017.
 */

public class GroupDao {

    public static int insertMany(List<Groups> groups) {
        String sql = "insert into groups(Id, Name, IsSchool, SectionId, IsSection, ClassId, IsClass, " +
                "CreatedBy, CreatorName, CreatorRole, CreatedDate, IsActive, SchoolId, RecentMessage) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(Groups group: groups) {
                stmt.bindLong(1, group.getId());
                stmt.bindString(2, group.getName());
                stmt.bindString(3, Boolean.toString(group.isSchool()));
                stmt.bindLong(4, group.getSectionId());
                stmt.bindString(5, Boolean.toString(group.isSection()));
                stmt.bindLong(6, group.getClassId());
                stmt.bindString(7, Boolean.toString(group.isClas()));
                stmt.bindLong(8, group.getCreatedBy());
                stmt.bindString(9, group.getCreatorName());
                stmt.bindString(10, group.getCreatorRole());
                stmt.bindString(11, group.getCreatedDate());
                stmt.bindString(12, Boolean.toString(group.isActive()));
                stmt.bindLong(13, group.getSchoolId());
                stmt.bindString(14, "");
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

    public static Groups getGroup(long groupId) {
        Groups group = new Groups();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from groups where Id = " + groupId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            group.setId(c.getLong(c.getColumnIndex("Id")));
            group.setName(c.getString(c.getColumnIndex("Name")));
            group.setSchool(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSchool"))));
            group.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            group.setSection(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSection"))));
            group.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            group.setClas(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsClass"))));
            group.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            group.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            group.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            group.setCreatedDate(c.getString(c.getColumnIndex("CreatedDate")));
            group.setActive(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsActive"))));
            group.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            group.setRecentMessage(c.getString(c.getColumnIndex("RecentMessage")));
            c.moveToNext();
        }
        c.close();
        return group;
    }

    public static Groups getRecentGroup(long classId) {
        Groups group = new Groups();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select Id from groups where ClassId = " + classId + " and IsSchool == 'false' order by Id asc", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            group.setId(c.getLong(c.getColumnIndex("Id")));
            c.moveToNext();
        }
        c.close();
        return group;
    }

    public static List<Groups> getGroups(long classId) {
        List<Groups> groups = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from groups where ClassId = " + classId + " and IsSchool == 'false' order by Id asc", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Groups group = new Groups();
            group.setId(c.getLong(c.getColumnIndex("Id")));
            group.setName(c.getString(c.getColumnIndex("Name")));
            group.setSchool(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSchool"))));
            group.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            group.setSection(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSection"))));
            group.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            group.setClas(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsClass"))));
            group.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            group.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            group.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            group.setCreatedDate(c.getString(c.getColumnIndex("CreatedDate")));
            group.setActive(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsActive"))));
            group.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            group.setRecentMessage(c.getString(c.getColumnIndex("RecentMessage")));
            groups.add(group);
            c.moveToNext();
        }
        c.close();
        return groups;
    }

    public static List<Groups> getSchoolGroups() {
        List<Groups> groups = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from groups where IsSchool = 'true' order by Id asc", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Groups group = new Groups();
            group.setId(c.getLong(c.getColumnIndex("Id")));
            group.setName(c.getString(c.getColumnIndex("Name")));
            group.setSchool(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSchool"))));
            group.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            group.setSection(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSection"))));
            group.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            group.setClas(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsClass"))));
            group.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            group.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            group.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            group.setCreatedDate(c.getString(c.getColumnIndex("CreatedDate")));
            group.setActive(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsActive"))));
            group.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            group.setRecentMessage(c.getString(c.getColumnIndex("RecentMessage")));
            groups.add(group);
            c.moveToNext();
        }
        c.close();
        return groups;
    }

    public static Groups getRecentSchoolGroup() {
        Groups group = new Groups();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select Id from groups where IsSchool = 'true' order by Id asc", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            group.setId(c.getLong(c.getColumnIndex("Id")));
            c.moveToNext();
        }
        c.close();
        return group;
    }

}

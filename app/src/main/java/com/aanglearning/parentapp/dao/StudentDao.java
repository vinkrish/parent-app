package com.aanglearning.parentapp.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aanglearning.parentapp.model.Student;
import com.aanglearning.parentapp.util.AppGlobal;

/**
 * Created by Vinay on 23-06-2017.
 */

public class StudentDao {
    public static int insert(Student student) {
        String sql = "insert into student(Id, Name, SchoolId, ClassId, SectionId, AdmissionNo, RollNo, " +
                "Username, Password, Image, FatherName, MotherName, DateOfBirth, Gender, Email, " +
                "Mobile1, Mobile2, Street, City, District, State, Pincode) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1, student.getId());
            stmt.bindString(2, student.getName());
            stmt.bindLong(3, student.getSchoolId());
            stmt.bindLong(4, student.getClassId());
            stmt.bindLong(5, student.getSectionId());
            stmt.bindString(6, student.getAdmissionNo());
            stmt.bindLong(7, student.getRollNo());
            stmt.bindString(8, student.getUsername());
            stmt.bindString(9, student.getPassword());
            stmt.bindString(10, student.getImage());
            stmt.bindString(11, student.getFatherName());
            stmt.bindString(12, student.getMotherName());
            stmt.bindString(13, student.getDateOfBirth());
            stmt.bindString(14, student.getGender());
            stmt.bindString(15, student.getEmail());
            stmt.bindString(16, student.getMobile1());
            stmt.bindString(17, student.getMobile2());
            stmt.bindString(18, student.getStreet());
            stmt.bindString(19, student.getCity());
            stmt.bindString(20, student.getDistrict());
            stmt.bindString(21, student.getState());
            stmt.bindString(22, student.getPincode());
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

    public static Student getStudent(long studentId) {
        Student student = new Student();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from student where Id = " + studentId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            student.setId(c.getLong(c.getColumnIndex("Id")));
            student.setName(c.getString(c.getColumnIndex("Name")));
            student.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            student.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            student.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            student.setAdmissionNo(c.getString(c.getColumnIndex("AdmissionNo")));
            student.setRollNo(c.getInt(c.getColumnIndex("RollNo")));
            student.setUsername(c.getString(c.getColumnIndex("Username")));
            student.setPassword(c.getString(c.getColumnIndex("Password")));
            student.setImage(c.getString(c.getColumnIndex("Image")));
            student.setFatherName(c.getString(c.getColumnIndex("FatherName")));
            student.setMotherName(c.getString(c.getColumnIndex("MotherName")));
            student.setDateOfBirth(c.getString(c.getColumnIndex("DateOfBirth")));
            student.setGender(c.getString(c.getColumnIndex("Gender")));
            student.setEmail(c.getString(c.getColumnIndex("Email")));
            student.setMobile1(c.getString(c.getColumnIndex("Mobile1")));
            student.setMobile2(c.getString(c.getColumnIndex("Mobile2")));
            student.setStreet(c.getString(c.getColumnIndex("Street")));
            student.setCity(c.getString(c.getColumnIndex("City")));
            student.setDistrict(c.getString(c.getColumnIndex("District")));
            student.setState(c.getString(c.getColumnIndex("State")));
            student.setPincode(c.getString(c.getColumnIndex("Pincode")));
            c.moveToNext();
        }
        c.close();
        return student;
    }

    public static int clear(long studentId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from student where Id = " + studentId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

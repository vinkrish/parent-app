package com.aanglearning.parentapp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class AlbumDao {

    public static int insert(List<Album> albums) {
        String sql = "insert into album(Id, Name, CoverPic, CreatedBy, CreatorName, CreatorRole, CreatedAt, SchoolId, ClassId, SectionId) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(Album album: albums) {
                stmt.bindLong(1, album.getId());
                stmt.bindString(2, album.getName());
                stmt.bindString(3, album.getCoverPic());
                stmt.bindLong(4, album.getCreatedBy());
                stmt.bindString(5, album.getCreatorName());
                stmt.bindString(6, album.getCreatorRole());
                stmt.bindLong(7, album.getCreatedAt());
                stmt.bindLong(8, album.getSchoolId());
                stmt.bindLong(9, album.getClassId());
                stmt.bindLong(10, album.getSectionId());
                stmt.executeInsert();
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

    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from album order by Id asc", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Album album = new Album();
            album.setId(c.getLong(c.getColumnIndex("Id")));
            album.setName(c.getString(c.getColumnIndex("Name")));
            album.setCoverPic(c.getString(c.getColumnIndex("CoverPic")));
            album.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            album.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            album.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            album.setCreatedAt(c.getLong(c.getColumnIndex("CreatedAt")));
            album.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            album.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            album.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            albums.add(album);
            c.moveToNext();
        }
        c.close();
        return albums;
    }

    public static int update(Album album) {
        String sql = "update album set Name = ?, CoverPic = ? where Id = ? ";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindString(1, album.getName());
            stmt.bindString(2, album.getCoverPic());
            stmt.bindLong(3, album.getId());
            stmt.executeUpdateDelete();
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

}

package com.aanglearning.parentapp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.MessageRecipient;
import com.aanglearning.parentapp.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 23-08-2017.
 */

public class MessageRecipientDao {

    public static int insertMany(List<MessageRecipient> mrList) {
        String sql = "insert into message_recipient(Id, RecipientId, RecipientName, Role, GroupId, MessageId, IsRead, ReadAt) " +
                "values(?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(MessageRecipient mr: mrList) {
                stmt.bindLong(1, mr.getId());
                stmt.bindLong(2, mr.getRecipientId());
                stmt.bindString(3, mr.getRecipientName());
                stmt.bindString(4, mr.getRole());
                stmt.bindLong(5, mr.getGroupId());
                stmt.bindLong(6, mr.getMessageId());
                stmt.bindString(7, Boolean.toString(mr.isRead()));
                stmt.bindLong(8, mr.getReadAt());
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

    public static List<MessageRecipient> getMessageRecipients(long recipientId, String recipientName, long groupId) {
        List<MessageRecipient> mrList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select Id from message where GroupId = " + groupId +
                " and Id not in (select MessageId from message_recipient where GroupId = " + groupId + ")", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            MessageRecipient mr = new MessageRecipient();
            mr.setRecipientId(recipientId);
            mr.setRecipientName(recipientName);
            mr.setRole("student");
            mr.setGroupId(groupId);
            mr.setMessageId(c.getLong(c.getColumnIndex("Id")));
            mr.setRead(true);
            mr.setReadAt(System.currentTimeMillis());
            mrList.add(mr);
            c.moveToNext();
        }
        c.close();
        return mrList;
    }
}

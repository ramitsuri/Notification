package com.ramitsuri.notification.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ramitsuri.notification.NewNotification;
import com.ramitsuri.notification.NotificationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310247189 on 8/5/2016.
 */
public class SQLHelper extends SQLiteOpenHelper {

    private static SQLHelper instance = null;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notification";
    private static final String TABLE_RULES = "rules";

    private static final String KEY_ID = "ruleID";
    private static final String KEY_PACKAGE_NAME = "packageName";
    private static final String KEY_FILTER_TEXT = "filterText";
    private static final String KEY_NOTIFICATION_TITLE = "notificationTitle";
    private static final String KEY_NOTIFICATION_TEXT = "notificationText";
    private static final String KEY_NOTIFICATION_ORIGINAL_APP = "notificationOriginalApp";
    private static final String KEY_ENABLED = "enabled";

    private static DataSetObservable dataSetObservable = new DataSetObservable();

    public static SQLHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLHelper(context.getApplicationContext());
        }
        return instance;
    }

    private SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_RULES_TABLE = "CREATE TABLE " + TABLE_RULES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_PACKAGE_NAME + " TEXT, " +
                KEY_FILTER_TEXT + " TEXT, " +
                KEY_NOTIFICATION_TITLE + " TEXT, " +
                KEY_NOTIFICATION_TEXT + " TEXT, " +
                KEY_NOTIFICATION_ORIGINAL_APP + " INTEGER, " +
                KEY_ENABLED + " INTEGER)";
        sqLiteDatabase.execSQL(CREATE_RULES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public NotificationRule getRule(int id){

        SQLiteDatabase db=getReadableDatabase();
        try {
            Cursor cursor= db.query(TABLE_RULES, new String[]{KEY_ID,
                            KEY_PACKAGE_NAME, KEY_FILTER_TEXT,
                            KEY_NOTIFICATION_TITLE, KEY_NOTIFICATION_TEXT,
                            KEY_NOTIFICATION_ORIGINAL_APP, KEY_ENABLED}, KEY_ID+"=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if(cursor!=null)
                cursor.moveToFirst();

            return cursorToNotificationRule(cursor);
        }
        catch (Exception e) {
            Log.e("notification", "Exception when getting notification rule", e);
            return new NotificationRule();
        }
    }

    public ArrayList<NotificationRule> getAllRules(){

        try {
            ArrayList<NotificationRule> listRules = new ArrayList<>();

            String selectQuery = "SELECT * FROM " + TABLE_RULES;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst())
            {
                do {
                    NotificationRule rule = cursorToNotificationRule(cursor);
                    listRules.add(rule);
                }while (cursor.moveToNext());
            }
            return listRules;
        }
        catch (Exception e) {

            return new ArrayList<>();
        }
    }

    public void createRule(NotificationRule rule){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_PACKAGE_NAME, rule.getPackageName());
            contentValues.put(KEY_FILTER_TEXT, rule.getFilterText());
            contentValues.put(KEY_NOTIFICATION_TITLE, rule.getNewNotification().getTitle());
            contentValues.put(KEY_NOTIFICATION_TEXT, rule.getNewNotification().getText());
            contentValues.put(KEY_NOTIFICATION_ORIGINAL_APP, rule.getNewNotification().getOpenOriginalApp());
            contentValues.put(KEY_ENABLED, rule.getIsEnabled());
            db.insert(TABLE_RULES, null, contentValues);
            dataSetObservable.notifyChanged();
        } catch (Exception e) {
            Log.e("notification", "Exception when adding notification rule", e);
        }
    }

    public void editRule(NotificationRule rule){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_PACKAGE_NAME, rule.getPackageName());
            contentValues.put(KEY_FILTER_TEXT, rule.getFilterText());
            contentValues.put(KEY_NOTIFICATION_TITLE, rule.getNewNotification().getTitle());
            contentValues.put(KEY_NOTIFICATION_TEXT, rule.getNewNotification().getText());
            contentValues.put(KEY_NOTIFICATION_ORIGINAL_APP, rule.getNewNotification().getOpenOriginalApp());
            contentValues.put(KEY_ENABLED, rule.getIsEnabled());
            dataSetObservable.notifyChanged();
            db.update(TABLE_RULES, contentValues, KEY_ID + "=?", new String[]{String.valueOf(rule.getId())});
        }
        catch (Exception e) {
            Log.e("notification", "Exception when updating rule", e);
        }
    }

    public void deleteRule(NotificationRule rule){
        try {
            SQLiteDatabase db= this.getWritableDatabase();
            db.delete(TABLE_RULES, KEY_ID + "=?", new String[]{String.valueOf(rule.getId())});
            db.close();
            dataSetObservable.notifyChanged();
        } catch (Exception e) {
            Log.e("notification", "Exception when deleting rule", e);
        }
    }

    public void deleteAllRules(){
        try {
            SQLiteDatabase db= this.getWritableDatabase();
            db.delete(TABLE_RULES, null, null);
            db.execSQL("vacuum");
            db.close();
            dataSetObservable.notifyChanged();
        } catch (SQLException e) {
            Log.e("notification", "Exception when deleting all rules", e);
        }
    }

    private NotificationRule cursorToNotificationRule(Cursor cursor) {

        NotificationRule rule = new NotificationRule();
        rule.setId(cursor.getInt(0));
        rule.setPackageName(cursor.getString(1));
        rule.setFilterText(cursor.getString(2));

        NewNotification notification = new NewNotification();
        notification.setTitle(cursor.getString(3));
        notification.setText(cursor.getString(4));
        notification.setOpenOriginalApp(getBooleanForInt(cursor.getInt(5)));

        rule.setNewNotification(notification);
        rule.setEnabled(getBooleanForInt(cursor.getInt(6)));
        return rule;
    }

    private boolean getBooleanForInt(int anInt) {
        return anInt == 0 ? false : true;
    }
}

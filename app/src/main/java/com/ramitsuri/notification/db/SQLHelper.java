package com.ramitsuri.notification.db;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                KEY_FILTER_TEXT + " INTEGER, " +
                KEY_NOTIFICATION_TITLE + " TEXT, " +
                KEY_NOTIFICATION_TEXT + " TEXT, " +
                KEY_NOTIFICATION_ORIGINAL_APP + " INTEGER, " +
                KEY_ENABLED + " INTEGER)";
        sqLiteDatabase.execSQL(CREATE_RULES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

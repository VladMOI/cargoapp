package ua.moyseienko.cargoapp.services.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDbInit extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "main.db";
    static final String TABLE_NAME = "user";
    static final String COLUMN_ID = "id";
    static final String COLUMN_FIRST_NAME = "firstname";
    static final String COLUMN_LAST_NAME = "lastname";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_EXPERIENCE = "experience";
    static final String COLUMN_BALANCE = "balance";
    static final String TABLE_ORDERS = "orders";
    static final String COLUMN_ORDER_ID = "id";

    public LocalDbInit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_EXPERIENCE + " INTEGER,"
                + COLUMN_BALANCE + " INTEGER)";
        String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + "INEGER PRIMARY KEY)";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_ORDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        // Create tables again
        onCreate(db);
    }
}

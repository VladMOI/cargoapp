package ua.moyseienko.cargoapp.services.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LocalInsertUser {
    public long insert(
            Context context,
            String firstname,
            String lastname,
            String email,
            String password,
            int balance,
            int experience
            )
    {
        LocalDbInit dbHelper = new LocalDbInit(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Создайте объект ContentValues для первой строки
        ContentValues values1 = new ContentValues();
        values1.put(dbHelper.COLUMN_FIRST_NAME, firstname);
        values1.put(dbHelper.COLUMN_LAST_NAME, lastname);
        values1.put(dbHelper.COLUMN_EMAIL, email);
        values1.put(dbHelper.COLUMN_PASSWORD, password);
        values1.put(dbHelper.COLUMN_EXPERIENCE, experience);
        values1.put(dbHelper.COLUMN_BALANCE, balance);

        // Вставьте первую строку в таблицу "user"
        long rowId1 = db.insert(dbHelper.TABLE_NAME, null, values1);

        db.close();
        return rowId1;
    }
}

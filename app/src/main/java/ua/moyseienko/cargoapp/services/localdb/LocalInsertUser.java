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

        // Создайте объект ContentValues для второй строки
//        ContentValues values2 = new ContentValues();
//        values2.put(dbHelper.COLUMN_FIRST_NAME, "Mary");
//        values2.put(dbHelper.COLUMN_LAST_NAME, "Smith");
//        values2.put(dbHelper.COLUMN_EMAIL, "marysmith@example.com");
//        values2.put(dbHelper.COLUMN_PASSWORD, "password2");
//        values2.put(dbHelper.COLUMN_EXPERIENCE, 3);
//        values2.put(dbHelper.COLUMN_BALANCE, 50.0);
//
//        // Вставьте вторую строку в таблицу "user"
//        long rowId2 = db.insert(dbHelper.TABLE_NAME, null, values2);

        // Закройте базу данных
        db.close();
        return rowId1;
    }
}

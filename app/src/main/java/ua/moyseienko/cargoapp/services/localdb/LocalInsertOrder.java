package ua.moyseienko.cargoapp.services.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LocalInsertOrder {
    public long insert(
            Context context,
            int id) {
        LocalDbInit dbHelper = new LocalDbInit(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Создайте объект ContentValues для первой строки
        ContentValues values1 = new ContentValues();
        values1.put(dbHelper.COLUMN_ORDER_ID, id);

        // Вставьте первую строку в таблицу "user"
        long rowId1 = db.insert(dbHelper.TABLE_ORDERS, null, values1);
        // Закройте базу данных
        db.close();
        return rowId1;
    }
}

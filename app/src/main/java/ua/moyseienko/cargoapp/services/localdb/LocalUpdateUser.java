package ua.moyseienko.cargoapp.services.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LocalUpdateUser {
    public long update(
            Context context,
            int balance
    )
    {
        LocalDbInit dbHelper = new LocalDbInit(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(dbHelper.COLUMN_BALANCE, balance);
        String[] args = {"0"};
        long rowId1 = db.update(dbHelper.TABLE_NAME, values1, "id = ?",args);

        db.close();
        return rowId1;
    }
}

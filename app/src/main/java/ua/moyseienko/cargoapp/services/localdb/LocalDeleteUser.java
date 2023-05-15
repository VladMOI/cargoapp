package ua.moyseienko.cargoapp.services.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LocalDeleteUser {
    public int deleteUser(Context context){
        LocalDbInit dbHelper = new LocalDbInit(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numRowsDeleted = db.delete(dbHelper.TABLE_NAME, null, null);
        db.close();
        return numRowsDeleted;
    }
}

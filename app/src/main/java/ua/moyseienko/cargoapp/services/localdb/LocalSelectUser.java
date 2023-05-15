package ua.moyseienko.cargoapp.services.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalSelectUser {
    public ArrayList<HashMap<String, String>> selectUser(Context context){
        LocalDbInit dbHelper = new LocalDbInit(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { dbHelper.COLUMN_ID,
                dbHelper.COLUMN_FIRST_NAME,
                dbHelper.COLUMN_LAST_NAME,
                dbHelper.COLUMN_EMAIL,
                dbHelper.COLUMN_PASSWORD,
                dbHelper.COLUMN_EXPERIENCE,
                dbHelper.COLUMN_BALANCE};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, projection, null, null, null, null, null);

        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

        while (cursor.moveToNext()) {
            HashMap<String, String> dataMap = new HashMap<>();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_ID));
            dataMap.put(dbHelper.COLUMN_ID, Integer.toString(id));

            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_FIRST_NAME));
            dataMap.put(dbHelper.COLUMN_FIRST_NAME, firstName);

            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_LAST_NAME));
            dataMap.put(dbHelper.COLUMN_LAST_NAME, lastName);

            String email = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_EMAIL));
            dataMap.put(dbHelper.COLUMN_EMAIL, email);

            String password = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_PASSWORD));
            dataMap.put(dbHelper.COLUMN_PASSWORD, password);

            int experience = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_EXPERIENCE));
            dataMap.put(dbHelper.COLUMN_EXPERIENCE, Integer.toString(experience));

            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_BALANCE));
            dataMap.put(dbHelper.COLUMN_BALANCE, Double.toString(balance));

            dataList.add(dataMap);

            dataList.add(dataMap);
        }

        cursor.close();
        db.close();

        // Пройдитесь по списку и выведите значения в консоль
        for (HashMap<String, String> dataMap : dataList) {
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        return dataList;
    }
}

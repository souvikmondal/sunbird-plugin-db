package org.sunbird.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class SQLiteOperator {

    private static final String LOG_TAG = SQLiteOperator.class.getSimpleName();
    private SQLiteDatabase database;
    private String dbName;
    private int dbVersion;

    SQLiteOperator(SQLiteDatabase database, String dbName, int dbVersion) {
        this.database = database;
        this.dbName = dbName;
        this.dbVersion = dbVersion;
    }

    public String getDBName() {
        return this.dbName;
    }

    public int getDBVersion() {
        return dbVersion;
    }

    public JSONArray execute(String query) throws JSONException {
        Cursor cursor = database.rawQuery(query, null);
        JSONArray jsonArray = new JSONArray();

        if (cursor != null && cursor.moveToFirst()) {
            int size = cursor.getCount();
            for (int index = 0; index < size; index++) {
                JSONObject jsonObject = new JSONObject();
                switch (cursor.getType(index)) {
                    case Cursor.FIELD_TYPE_STRING:
                        jsonObject.put(cursor.getColumnName(index), cursor.getString(index));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        jsonObject.put(cursor.getColumnName(index), cursor.getFloat(index));
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        jsonObject.put(cursor.getColumnName(index), cursor.getInt(index));
                        break;
                }
                jsonArray.put(jsonObject);
            }
        }

        return jsonArray;

    }


    public void beginTransaction() {
        database.beginTransaction();
    }

    public void endTransaction(boolean isOperationSuccessful) {
        if (isOperationSuccessful) {
            database.setTransactionSuccessful();
        }
        database.endTransaction();
    }

    public JSONArray read(boolean distinct,
                          String table,
                          String[] columns,
                          String selection,
                          String[] selectionArgs,
                          String groupBy,
                          String having,
                          String orderBy,
                          String limit) throws JSONException {

        Cursor cursor = database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        JSONArray jsonArray = new JSONArray();

        if (cursor != null && cursor.moveToFirst()) {
            int size = cursor.getCount();
            for (int index = 0; index < size; index++) {
                JSONObject jsonObject = new JSONObject();
                switch (cursor.getType(index)) {
                    case Cursor.FIELD_TYPE_STRING:
                        jsonObject.put(cursor.getColumnName(index), cursor.getString(index));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        jsonObject.put(cursor.getColumnName(index), cursor.getFloat(index));
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        jsonObject.put(cursor.getColumnName(index), cursor.getInt(index));
                        break;
                }
                jsonArray.put(jsonObject);
            }
        }

        return jsonArray;
    }

    public long insert(String table, JSONObject jsonObject) throws JSONException {

        ContentValues values = new ContentValues();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof Integer) {
                values.put(key, (Integer) value);
            } else if (value instanceof Long) {
                values.put(key, (Long) value);
            } else if (value instanceof String) {
                values.put(key, (String) value);
            } else if (value instanceof Float) {
                values.put(key, (Float) value);
            } else if (value instanceof Double) {
                values.put(key, (Double) value);
            }

        }

        return database.insert(table, null, values);
    }

}

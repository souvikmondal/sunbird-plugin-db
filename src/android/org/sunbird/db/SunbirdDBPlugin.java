package org.sunbird.db;

import android.support.annotation.NonNull;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SunbirdDBPlugin extends CordovaPlugin {


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        switch (action) {
            case "init":
                doInit(args, callbackContext);
                break;
            case "read":
                doRead(args, callbackContext);
                break;
            case "insert":
                doInsert(args, callbackContext);
                break;
            case "execute":
                doExecute(args, callbackContext);
                break;
            case "beginTransaction":
                doBeginTransaction();
                break;
            case "endTransaction":
                doEndTransaction(args);
                break;
        }

        return true;
    }

    private void doEndTransaction(JSONArray args) throws JSONException {
        boolean isOperationSuccessful = args.getBoolean(0);
        SunbirdDBHelper.getInstance().operator().endTransaction(isOperationSuccessful);
    }

    private void doBeginTransaction() {
        SunbirdDBHelper.getInstance().operator().beginTransaction();
    }

    private void doExecute(JSONArray args, CallbackContext callbackContext) {
        try {
            JSONArray resultArray = SunbirdDBHelper.getInstance().operator().execute(args.getString(0));
            callbackContext.success(resultArray);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void doInsert(JSONArray args, CallbackContext callbackContext) {
        try {
            long number = SunbirdDBHelper.getInstance().operator().insert(args.getString(0), args.getJSONObject(1));
            callbackContext.success((int) number);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void doRead(JSONArray args, CallbackContext callbackContext) {
        try {
            JSONArray resultArray = SunbirdDBHelper.getInstance().operator().read(
                    args.getBoolean(0),
                    args.getString(1),
                    toStringArray(args.getJSONArray(2)),
                    args.getString(3),
                    toStringArray(args.getJSONArray(4)),
                    args.getString(5),
                    args.getString(6),
                    args.getString(7),
                    args.getString(8)
            );
            callbackContext.success(resultArray);
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void doInit(JSONArray args, CallbackContext callbackContext) throws JSONException {
        List<Migration> migrationList = prepareMigrations(args);

        SunbirdDBContext sunbirdDBContext = new SunbirdDBContext();
        sunbirdDBContext.setContext(cordova.getContext())
                .setDbName(args.getString(0))
                .setDbVersion(args.getInt(1))
                .setMigrationList(migrationList);


        SunbirdDBHelper.init(sunbirdDBContext, callbackContext);
    }

    private List<Migration> prepareMigrations(JSONArray args) throws JSONException {
        List<Migration> migrationList = new ArrayList<>();
        JSONArray migrationArray = args.getJSONArray(2);
        int size = migrationArray.length();
        for (int i = 0; i < size; i++) {
            migrationList.add(createMigration(migrationArray.getJSONObject(i)));
        }
        return migrationList;
    }

    private Migration createMigration(JSONObject migrationObject) throws JSONException {
        List<String> queryList = new ArrayList<>();
        JSONArray queryArray = migrationObject.getJSONArray("queryList");
        int querySize = queryArray.length();
        for (int j = 0; j < querySize; j++) {
            queryList.add(queryArray.getString(j));
        }

        Migration migration = new Migration();
        migration.setQueryList(queryList)
                .setTargetDbVersion(migrationObject.getInt("targetDbVersion"));
        return migration;
    }

    private String[] toStringArray(JSONArray array) throws JSONException {
        int length = array.length();
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            values[i] = array.getString(i);
        }
        return values;
    }
}

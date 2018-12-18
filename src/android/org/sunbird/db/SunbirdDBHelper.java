package org.sunbird.db;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class SunbirdDBHelper extends SQLiteOpenHelper {

    private static SunbirdDBHelper instance;

    public static void init(SunbirdDBContext sunbirdDBContext, CallbackContext callbackContext) {
        if (instance == null)
            instance = new SunbirdDBHelper(sunbirdDBContext, callbackContext);
    }

    public static SunbirdDBHelper getInstance() {
        return instance;
    }

    private SunbirdDBContext sunbirdDBContext;
    private SQLiteOperator sqLiteOperator;
    private Subject<JSONObject> dbLifecycleSubect;
    private List<Migration> migrationList;


    private SunbirdDBHelper(SunbirdDBContext sunbirdDBContext, CallbackContext callbackContext) {
        super(sunbirdDBContext.getContext(), sunbirdDBContext.getDbName(),
                null, sunbirdDBContext.getDbVersion());
        this.sunbirdDBContext = sunbirdDBContext;
        this.migrationList = sunbirdDBContext.getMigrationList();
        createEventHandler(callbackContext);
    }

    @SuppressLint("CheckResult")
    private void createEventHandler(CallbackContext callbackContext) {
        this.dbLifecycleSubect = PublishSubject.create();
        this.dbLifecycleSubect.subscribe(jsonObject -> {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonObject);
            pluginResult.setKeepCallback(true);

            callbackContext.sendPluginResult(pluginResult);
        });
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (Migration migration:migrationList) {
            for (String query:migration.getQueryList()) {
                sqLiteDatabase.execSQL(query);
            }
        }
        dbLifecycleSubect.toSerialized().onNext(createJsonForOncreate());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        for (Migration migration:migrationList) {
            if (migration.isRequired(oldVersion, newVersion)) {
                for (String query:migration.getQueryList()) {
                    sqLiteDatabase.execSQL(query);
                }
            }
        }
        dbLifecycleSubect.toSerialized().onNext(createJsonForOnupgrade(oldVersion, newVersion));
    }

    private JSONObject createJsonForOncreate() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "onCreate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForOnupgrade(int oldVersion, int newVersion) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "onUpgrade");
            jsonObject.put("oldVersion", oldVersion);
            jsonObject.put("newVersion", newVersion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public SQLiteOperator operator() {
        if (sqLiteOperator == null) {
            SQLiteDatabase database = getWritableDatabase();
            sqLiteOperator = new SQLiteOperator(database, sunbirdDBContext.getDbName(), sunbirdDBContext.getDbVersion());
        }
        return sqLiteOperator;
    }

}
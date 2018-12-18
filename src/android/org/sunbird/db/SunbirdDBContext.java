package org.sunbird.db;

import android.content.Context;

import java.util.List;

public class SunbirdDBContext {

    private String dbName;
    private int dbVersion;
    private Context context;
    private List<Migration> migrationList;

    public String getDbName() {
        return dbName;
    }

    public SunbirdDBContext setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public SunbirdDBContext setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public SunbirdDBContext setContext(Context context) {
        this.context = context;
        return this;
    }

    public List<Migration> getMigrationList() {
        return migrationList;
    }

    public SunbirdDBContext setMigrationList(List<Migration> migrationList) {
        this.migrationList = migrationList;
        return this;
    }
}

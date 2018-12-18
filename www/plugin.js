var exec = require('cordova/exec');

var PLUGIN_NAME = 'DB';

var db = {

    init(dbName, dbVersion, migrations, callback) {
        exec(callback, null, PLUGIN_NAME, "init", [dbName, dbVersion, migrations]);
    },

    read(distinct,
         table,
         columns,
         selection,
         selectionArgs,
         groupBy,
         having,
         orderBy,
         limit,
         success,
         error) {

        exec(success, error, PLUGIN_NAME, "read", [distinct,
            table,
            columns,
            selection,
            selectionArgs,
            groupBy,
            having,
            orderBy,
            limit]);
    },

    execute(query, success, error) {
        exec(success, error, PLUGIN_NAME, "execute", [query]);
    },

    insert(table, model, success, error) {
        exec(success, error, PLUGIN_NAME, "insert", [table, model]);
    },

    beginTransaction() {
        exec(null, null, PLUGIN_NAME, "beginTransaction", []);
    },

    endTransaction(isOperationSuccessful) {
        exec(null, null, PLUGIN_NAME, "endTransaction", [isOperationSuccessful]);
    }

};


module.exports = db;

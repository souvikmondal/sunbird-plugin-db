
var sqlite3 = require('sqlite3').verbose();
var db = undefined;

var browserInterface = {

    init: function (success, failure, args) {
        var dbName = args[0];
        var dbVersion = args[1];
        var migrationArray = args[2];

        db = new sqlite3.Database(dbName + ".db");

        var queries = migrationArray["queryList"];

        db.serialize(function() {
            queries.forEach(q => {
                db.run(q);
            });
        });

        var onCreateObject = {
          "method": "onCreate"
        };

        success(onCreateObject);

    },


    query: function (success, failure, opts) {
    },


    insert: function (success, failure, opts) {
        var tableName = opts[0];
        var model = args[1];

        var values = " VALUES(";
        var vArray = [];

        for (var k in model) {
            key = key + k + ",";
            var v = model[k];

            values = values + "?";
            vArray.push(v);
        }

        values = values.substr(0, values.length - 2);
        values = values + ")";

        var sqlstmt = "INSERT INTO "+ tableName + values;

        db.serialize(function() {

            db.run(sqlstmt, vArray);

        });
    }
};

module.exports = browserInterface;
require('cordova/exec/proxy').add('DB', module.exports);
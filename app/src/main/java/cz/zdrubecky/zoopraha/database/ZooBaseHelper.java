package cz.zdrubecky.zoopraha.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cz.zdrubecky.zoopraha.database.ZooDBSchema.*;


public class ZooBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "zooBase.db";

    public ZooBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Called whenever there's no DATABASE_NAME file among the databases
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AdoptionsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AdoptionsTable.Cols.ID + ", " +
                AdoptionsTable.Cols.LEXICON_ID + ", " +
                AdoptionsTable.Cols.OPENDATA_ID + ", " +
                AdoptionsTable.Cols.NAME + ", " +
                AdoptionsTable.Cols.PRICE + ", " +
                AdoptionsTable.Cols.VISIT +
                ")"
        );
    }

    // Called when there's a lower version present
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

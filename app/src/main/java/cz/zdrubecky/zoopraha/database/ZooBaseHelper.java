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

        db.execSQL("CREATE TABLE " + AnimalsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AnimalsTable.Cols.ID + ", " +
                AnimalsTable.Cols.OPENDATA_ID + ", " +
                AnimalsTable.Cols.NAME + ", " +
                AnimalsTable.Cols.LATIN_NAME + ", " +
                AnimalsTable.Cols.CLASS_NAME + ", " +
                AnimalsTable.Cols.CLASS_LATIN_NAME + ", " +
                AnimalsTable.Cols.ORDER_NAME + ", " +
                AnimalsTable.Cols.ORDER_LATIN_NAME + ", " +
                AnimalsTable.Cols.DESCRIPTION + ", " +
                AnimalsTable.Cols.IMAGE + ", " +
                AnimalsTable.Cols.CONTINENTS + ", " +
                AnimalsTable.Cols.DISTRIBUTION + ", " +
                AnimalsTable.Cols.BIOTOPE + ", " +
                AnimalsTable.Cols.BIOTOPES_DETAIL + ", " +
                AnimalsTable.Cols.FOOD + ", " +
                AnimalsTable.Cols.FOOD_DETAIL + ", " +
                AnimalsTable.Cols.PROPORTIONS + ", " +
                AnimalsTable.Cols.REPRODUCTION + ", " +
                AnimalsTable.Cols.ATTRACTIONS + ", " +
                AnimalsTable.Cols.PROJECTS + ", " +
                AnimalsTable.Cols.BREEDING + ", " +
                AnimalsTable.Cols.LOCATION + ", " +
                AnimalsTable.Cols.LOCATION_URL +
                ")"
        );

        db.execSQL("CREATE TABLE " + EventsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventsTable.Cols.ID + ", " +
                EventsTable.Cols.START + ", " +
                EventsTable.Cols.END + ", " +
                EventsTable.Cols.DURATION + ", " +
                EventsTable.Cols.DESCRIPTION + ", " +
                EventsTable.Cols.NAME +
                ")"
        );
    }

    // Called when there's a lower version present
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

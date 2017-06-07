package cz.zdrubecky.zoopraha.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cz.zdrubecky.zoopraha.database.ZooDBSchema.*;


public class ZooBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "zooBase.db";
    private static ZooBaseHelper sInstance = null;

    public static ZooBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            // Create a new instance using the app context rather than being locked to a specific activity
            sInstance = new ZooBaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private ZooBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Called whenever there's no DATABASE_NAME file among the databases
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AdoptionsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AdoptionsTable.Cols.ID + " NOT NULL, " +
                AdoptionsTable.Cols.LEXICON_ID + ", " +
                AdoptionsTable.Cols.NAME + ", " +
                AdoptionsTable.Cols.PRICE + ", " +
                AdoptionsTable.Cols.VISIT +
                ")"
        );

        db.execSQL("CREATE UNIQUE INDEX idx_adoptions_id ON " + AdoptionsTable.NAME + " (" + AdoptionsTable.Cols.ID + ")");

        db.execSQL("CREATE TABLE " + AnimalsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AnimalsTable.Cols.ID + " NOT NULL, " +
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

        db.execSQL("CREATE UNIQUE INDEX idx_animals_id ON " + AnimalsTable.NAME + " (" + AnimalsTable.Cols.ID + ")");

        db.execSQL("CREATE TABLE " + ClassificationsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ClassificationsTable.Cols.ID + " NOT NULL, " +
                ClassificationsTable.Cols.OPENDATA_ID + ", " +
                ClassificationsTable.Cols.TYPE + ", " +
                ClassificationsTable.Cols.PARENT_ID + ", " +
                ClassificationsTable.Cols.NAME + ", " +
                ClassificationsTable.Cols.LATIN_NAME + ", " +
                ClassificationsTable.Cols.SLUG +
                ")"
        );

        db.execSQL("CREATE UNIQUE INDEX idx_classifications_id ON " + ClassificationsTable.NAME + " (" + ClassificationsTable.Cols.ID + ")");

        db.execSQL("CREATE TABLE " + EventsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventsTable.Cols.ID + " NOT NULL, " +
                EventsTable.Cols.START + ", " +
                EventsTable.Cols.END + ", " +
                EventsTable.Cols.DURATION + ", " +
                EventsTable.Cols.DESCRIPTION + ", " +
                EventsTable.Cols.NAME +
                ")"
        );

        db.execSQL("CREATE UNIQUE INDEX idx_events_id ON " + EventsTable.NAME + " (" + EventsTable.Cols.ID + ")");

        db.execSQL("CREATE TABLE " + FiltersTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                FiltersTable.Cols.NAME + " NOT NULL, " +
                FiltersTable.Cols.VALUE + " NOT NULL, " +
                FiltersTable.Cols.COUNT +
                ")"
        );

        db.execSQL("CREATE UNIQUE INDEX idx_filters_name_value ON " + FiltersTable.NAME + " (" + FiltersTable.Cols.NAME + ", " + FiltersTable.Cols.VALUE + ")");
    }

    // Called when there's a lower version present
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.LocationsTable;
import cz.zdrubecky.zoopraha.model.Location;

public class LocationManager {
    private SQLiteDatabase mDatabase;
    private List<Location> mLocations;

    public LocationManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
        mLocations = new ArrayList<>();
    }

    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();

        ZooCursorWrapper cursor = queryLocations(null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                locations.add(cursor.getLocation());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return locations;
    }

    public Location getLocation(String id) {
        ZooCursorWrapper cursor = queryLocations(
                LocationsTable.Cols.ID + " = ?",
                new String[] { id }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLocation();
        } finally {
            cursor.close();
        }
    }

    public void addLocation(Location location) {
        mLocations.add(location);
    }

    public void updateLocation(Location location) {
        ContentValues values = getContentValues(location);

        mDatabase.update(LocationsTable.NAME, values, LocationsTable.Cols.ID + " = ?", new String[] {location.getId()});
    }

    public boolean removeLocation(Location location) {
        return mDatabase.delete(LocationsTable.NAME, LocationsTable.Cols.ID + " = ?", new String[] {location.getId()}) > 0;
    }

    // Bind one object to the statement
    private void bindLocation(SQLiteStatement statement, Location location) {
        // The binding index starts at "1"
        statement.bindString(1, location.getId());
        statement.bindString(2, location.getDescription());
        statement.bindString(3, Integer.toString(location.getOrdering()));
        statement.bindString(4, location.getUrl());
        statement.bindString(5, location.getGpsX());
        statement.bindString(6, location.getGpsY());
        statement.bindString(7, location.getName());
        statement.bindString(8, location.getSlug());

        statement.execute();
        statement.clearBindings();
    }

    // Flush all the locations into the database at once
    public void flushLocations() {
        // Prepare the query for late binding
        String query = "INSERT OR REPLACE INTO " + LocationsTable.NAME + " ( " +
                LocationsTable.Cols.ID + ", " +
                LocationsTable.Cols.DESCRIPTION + ", " +
                LocationsTable.Cols.ORDERING + ", " +
                LocationsTable.Cols.URL + ", " +
                LocationsTable.Cols.GPS_X + ", " +
                LocationsTable.Cols.GPS_Y + ", " +
                LocationsTable.Cols.NAME + ", " +
                LocationsTable.Cols.SLUG + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";

        // Lock the DB file for the time being
        mDatabase.beginTransactionNonExclusive();

        SQLiteStatement statement = mDatabase.compileStatement(query);

        // Bind every location from the list
        for (Location location : mLocations) {
            bindLocation(statement, location);
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + LocationsTable.NAME);
    }

    // Mapping of the Location to the columns
    private static ContentValues getContentValues(Location location) {
        ContentValues values = new ContentValues();

        values.put(LocationsTable.Cols.ID, location.getId());
        values.put(LocationsTable.Cols.DESCRIPTION, location.getDescription());
        values.put(LocationsTable.Cols.ORDERING, location.getOrdering());
        values.put(LocationsTable.Cols.URL, location.getUrl());
        values.put(LocationsTable.Cols.GPS_X, location.getGpsX());
        values.put(LocationsTable.Cols.GPS_Y, location.getGpsY());
        values.put(LocationsTable.Cols.NAME, location.getName());
        values.put(LocationsTable.Cols.SLUG, location.getSlug());

        return values;
    }

    private ZooCursorWrapper queryLocations(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(LocationsTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new ZooCursorWrapper(cursor);
    }
}

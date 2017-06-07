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
import cz.zdrubecky.zoopraha.database.ZooDBSchema.FiltersTable;
import cz.zdrubecky.zoopraha.model.Filter;

public class FilterManager {
    private SQLiteDatabase mDatabase;
    private List<Filter> mFilters;

    public FilterManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
        mFilters = new ArrayList<>();
    }

    public List<Filter> getFilters(String name) {
        List<Filter> filters = new ArrayList<>();

        ZooCursorWrapper cursor = queryFilters(
                FiltersTable.Cols.NAME + " = ?",
                new String[] { name }
        );

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                filters.add(cursor.getFilter());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return filters;
    }

    public void addFilter(Filter filter) {
        mFilters.add(filter);
    }

    public void updateFilter(Filter filter) {
        ContentValues values = getContentValues(filter);

        mDatabase.update(FiltersTable.NAME, values, FiltersTable.Cols.NAME + " = ?", new String[] {filter.getName()});
    }

    public boolean removeFilter(Filter filter) {
        return mDatabase.delete(FiltersTable.NAME, FiltersTable.Cols.NAME + " = ?", new String[] {filter.getName()}) > 0;
    }
    
    // Bind one object to the statement
    private void bindFilter(SQLiteStatement statement, Filter filter) {
        // The binding index starts at "1"
        statement.bindString(1, filter.getName());
        statement.bindString(2, filter.getValue());
        statement.bindString(3, Integer.toString(filter.getCount()));

        statement.execute();
        statement.clearBindings();
    }

    // Flush all the filters into the database at once
    public void flushFilters() {
        // Prepare the query for late binding
        String query = "INSERT OR REPLACE INTO " + FiltersTable.NAME + " ( " +
                FiltersTable.Cols.NAME + ", " +
                FiltersTable.Cols.VALUE + ", " +
                FiltersTable.Cols.COUNT + " ) VALUES ( ?, ?, ? )";

        // Lock the DB file for the time being
        mDatabase.beginTransactionNonExclusive();

        SQLiteStatement statement = mDatabase.compileStatement(query);

        // Bind every filter from the list
        for (Filter filter : mFilters) {
            bindFilter(statement, filter);
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + FiltersTable.NAME);
    }

    // Mapping of the Filter to the columns
    private static ContentValues getContentValues(Filter filter) {
        ContentValues values = new ContentValues();

        values.put(FiltersTable.Cols.NAME, filter.getName());
        values.put(FiltersTable.Cols.VALUE, filter.getValue());
        values.put(FiltersTable.Cols.COUNT, filter.getCount());

        return values;
    }

    private ZooCursorWrapper queryFilters(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(FiltersTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new ZooCursorWrapper(cursor);
    }
}

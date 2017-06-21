package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.EventsTable;
import cz.zdrubecky.zoopraha.model.Event;

public class EventManager {
    private SQLiteDatabase mDatabase;
    private List<Event> mEvents;

    public EventManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
        mEvents = new ArrayList<>();
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        // The events are restricted to those in the future by default
        ZooCursorWrapper cursor = queryEvents(
                EventsTable.Cols.START + " >= ?",
                new String[] { new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("cs")).format(new Date()) }
        );

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                events.add(cursor.getEvent());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return events;
    }

    public Event getEvent(String id) {
        ZooCursorWrapper cursor = queryEvents(
                ZooDBSchema.EventsTable.Cols.ID + " = ?",
                new String[] { id }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getEvent();
        } finally {
            cursor.close();
        }
    }

    public void addEvent(Event event) {
        mEvents.add(event);
    }

    public void updateEvent(Event event) {
        mEvents.add(event);
    }

    public boolean removeEvent(Event event) {
        return mDatabase.delete(EventsTable.NAME, EventsTable.Cols.ID + " = ?", new String[] {event.getId()}) > 0;
    }

    // Bind one object to the statement
    private void bindEvent(SQLiteStatement statement, Event event) {
        // The binding index starts at "1"
        statement.bindString(1, event.getId());
        statement.bindString(2, event.getStart());
        statement.bindString(3, event.getEnd());
        statement.bindString(4, Integer.toString(event.getDuration()));
        statement.bindString(5, event.getName());
        statement.bindString(6, event.getDescription());

        statement.execute();
        statement.clearBindings();
    }

    // Flush all the events into the database at once via a transaction
    public void flushEvents() {
        // Prepare the query for late binding
        String query = "INSERT OR REPLACE INTO " + EventsTable.NAME + " ( " +
                EventsTable.Cols.ID + ", " +
                EventsTable.Cols.START + ", " +
                EventsTable.Cols.END + ", " +
                EventsTable.Cols.DURATION + ", " +
                EventsTable.Cols.NAME + ", " +
                EventsTable.Cols.DESCRIPTION + " ) VALUES ( ?, ?, ?, ?, ?, ? )";

        // Lock the DB file for the time being
        mDatabase.beginTransactionNonExclusive();

        SQLiteStatement statement = mDatabase.compileStatement(query);

        // Bind every event from the list
        for (Event event : mEvents) {
            bindEvent(statement, event);
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + EventsTable.NAME);
    }

    // Mapping of the Event to the columns
    private static ContentValues getContentValues(Event event) {
        ContentValues values = new ContentValues();

        values.put(EventsTable.Cols.ID, event.getId());
        values.put(EventsTable.Cols.START, event.getStart());
        values.put(EventsTable.Cols.END, event.getEnd());
        values.put(EventsTable.Cols.DURATION, event.getDuration());
        values.put(EventsTable.Cols.DESCRIPTION, event.getDescription());
        values.put(EventsTable.Cols.NAME, event.getName());

        return values;
    }

    private ZooCursorWrapper queryEvents(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(EventsTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new ZooCursorWrapper(cursor);
    }
}

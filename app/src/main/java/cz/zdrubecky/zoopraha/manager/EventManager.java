package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.EventsTable;
import cz.zdrubecky.zoopraha.model.Event;

public class EventManager {
    private SQLiteDatabase mDatabase;

    public EventManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        ZooCursorWrapper cursor = queryEvents(null, null);

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
        // todo check if it exists and then update
        ContentValues values = getContentValues(event);

        // The second argument - nullColumnHack - is used to force insert in the case of empty values
        mDatabase.insert(EventsTable.NAME, null, values);
    }

    public void updateEvent(Event event) {
        ContentValues values = getContentValues(event);

        mDatabase.update(EventsTable.NAME, values, EventsTable.Cols.ID + " = ?", new String[] {event.getId()});
    }

    public boolean removeEvent(Event event) {
        return mDatabase.delete(EventsTable.NAME, EventsTable.Cols.ID + " = ?", new String[] {event.getId()}) > 0;
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

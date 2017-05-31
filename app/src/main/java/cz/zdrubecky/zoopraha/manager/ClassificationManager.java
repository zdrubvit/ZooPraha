package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.ClassificationsTable;
import cz.zdrubecky.zoopraha.model.Classification;

public class ClassificationManager {
    private SQLiteDatabase mDatabase;

    public ClassificationManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
    }

    public List<Classification> getClassifications() {
        List<Classification> classifications = new ArrayList<>();

        ZooCursorWrapper cursor = queryClassifications(null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                classifications.add(cursor.getClassification());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return classifications;
    }

    public Classification getClassification(String id) {
        ZooCursorWrapper cursor = queryClassifications(
                ClassificationsTable.Cols.ID + " = ?",
                new String[] { id }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getClassification();
        } finally {
            cursor.close();
        }
    }

    public void addClassification(Classification classification) {
        // todo check if it exists and then update
        ContentValues values = getContentValues(classification);

        // The second argument - nullColumnHack - is used to force insert in the case of empty values
        mDatabase.insert(ClassificationsTable.NAME, null, values);
    }

    public void updateClassification(Classification classification) {
        ContentValues values = getContentValues(classification);

        mDatabase.update(ClassificationsTable.NAME, values, ClassificationsTable.Cols.ID + " = ?", new String[] {classification.getId()});
    }

    public boolean removeClassification(Classification classification) {
        return mDatabase.delete(ClassificationsTable.NAME, ClassificationsTable.Cols.ID + " = ?", new String[] {classification.getId()}) > 0;
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + ClassificationsTable.NAME);
    }

    private static ContentValues getContentValues(Classification classification) {
        ContentValues values = new ContentValues();

        values.put(ClassificationsTable.Cols.ID, classification.getId());
        values.put(ClassificationsTable.Cols.OPENDATA_ID, classification.getOpendataId());
        values.put(ClassificationsTable.Cols.TYPE, classification.getType());
        values.put(ClassificationsTable.Cols.PARENT_ID, classification.getParentId());
        values.put(ClassificationsTable.Cols.NAME, classification.getName());
        values.put(ClassificationsTable.Cols.LATIN_NAME, classification.getLatinName());
        values.put(ClassificationsTable.Cols.SLUG, classification.getSlug());

        return values;
    }

    private ZooCursorWrapper queryClassifications(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ClassificationsTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new ZooCursorWrapper(cursor);
    }
}

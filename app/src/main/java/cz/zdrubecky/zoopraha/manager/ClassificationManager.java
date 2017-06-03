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
import cz.zdrubecky.zoopraha.database.ZooDBSchema.ClassificationsTable;
import cz.zdrubecky.zoopraha.model.Classification;

public class ClassificationManager {
    private SQLiteDatabase mDatabase;
    private List<Classification> mClassifications;

    public ClassificationManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
        mClassifications = new ArrayList<>();
    }

    // Return the whole taxonomy, e.g. classes with their child orders
    public List<Classification> getTaxonomy() {
        List<Classification> classes = getClassifications(
                ClassificationsTable.Cols.TYPE + " = ?",
                new String[] { "class" }
        );

        // Get the orders for each class
        for (Classification parentClass : classes) {
            List<Classification> orders = getClassifications(
                    ClassificationsTable.Cols.TYPE + " = ? AND " + ClassificationsTable.Cols.PARENT_ID + " = ?",
                    new String[] { "order", Integer.toString(parentClass.getOpendataId()) }
            );

            parentClass.setOrders(orders);
        }

        return classes;
    }

    public List<Classification> getClassifications(String whereClause, String[] whereArgs) {
        List<Classification> classifications = new ArrayList<>();

        ZooCursorWrapper cursor = queryClassifications(whereClause, whereArgs);

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

            Classification classification = cursor.getClassification();

            // If we're dealing with a class, get its child orders
            if (classification.getType() == "class") {
                List<Classification> orders = getClassifications(
                        ClassificationsTable.Cols.TYPE + " = ? AND " + ClassificationsTable.Cols.PARENT_ID + " = ?",
                        new String[] { "order", Integer.toString(classification.getOpendataId()) }
                );

                classification.setOrders(orders);
            }

            return classification;
        } finally {
            cursor.close();
        }
    }

    public void addClassification(Classification classification) {
        mClassifications.add(classification);
    }

    public void updateClassification(Classification classification) {
        ContentValues values = getContentValues(classification);

        mDatabase.update(ClassificationsTable.NAME, values, ClassificationsTable.Cols.ID + " = ?", new String[] {classification.getId()});
    }

    public boolean removeClassification(Classification classification) {
        return mDatabase.delete(ClassificationsTable.NAME, ClassificationsTable.Cols.ID + " = ?", new String[] {classification.getId()}) > 0;
    }

    // Bind one object to the statement
    private void bindClassification(SQLiteStatement statement, Classification classification) {
        // The binding index starts at "1"
        statement.bindString(1, classification.getId());
        statement.bindString(2, Integer.toString(classification.getOpendataId()));
        statement.bindString(3, classification.getType());
        statement.bindString(4, Integer.toString(classification.getParentId()));
        statement.bindString(5, classification.getName());
        statement.bindString(6, classification.getLatinName());
        statement.bindString(7, classification.getSlug());

        statement.execute();
        statement.clearBindings();
    }

    // Flush all the classifications into the database at once
    public void flushClassifications() {
        // Prepare the query for late binding
        String query = "INSERT OR REPLACE INTO " + ClassificationsTable.NAME + " ( " +
                ClassificationsTable.Cols.ID + ", " +
                ClassificationsTable.Cols.OPENDATA_ID + ", " +
                ClassificationsTable.Cols.TYPE + ", " +
                ClassificationsTable.Cols.PARENT_ID + ", " +
                ClassificationsTable.Cols.NAME + ", " +
                ClassificationsTable.Cols.LATIN_NAME + ", " +
                ClassificationsTable.Cols.SLUG + " ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";

        // Lock the DB file for the time being
        mDatabase.beginTransactionNonExclusive();

        SQLiteStatement statement = mDatabase.compileStatement(query);

        // Bind every class and order from the list
        for (Classification classification : mClassifications) {
            bindClassification(statement, classification);

            List<Classification> orders = classification.getOrders();
            for (Classification order : orders) {
                bindClassification(statement, order);
            }
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
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

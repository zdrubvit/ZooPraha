package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.AdoptionsTable;
import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.Adoption;

public class AdoptionManager {
    private SQLiteDatabase mDatabase;
    private List<Adoption> mAdoptions;

    public AdoptionManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
        mAdoptions = new ArrayList<>();
    }

    public List<Adoption> getAdoptions(String whereClause, String[] whereArgs) {
        List<Adoption> adoptions = new ArrayList<>();

        ZooCursorWrapper cursor = queryAdoptions(whereClause, whereArgs);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                adoptions.add(cursor.getAdoption());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return adoptions;
    }

    // Query the animal names using a regex operator
    public List<Adoption> searchAdoptions(String searchQuery) {
        String whereClause = AdoptionsTable.Cols.NAME + " LIKE ? OR " + AdoptionsTable.Cols.NAME_NO_ACCENTS + " LIKE ?";
        String[] whereArgs = new String[] {"%" + searchQuery + "%", "%" + searchQuery + "%"};

        return getAdoptions(whereClause, whereArgs);
    }

    public void addAdoption(Adoption adoption) {
        mAdoptions.add(adoption);
    }

    public void updateAdoption(Adoption adoption) {
        ContentValues values = getContentValues(adoption);

        mDatabase.update(AdoptionsTable.NAME, values, AdoptionsTable.Cols.ID + " = ?", new String[] {adoption.getId()});
    }

    public boolean removeAdoption(Adoption adoption) {
        return mDatabase.delete(AdoptionsTable.NAME, AdoptionsTable.Cols.ID + " = ?", new String[] {adoption.getId()}) > 0;
    }

    // Get rid of the diacritics
    private String normalizeName(String name) {
        // Break the string into individual characters
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        // Remove the chars that are not letters (lowercase matches each accent)
        name = name.replaceAll("\\p{M}", "");

        return name;
    }

    // Bind one object to the statement
    private void bindAdoption(SQLiteStatement statement, Adoption adoption) {
        // The binding index starts at "1"
        statement.bindString(1, adoption.getId());
        statement.bindString(2, adoption.getLexiconId());
        statement.bindString(3, adoption.getName());
        statement.bindString(4, normalizeName(adoption.getName()));
        statement.bindString(5, Integer.toString(adoption.getPrice()));
        statement.bindString(6, Boolean.toString(adoption.isVisit()));

        statement.execute();
        statement.clearBindings();
    }

    // Flush all the adoptions into the database at once
    public void flushAdoptions() {
        // Prepare the query for late binding
        String query = "INSERT OR REPLACE INTO " + AdoptionsTable.NAME + " ( " +
                AdoptionsTable.Cols.ID + ", " +
                AdoptionsTable.Cols.LEXICON_ID + ", " +
                AdoptionsTable.Cols.NAME + ", " +
                AdoptionsTable.Cols.NAME_NO_ACCENTS + ", " +
                AdoptionsTable.Cols.PRICE + ", " +
                AdoptionsTable.Cols.VISIT + " ) VALUES ( ?, ?, ?, ?, ?, ? )";

        // Lock the DB file for the time being
        mDatabase.beginTransactionNonExclusive();

        SQLiteStatement statement = mDatabase.compileStatement(query);

        // Bind every class and order from the list
        for (Adoption adoption : mAdoptions) {
            bindAdoption(statement, adoption);
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + AdoptionsTable.NAME);
    }

    // Mapping of the adoption to the columns
    private static ContentValues getContentValues(Adoption adoption) {
        ContentValues values = new ContentValues();

        values.put(AdoptionsTable.Cols.ID, adoption.getId());
        values.put(AdoptionsTable.Cols.LEXICON_ID, adoption.getLexiconId());
        values.put(AdoptionsTable.Cols.NAME, adoption.getName());
        values.put(AdoptionsTable.Cols.PRICE, adoption.getPrice());
        values.put(AdoptionsTable.Cols.VISIT, adoption.isVisit());

        return values;
    }

    private ZooCursorWrapper queryAdoptions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(AdoptionsTable.NAME, null, whereClause, whereArgs, null, null, AdoptionsTable.Cols.NAME_NO_ACCENTS + " ASC");

        return new ZooCursorWrapper(cursor);
    }
}

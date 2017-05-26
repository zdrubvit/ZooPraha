package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.AdoptionsTable;
import cz.zdrubecky.zoopraha.model.Adoption;
import cz.zdrubecky.zoopraha.model.Animal;

public class AdoptionsManager {
    private static AdoptionsManager sAdoptionsManager;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private AdoptionsManager(Context context) {
        // Keep the app context instead of an activity (so it can be garbage collected, cause there's no reference to it)
        mContext = context.getApplicationContext();
        mDatabase = new ZooBaseHelper(mContext).getWritableDatabase();
    }

    public static AdoptionsManager get(Context context) {
        if (sAdoptionsManager == null) {
            sAdoptionsManager = new AdoptionsManager(context);
        }

        return sAdoptionsManager;
    }

    public List<Adoption> getAdoptions() {
        List<Adoption> adoptions = new ArrayList<>();

        ZooCursorWrapper cursor = queryAdoptions(null, null);

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

    public void setAdoptions(List<Adoption> adoptions) {
        for (Adoption adoption : adoptions) {
            addAdoption(adoption);
        }
    }

    public Adoption getAnimal(String id) {
        ZooCursorWrapper cursor = queryAdoptions(
                AdoptionsTable.Cols.ID + " = ?",
                new String[] { id }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getAdoption();
        } finally {
            cursor.close();
        }
    }

    public void addAdoption(Adoption adoption) {
        // todo check if it exists and then update
        ContentValues values = getContentValues(adoption);

        // The second argument - nullColumnHack - is used to force insert in the case of empty values
        mDatabase.insert(AdoptionsTable.NAME, null, values);
    }

    public void updateAdoption(Adoption adoption) {
        ContentValues values = getContentValues(adoption);

        mDatabase.update(AdoptionsTable.NAME, values, AdoptionsTable.Cols.ID + " = ?", new String[] {adoption.getId()});
    }

    public boolean removeAdoption(Adoption adoption) {
        return mDatabase.delete(AdoptionsTable.NAME, AdoptionsTable.Cols.ID + " = ?", new String[] {adoption.getId()}) > 0;
    }

    // Mapping of the adoption to the columns
    private static ContentValues getContentValues(Adoption adoption) {
        ContentValues values = new ContentValues();

        values.put(AdoptionsTable.Cols.ID, adoption.getId());
        values.put(AdoptionsTable.Cols.LEXICON_ID, adoption.getLexiconId());
        values.put(AdoptionsTable.Cols.OPENDATA_ID, adoption.getOpendataId());
        values.put(AdoptionsTable.Cols.NAME, adoption.getName());
        values.put(AdoptionsTable.Cols.PRICE, adoption.getPrice());
        values.put(AdoptionsTable.Cols.VISIT, adoption.isVisit());

        return values;
    }

    private ZooCursorWrapper queryAdoptions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(AdoptionsTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new ZooCursorWrapper(cursor);
    }
}

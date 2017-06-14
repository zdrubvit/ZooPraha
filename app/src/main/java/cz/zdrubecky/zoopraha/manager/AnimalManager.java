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
import cz.zdrubecky.zoopraha.database.ZooDBSchema.AnimalsTable;
import cz.zdrubecky.zoopraha.model.Animal;

public class AnimalManager {
    private SQLiteDatabase mDatabase;
    private List<Animal> mAnimals;

    public AnimalManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
        mAnimals = new ArrayList<>();
    }

    // The query works with a single filter so far, but the second argument counts on the future extension for more
    public List<Animal> getAnimals(String whereClause, String[] whereArgs) {
        List<Animal> animals = new ArrayList<>();

        ZooCursorWrapper cursor = queryAnimals(whereClause, whereArgs, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                animals.add(cursor.getAnimal());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return animals;
    }

    public Animal getAnimal(String id) {
        ZooCursorWrapper cursor = queryAnimals(
                AnimalsTable.Cols.ID + " = ?",
                new String[] { id },
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getAnimal();
        } finally {
            cursor.close();
        }
    }

    public void addAnimal(Animal animal) {
        mAnimals.add(animal);
    }

    public void updateAnimal(Animal animal) {
        ContentValues values = getContentValues(animal);

        mDatabase.update(AnimalsTable.NAME, values, AnimalsTable.Cols.ID + " = ?", new String[] {animal.getId()});
    }

    public boolean removeAnimal(Animal animal) {
        return mDatabase.delete(AnimalsTable.NAME, AnimalsTable.Cols.ID + " = ?", new String[] {animal.getId()}) > 0;
    }

    // Bind one object to the statement
    private void bindAnimal(SQLiteStatement statement, Animal animal) {
        // The binding index starts at "1"
        statement.bindString(1, animal.getId());
        statement.bindString(2, animal.getName());
        statement.bindString(3, animal.getLatinName());
        statement.bindString(4, animal.getClassName());
        statement.bindString(5, animal.getClassLatinName());
        statement.bindString(6, animal.getOrderName());
        statement.bindString(7, animal.getOrderLatinName());
        statement.bindString(8, animal.getDescription());
        statement.bindString(9, animal.getImage());
        statement.bindString(10, animal.getContinents());
        statement.bindString(11, animal.getDistribution());
        statement.bindString(12, animal.getBiotope());
        statement.bindString(13, animal.getBiotopesDetail());
        statement.bindString(14, animal.getFood());
        statement.bindString(15, animal.getFoodDetail());
        statement.bindString(16, animal.getProportions());
        statement.bindString(17, animal.getReproduction());
        statement.bindString(18, animal.getAttractions());
        statement.bindString(19, animal.getProjects());
        statement.bindString(20, animal.getBreeding());
        statement.bindString(21, animal.getLocation());
        statement.bindString(22, animal.getLocationUrl());

        statement.execute();
        statement.clearBindings();
    }

    // Flush all the animals into the database at once
    public void flushAnimals() {
        // Prepare the query for late binding
        String query = "INSERT OR REPLACE INTO " + AnimalsTable.NAME + " ( " +
                AnimalsTable.Cols.ID + ", " +
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
                AnimalsTable.Cols.LOCATION_URL + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        // Lock the DB file for the time being
        mDatabase.beginTransactionNonExclusive();

        SQLiteStatement statement = mDatabase.compileStatement(query);

        // Bind every class and order from the list
        for (Animal animal : mAnimals) {
            bindAnimal(statement, animal);
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + AnimalsTable.NAME);
    }

    private static ContentValues getContentValues(Animal animal) {
        ContentValues values = new ContentValues();

        values.put(AnimalsTable.Cols.ID, animal.getId());
        values.put(AnimalsTable.Cols.NAME, animal.getName());
        values.put(AnimalsTable.Cols.LATIN_NAME, animal.getLatinName());
        values.put(AnimalsTable.Cols.CLASS_NAME, animal.getClassName());
        values.put(AnimalsTable.Cols.CLASS_LATIN_NAME, animal.getClassLatinName());
        values.put(AnimalsTable.Cols.ORDER_NAME, animal.getOrderName());
        values.put(AnimalsTable.Cols.ORDER_LATIN_NAME, animal.getOrderLatinName());
        values.put(AnimalsTable.Cols.DESCRIPTION, animal.getDescription());
        values.put(AnimalsTable.Cols.IMAGE, animal.getImage());
        values.put(AnimalsTable.Cols.CONTINENTS, animal.getContinents());
        values.put(AnimalsTable.Cols.DISTRIBUTION, animal.getDistribution());
        values.put(AnimalsTable.Cols.BIOTOPE, animal.getBiotope());
        values.put(AnimalsTable.Cols.BIOTOPES_DETAIL, animal.getBiotopesDetail());
        values.put(AnimalsTable.Cols.FOOD, animal.getFood());
        values.put(AnimalsTable.Cols.FOOD_DETAIL, animal.getFoodDetail());
        values.put(AnimalsTable.Cols.PROPORTIONS, animal.getProportions());
        values.put(AnimalsTable.Cols.REPRODUCTION, animal.getReproduction());
        values.put(AnimalsTable.Cols.ATTRACTIONS, animal.getAttractions());
        values.put(AnimalsTable.Cols.PROJECTS, animal.getProjects());
        values.put(AnimalsTable.Cols.BREEDING, animal.getBreeding());
        values.put(AnimalsTable.Cols.LOCATION, animal.getLocation());
        values.put(AnimalsTable.Cols.LOCATION_URL, animal.getLocationUrl());

        return values;
    }

    private ZooCursorWrapper queryAnimals(String whereClause, String[] whereArgs, String limit) {
        Cursor cursor = mDatabase.query(AnimalsTable.NAME, null, whereClause, whereArgs, null, null, AnimalsTable.Cols.NAME + " ASC", limit);

        return new ZooCursorWrapper(cursor);
    }
    
    public String createWhereClauseFromFilter(String filter) {
        String whereClause = null;
        
        if (filter != null) {
            // "Switch" the filter type and match the appropriate database column (the filter names correspond to the API documentation)
            if (filter.equals("biotopes")) {
                whereClause = AnimalsTable.Cols.BIOTOPE + " = ?";
            } else if (filter.equals("class_name")) {
                whereClause = AnimalsTable.Cols.CLASS_NAME + " = ?";
            } else if (filter.equals("continents")) {
                whereClause = AnimalsTable.Cols.CONTINENTS + " = ?";
            } else if (filter.equals("description")) {
                whereClause = AnimalsTable.Cols.DESCRIPTION + " = ?";
            } else if (filter.equals("distribution")) {
                whereClause = AnimalsTable.Cols.DISTRIBUTION + " = ?";
            } else if (filter.equals("food")) {
                whereClause = AnimalsTable.Cols.FOOD + " = ?";
            } else if (filter.equals("location")) {
                whereClause = AnimalsTable.Cols.LOCATION + " = ?";
            } else if (filter.equals("name")) {
                whereClause = AnimalsTable.Cols.NAME + " = ?";
            } else if (filter.equals("order_name")) {
                whereClause = AnimalsTable.Cols.ORDER_NAME + " = ?";
            }
        }

        return whereClause;
    }
}

package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.AnimalsTable;
import cz.zdrubecky.zoopraha.model.Animal;

public class AnimalManager {
    private SQLiteDatabase mDatabase;

    public AnimalManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
    }

    public List<Animal> getAnimals() {
        List<Animal> animals = new ArrayList<>();

        ZooCursorWrapper cursor = queryAnimals(null, null);

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
                new String[] { id }
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
        // todo check if it exists and then update
        ContentValues values = getContentValues(animal);

        // The second argument - nullColumnHack - is used to force insert in the case of empty values
        mDatabase.insert(AnimalsTable.NAME, null, values);
    }

    public void updateAnimal(Animal animal) {
        ContentValues values = getContentValues(animal);

        mDatabase.update(AnimalsTable.NAME, values, AnimalsTable.Cols.ID + " = ?", new String[] {animal.getId()});
    }

    public boolean removeAnimal(Animal animal) {
        return mDatabase.delete(AnimalsTable.NAME, AnimalsTable.Cols.ID + " = ?", new String[] {animal.getId()}) > 0;
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

    private ZooCursorWrapper queryAnimals(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(AnimalsTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new ZooCursorWrapper(cursor);
    }
}

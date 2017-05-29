package cz.zdrubecky.zoopraha.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import cz.zdrubecky.zoopraha.database.ZooDBSchema.*;
import cz.zdrubecky.zoopraha.model.*;

import static java.lang.Boolean.parseBoolean;

// Wrapper around a db cursor, used to map the incoming values to a model object
public class ZooCursorWrapper extends CursorWrapper {
    public ZooCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Adoption getAdoption() {
        // Pull the data from the underlying cursor using their indexed names
        String id = getString(getColumnIndex(AdoptionsTable.Cols.ID));
        String lexicon_id = getString(getColumnIndex(AdoptionsTable.Cols.LEXICON_ID));
        int opendata_id = getInt(getColumnIndex(AdoptionsTable.Cols.OPENDATA_ID));
        String name = getString(getColumnIndex(AdoptionsTable.Cols.NAME));
        int price = getInt(getColumnIndex(AdoptionsTable.Cols.PRICE));
        boolean visit = parseBoolean(getString(getColumnIndex(AdoptionsTable.Cols.VISIT)));

        Adoption adoption = new Adoption();
        adoption.setId(id);
        adoption.setLexiconId(lexicon_id);
        adoption.setOpendataId(opendata_id);
        adoption.setName(name);
        adoption.setPrice(price);
        adoption.setVisit(visit);

        return adoption;
    }

    public Animal getAnimal() {
        String id = getString(getColumnIndex(AnimalsTable.Cols.ID));
        int opendata_id = getInt(getColumnIndex(AnimalsTable.Cols.OPENDATA_ID));
        String name = getString(getColumnIndex(AnimalsTable.Cols.NAME));
        String latin_name = getString(getColumnIndex(AnimalsTable.Cols.LATIN_NAME));
        String class_name = getString(getColumnIndex(AnimalsTable.Cols.CLASS_NAME));
        String class_latin_name = getString(getColumnIndex(AnimalsTable.Cols.CLASS_LATIN_NAME));
        String order_name = getString(getColumnIndex(AnimalsTable.Cols.ORDER_NAME));
        String order_latin_name = getString(getColumnIndex(AnimalsTable.Cols.ORDER_LATIN_NAME));
        String description = getString(getColumnIndex(AnimalsTable.Cols.DESCRIPTION));
        String image = getString(getColumnIndex(AnimalsTable.Cols.IMAGE));
        String continents = getString(getColumnIndex(AnimalsTable.Cols.CONTINENTS));
        String distribution = getString(getColumnIndex(AnimalsTable.Cols.DISTRIBUTION));
        String biotope = getString(getColumnIndex(AnimalsTable.Cols.BIOTOPE));
        String biotopes_detail = getString(getColumnIndex(AnimalsTable.Cols.BIOTOPES_DETAIL));
        String food = getString(getColumnIndex(AnimalsTable.Cols.FOOD));
        String food_detail = getString(getColumnIndex(AnimalsTable.Cols.FOOD_DETAIL));
        String proportions = getString(getColumnIndex(AnimalsTable.Cols.PROPORTIONS));
        String reproduction = getString(getColumnIndex(AnimalsTable.Cols.REPRODUCTION));
        String attractions = getString(getColumnIndex(AnimalsTable.Cols.ATTRACTIONS));
        String projects = getString(getColumnIndex(AnimalsTable.Cols.PROJECTS));
        String breeding = getString(getColumnIndex(AnimalsTable.Cols.BREEDING));
        String location = getString(getColumnIndex(AnimalsTable.Cols.LOCATION));
        String location_url = getString(getColumnIndex(AnimalsTable.Cols.LOCATION_URL));

        Animal animal = new Animal();
        animal.setId(id);
        animal.setOpendataId(opendata_id);
        animal.setName(name);
        animal.setLatinName(latin_name);
        animal.setClassName(class_name);
        animal.setClassLatinName(class_latin_name);
        animal.setOrderName(order_name);
        animal.setOrderLatinName(order_latin_name);
        animal.setDescription(description);
        animal.setImage(image);
        animal.setContinents(continents);
        animal.setDistribution(distribution);
        animal.setBiotope(biotope);
        animal.setBiotopesDetail(biotopes_detail);
        animal.setFood(food);
        animal.setFoodDetail(food_detail);
        animal.setProportions(proportions);
        animal.setReproduction(reproduction);
        animal.setAttractions(attractions);
        animal.setProjects(projects);
        animal.setBreeding(breeding);
        animal.setLocation(location);
        animal.setLocationUrl(location_url);

        return animal;
    }
}

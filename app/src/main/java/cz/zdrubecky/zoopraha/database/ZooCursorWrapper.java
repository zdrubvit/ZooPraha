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
        String name = getString(getColumnIndex(AdoptionsTable.Cols.NAME));
        int price = getInt(getColumnIndex(AdoptionsTable.Cols.PRICE));
        boolean visit = parseBoolean(getString(getColumnIndex(AdoptionsTable.Cols.VISIT)));

        Adoption adoption = new Adoption();
        adoption.setId(id);
        adoption.setLexiconId(lexicon_id);
        adoption.setName(name);
        adoption.setPrice(price);
        adoption.setVisit(visit);

        return adoption;
    }

    public Animal getAnimal() {
        String id = getString(getColumnIndex(AnimalsTable.Cols.ID));
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

    public Classification getClassification() {
        String id = getString(getColumnIndex(ClassificationsTable.Cols.ID));
        int opendataId = getInt(getColumnIndex(ClassificationsTable.Cols.OPENDATA_ID));
        String type = getString(getColumnIndex(ClassificationsTable.Cols.TYPE));
        int parent_id = getInt(getColumnIndex(ClassificationsTable.Cols.PARENT_ID));
        String name = getString(getColumnIndex(ClassificationsTable.Cols.NAME));
        String latin_name = getString(getColumnIndex(ClassificationsTable.Cols.LATIN_NAME));
        String slug = getString(getColumnIndex(ClassificationsTable.Cols.SLUG));

        Classification classification = new Classification();
        classification.setId(id);
        classification.setOpendataId(opendataId);
        classification.setType(type);
        classification.setParentId(parent_id);
        classification.setName(name);
        classification.setLatinName(latin_name);
        classification.setSlug(slug);

        return classification;
    }

    public Event getEvent() {
        String id = getString(getColumnIndex(EventsTable.Cols.ID));
        String start = getString(getColumnIndex(EventsTable.Cols.START));
        String end = getString(getColumnIndex(EventsTable.Cols.END));
        int duration = getInt(getColumnIndex(EventsTable.Cols.DURATION));
        String description = getString(getColumnIndex(EventsTable.Cols.DESCRIPTION));
        String name = getString(getColumnIndex(EventsTable.Cols.NAME));

        Event event = new Event();
        event.setId(id);
        event.setStart(start);
        event.setEnd(end);
        event.setDuration(duration);
        event.setDescription(description);
        event.setName(name);

        return event;
    }

    public Filter getFilter() {
        String name = getString(getColumnIndex(FiltersTable.Cols.NAME));
        String value = getString(getColumnIndex(FiltersTable.Cols.VALUE));
        int count = getInt(getColumnIndex(FiltersTable.Cols.COUNT));

        Filter filter = new Filter();
        filter.setName(name);
        filter.setValue(value);
        filter.setCount(count);

        return filter;
    }

    public Location getLocation() {
        String id = getString(getColumnIndex(LocationsTable.Cols.ID));
        String description = getString(getColumnIndex(LocationsTable.Cols.DESCRIPTION));
        int ordering = getInt(getColumnIndex(LocationsTable.Cols.ORDERING));
        String url = getString(getColumnIndex(LocationsTable.Cols.URL));
        String gps_x = getString(getColumnIndex(LocationsTable.Cols.GPS_X));
        String gps_y = getString(getColumnIndex(LocationsTable.Cols.GPS_Y));
        String name = getString(getColumnIndex(LocationsTable.Cols.NAME));
        String slug = getString(getColumnIndex(LocationsTable.Cols.SLUG));

        Location location = new Location();
        location.setId(id);
        location.setDescription(description);
        location.setOrdering(ordering);
        location.setUrl(url);
        location.setGpsX(gps_x);
        location.setGpsY(gps_y);
        location.setName(name);
        location.setSlug(slug);

        return location;
    }

    public QuizResult getQuizResult() {
        String date = getString(getColumnIndex(QuizResultsTable.Cols.DATE));
        String name = getString(getColumnIndex(QuizResultsTable.Cols.NAME));
        int score = getInt(getColumnIndex(QuizResultsTable.Cols.SCORE));
        int questionTime = getInt(getColumnIndex(QuizResultsTable.Cols.QUESTION_TIME));
        int totalTime = getInt(getColumnIndex(QuizResultsTable.Cols.TOTAL_TIME));
        int questionCount = getInt(getColumnIndex(QuizResultsTable.Cols.QUESTION_COUNT));
        int correctAnswerCount = getInt(getColumnIndex(QuizResultsTable.Cols.CORRECT_ANSWER_COUNT));

        QuizResult quizResult = new QuizResult();
        quizResult.setDate(date);
        quizResult.setName(name);
        quizResult.setScore(score);
        quizResult.setQuestionTime(questionTime);
        quizResult.setTotalTime(totalTime);
        quizResult.setQuestionCount(questionCount);
        quizResult.setCorrectAnswerCount(correctAnswerCount);

        return quizResult;
    }
}

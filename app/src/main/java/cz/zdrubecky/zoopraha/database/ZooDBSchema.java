package cz.zdrubecky.zoopraha.database;

public class ZooDBSchema {
    public static final class AdoptionsTable {
        public static final String NAME = "adoptions";

        public static final class Cols {
            // Each new column has to be mentioned on four different places - here, DBHelper, AdoptionLab, CursorWrapper
            public static final String ID = "backend_id";
            public static final String LEXICON_ID = "lexicon_id";
            public static final String NAME = "name";
            public static final String PRICE = "price";
            public static final String VISIT = "visit";
        }
    }

    public static final class AnimalsTable {
        public static final String NAME = "animals";

        public static final class Cols {
            public static final String ID = "backend_id";
            public static final String NAME = "name";
            public static final String LATIN_NAME = "latin_name";
            public static final String CLASS_NAME = "class_name";
            public static final String CLASS_LATIN_NAME = "class_latin_name";
            public static final String ORDER_NAME = "order_name";
            public static final String ORDER_LATIN_NAME = "order_latin_name";
            public static final String DESCRIPTION = "description";
            public static final String IMAGE = "image";
            public static final String CONTINENTS = "continents";
            public static final String DISTRIBUTION = "distribution";
            public static final String BIOTOPE = "biotope";
            public static final String BIOTOPES_DETAIL = "biotopes_detail";
            public static final String FOOD = "food";
            public static final String FOOD_DETAIL = "food_detail";
            public static final String PROPORTIONS = "proportions";
            public static final String REPRODUCTION = "reproduction";
            public static final String ATTRACTIONS = "attractions";
            public static final String PROJECTS = "projects";
            public static final String BREEDING = "breeding";
            public static final String LOCATION = "location";
            public static final String LOCATION_URL = "location_url";
        }
    }

    public static final class ClassificationsTable {
        public static final String NAME = "classifications";

        public static final class Cols {
            public static final String ID = "backend_id";
            public static final String OPENDATA_ID = "opendata_id";
            public static final String TYPE = "type";
            public static final String PARENT_ID = "parent_id";
            public static final String NAME = "name";
            public static final String LATIN_NAME = "latin_name";
            public static final String SLUG = "slug";
        }
    }

    public static final class EventsTable {
        public static final String NAME = "events";

        public static final class Cols {
            public static final String ID = "backend_id";
            public static final String START = "start";
            public static final String END = "end";
            public static final String DURATION = "duration";
            public static final String DESCRIPTION = "description";
            public static final String NAME = "name";
        }
    }

    public static final class FiltersTable {
        public static final String NAME = "filters";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String VALUE = "value";
            public static final String COUNT = "count";
        }
    }

    public static final class LocationsTable {
        public static final String NAME = "locations";

        public static final class Cols {
            public static final String ID = "backend_id";
            public static final String DESCRIPTION = "description";
            public static final String ORDERING = "ordering";
            public static final String URL = "url";
            public static final String GPS_X = "gps_x";
            public static final String GPS_Y = "gps_y";
            public static final String NAME = "name";
            public static final String SLUG = "slug";
        }
    }

    public static final class QuizResultsTable {
        public static final String NAME = "quiz_results";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String NAME = "name";
            public static final String SCORE = "score";
            public static final String QUESTION_TIME = "question_time";
            public static final String TOTAL_TIME = "total_time";
            public static final String QUESTION_COUNT = "question_count";
            public static final String CORRECT_ANSWER_COUNT = "correct_answer_count";
        }
    }
}

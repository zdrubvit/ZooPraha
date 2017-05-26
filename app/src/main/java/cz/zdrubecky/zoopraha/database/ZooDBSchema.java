package cz.zdrubecky.zoopraha.database;

public class ZooDBSchema {
    public static final class AdoptionsTable {
        public static final String NAME = "adoptions";

        public static final class Cols {
            // Each new column has to be mentioned on four different places - here, DBHelper, AdoptionLab, CursorWrapper
            public static final String ID = "id";
            public static final String LEXICON_ID = "lexicon_id";
            public static final String OPENDATA_ID = "opendata_id";
            public static final String NAME = "name";
            public static final String PRICE = "price";
            public static final String VISIT = "visit";
        }
    }
}

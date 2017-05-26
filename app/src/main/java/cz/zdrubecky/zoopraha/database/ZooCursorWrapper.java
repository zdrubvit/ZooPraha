package cz.zdrubecky.zoopraha.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

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
}

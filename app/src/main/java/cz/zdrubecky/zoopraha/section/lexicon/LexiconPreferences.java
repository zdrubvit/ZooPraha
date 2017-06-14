package cz.zdrubecky.zoopraha.section.lexicon;

import android.content.Context;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LexiconPreferences {
    private static final String PREF_ANIMAL_FILTER_KEY = "animalFilterKey";
    private static final String PREF_ANIMAL_FILTER_VALUE = "animalFilterValue";
    private static final String PREF_ANIMAL_SEARCH_QUERY = "animalSearchQuery";


    public static String getFilterKey(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_ANIMAL_FILTER_KEY, null);
    }

    public static String getFilterValue(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_ANIMAL_FILTER_VALUE, null);
    }

    public static String getSearchQuery(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_ANIMAL_SEARCH_QUERY, null);
    }

    public static void setFilterKey(Context context, String filterKey) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ANIMAL_FILTER_KEY, filterKey)
                .apply();
    }

    public static void setFilterValue(Context context, String filterValue) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ANIMAL_FILTER_VALUE, filterValue)
                .apply();
    }

    public static void setSearchQuery(Context context, String searchQuery) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ANIMAL_SEARCH_QUERY, searchQuery)
                .apply();
    }
}

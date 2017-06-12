package cz.zdrubecky.zoopraha.section.lexicon;

import android.content.Context;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LexiconPreferences {
    private static final String PREF_FILTER_KEY = "filterKey";
    private static final String PREF_FILTER_VALUE = "filterValue";

    public static String getFilterKey(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_FILTER_KEY, null);
    }

    public static String getFilterValue(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_FILTER_VALUE, null);
    }

    public static void setFilterKey(Context context, String filterKey) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_FILTER_KEY, filterKey)
                .apply();
    }

    public static void setFilterValue(Context context, String filterValue) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_FILTER_VALUE, filterValue)
                .apply();
    }
}

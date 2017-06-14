package cz.zdrubecky.zoopraha.section.adoption;

import android.content.Context;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AdoptionPreferences {
    private static final String PREF_ADOPTION_SEARCH_QUERY = "adoptionSearchQuery";

    public static String getSearchQuery(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_ADOPTION_SEARCH_QUERY, null);
    }

    public static void setSearchQuery(Context context, String searchQuery) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ADOPTION_SEARCH_QUERY, searchQuery)
                .apply();
    }
}

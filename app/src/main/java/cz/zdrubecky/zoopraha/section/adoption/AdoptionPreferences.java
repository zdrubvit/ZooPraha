package cz.zdrubecky.zoopraha.section.adoption;

import android.content.Context;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AdoptionPreferences {
    private static final String PREF_ADOPTION_SEARCH_QUERY = "adoptionSearchQuery";
    private static final String PREF_ADOPTION_CURRENT_PAGE = "adoptionCurrentPage";

    public static String getSearchQuery(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_ADOPTION_SEARCH_QUERY, null);
    }

    public static int getCurrentPage(Context context) {
        return getDefaultSharedPreferences(context).getInt(PREF_ADOPTION_CURRENT_PAGE, 1);
    }

    public static void setSearchQuery(Context context, String searchQuery) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ADOPTION_SEARCH_QUERY, searchQuery)
                .apply();
    }

    public static void setCurrentPage(Context context, int currentPage) {
        getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_ADOPTION_CURRENT_PAGE, currentPage)
                .apply();
    }
}

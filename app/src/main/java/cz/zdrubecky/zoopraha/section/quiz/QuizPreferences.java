package cz.zdrubecky.zoopraha.section.quiz;

import android.content.Context;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class QuizPreferences {
    private static final String PREF_QUESTION_TIME = "questionTime";
    private static final String PREF_QUESTION_COUNT = "questionCount";
    private static final String PREF_USER_NAME = "userName";

    public static int getQuestionTime(Context context) {
        return getDefaultSharedPreferences(context).getInt(PREF_QUESTION_TIME, 10);
    }

    public static int getQuestionCount(Context context) {
        return getDefaultSharedPreferences(context).getInt(PREF_QUESTION_COUNT, 10);
    }

    public static String getUserName(Context context) {
        return getDefaultSharedPreferences(context).getString(PREF_USER_NAME, "");
    }

    public static void setQuestionTime(Context context, int questionTime) {
        getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_QUESTION_TIME, questionTime)
                .apply();
    }

    public static void setQuestionCount(Context context, int questionCount) {
        getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_QUESTION_COUNT, questionCount)
                .apply();
    }

    public static void setUserName(Context context, String userName) {
        getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_NAME, userName)
                .apply();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPrefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();
    }
}

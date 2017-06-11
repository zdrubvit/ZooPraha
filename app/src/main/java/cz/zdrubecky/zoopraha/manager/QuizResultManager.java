package cz.zdrubecky.zoopraha.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.zdrubecky.zoopraha.database.ZooBaseHelper;
import cz.zdrubecky.zoopraha.database.ZooCursorWrapper;
import cz.zdrubecky.zoopraha.database.ZooDBSchema.QuizResultsTable;
import cz.zdrubecky.zoopraha.model.QuizResult;

public class QuizResultManager {
    private SQLiteDatabase mDatabase;

    public QuizResultManager(Context context) {
        ZooBaseHelper zooBaseHelper = ZooBaseHelper.getInstance(context);
        mDatabase = zooBaseHelper.getWritableDatabase();
    }

    public List<QuizResult> getQuizResults() {
        List<QuizResult> quizResults = new ArrayList<>();

        ZooCursorWrapper cursor = queryQuizResults(null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                quizResults.add(cursor.getQuizResult());
                cursor.moveToNext();
            }
        } finally {
            // Close the cursor so that we don't run out of handles
            cursor.close();
        }

        return quizResults;
    }

    public void addQuizResult(QuizResult quizResult) {
        ContentValues values = getContentValues(quizResult);

        mDatabase.insert(QuizResultsTable.NAME, null, values);
    }

    public void dropTable() {
        mDatabase.execSQL("DROP TABLE IF EXISTS " + QuizResultsTable.NAME);
    }

    // Mapping of the QuizResult to the columns
    private static ContentValues getContentValues(QuizResult quizResult) {
        ContentValues values = new ContentValues();

        values.put(QuizResultsTable.Cols.DATE, quizResult.getDate());
        values.put(QuizResultsTable.Cols.NAME, quizResult.getName());
        values.put(QuizResultsTable.Cols.SCORE, quizResult.getScore());
        values.put(QuizResultsTable.Cols.QUESTION_TIME, quizResult.getQuestionTime());
        values.put(QuizResultsTable.Cols.TOTAL_TIME, quizResult.getTotalTime());
        values.put(QuizResultsTable.Cols.QUESTION_COUNT, quizResult.getQuestionCount());
        values.put(QuizResultsTable.Cols.CORRECT_ANSWER_COUNT, quizResult.getCorrectAnswerCount());

        return values;
    }

    private ZooCursorWrapper queryQuizResults(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(QuizResultsTable.NAME, null, whereClause, whereArgs, null, null, QuizResultsTable.Cols.SCORE + " DESC");

        return new ZooCursorWrapper(cursor);
    }
}

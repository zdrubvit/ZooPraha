package cz.zdrubecky.zoopraha.manager;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import cz.zdrubecky.zoopraha.model.Question;

public class QuestionManager {
    // A singleton class, available in memory throughout the whole application lifetime so that the questions are not lost
    private static QuestionManager sQuestionManager;

    private List<Question> mQuestions;
    private Context mContext;

    private QuestionManager(Context context) {
        // Keep the app context instead of an activity (so it can be garbage collected, cause there's no reference to it)
        mContext = context.getApplicationContext();

        mQuestions = new ArrayList<>();
    }

    public static QuestionManager get(Context context) {
        if (sQuestionManager == null) {
            sQuestionManager = new QuestionManager(context);
        }

        return sQuestionManager;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public Question getQuestion(int position) {
        return mQuestions.get(position);
    }

    public void addQuestion(Question question) {
        mQuestions.add(question);
    }

    public void updateQuestion(Question question) {

    }
}

package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.QuestionManager;
import cz.zdrubecky.zoopraha.model.JsonApiObject;
import cz.zdrubecky.zoopraha.model.Question;

public class QuizActivity
        extends SingleFragmentActivity
        implements QuestionFragment.Callbacks {
    
    private static final String TAG = "QuizActivity";

    private int mQuestionCount;
    private int mQuestionPosition;
    private int mCorrectAnswers;
    private int mIncorrectAnswers;
    private int mTotalTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set all the variables to their default values
        mQuestionCount = 10;
        mQuestionPosition = mCorrectAnswers = mIncorrectAnswers = mTotalTime = 0;

        DataFetcher dataFetcher = new DataFetcher();
        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response) {
                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
                QuestionManager manager = QuestionManager.get(QuizActivity.this);
                List<JsonApiObject.Resource> data = response.getData();
                Gson gson = new Gson();

                // todo make this threaded
                for (int i = 0; i < data.size(); i++) {
                    Question question = gson.fromJson(data.get(i).getDocument(), Question.class);
                    question.setId(data.get(i).getId());
                    manager.addQuestion(question);
                }

                replaceQuestionFragment();
            }
        });

        dataFetcher.getQuestions(Integer.toString(mQuestionCount));
    }

    @Override
    protected Fragment createInitialFragment() {
        return new LoadingScreenFragment();
    }

    private void replaceQuestionFragment() {
        Fragment newQuestion = QuestionFragment.newInstance(mQuestionPosition);

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), newQuestion)
                .commit();
    }

    @Override
    public void onQuestionAnswered(Question question) {
        // Check the answer's accuracy and increment the respective counter
        if (question.isAnsweredCorrectly()) {
            mCorrectAnswers++;
        } else {
            mIncorrectAnswers++;
        }

        // Increase the total quiz time
        mTotalTime += question.getTimeToAnswer();

        mQuestionPosition++;
        if (mQuestionPosition < mQuestionCount) {
            // Create and add a fragment with the next question
            replaceQuestionFragment();
        }
    }
}

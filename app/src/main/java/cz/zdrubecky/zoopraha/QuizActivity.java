package cz.zdrubecky.zoopraha;

import android.content.Intent;
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

    private QuestionManager mQuestionManager;
    private int mQuestionCount;
    private int mQuestionPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestionManager = QuestionManager.get(this);

        // It's a new game - remove the old questions
        mQuestionManager.deleteQuestions();

        // Set all the variables to their default values
        mQuestionCount = 2;
        mQuestionPosition = 0;

        DataFetcher dataFetcher = new DataFetcher();
        dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
            @Override
            public void onDataFetched(JsonApiObject response) {
                Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " resources.");
                List<JsonApiObject.Resource> data = response.getData();
                Gson gson = new Gson();

                // todo make this threaded
                for (int i = 0; i < data.size(); i++) {
                    Question question = gson.fromJson(data.get(i).getDocument(), Question.class);
                    question.setId(data.get(i).getId());
                    mQuestionManager.addQuestion(question);
                }

                // The questions have been imported and the first one can finally be displayed
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
        mQuestionPosition++;
        if (mQuestionPosition < mQuestionCount) {
            // Create and add a fragment with the next question
            replaceQuestionFragment();
        } else {
            Intent i = new Intent(QuizActivity.this, QuizResultActivity.class);

            startActivity(i);
        }
    }
}

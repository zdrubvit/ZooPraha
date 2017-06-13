package cz.zdrubecky.zoopraha.section.quiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cz.zdrubecky.zoopraha.SingleFragmentActivity;
import cz.zdrubecky.zoopraha.api.DataFetcher;
import cz.zdrubecky.zoopraha.manager.QuestionManager;
import cz.zdrubecky.zoopraha.model.JsonApiObject;
import cz.zdrubecky.zoopraha.model.Question;

public class QuizActivity
        extends SingleFragmentActivity
        implements QuestionFragment.Callbacks {
    
    private static final String TAG = "QuizActivity";
    private static final String KEY_QUESTION_POSITION = "question_position";
    private static final String KEY_QUESTION_COUNT = "question_count";

    private QuestionManager mQuestionManager;
    private int mQuestionCount;
    private int mQuestionPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestionManager = QuestionManager.get(this);

        if (savedInstanceState != null) {
            // There was a configuration change - the game resumes in the previous state (the initial fragment is recreated thanks to its own ID)
            mQuestionCount = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            mQuestionPosition = savedInstanceState.getInt(KEY_QUESTION_POSITION);
        } else {
            // It's a new game - remove the old questions
            mQuestionManager.deleteQuestions();

            // Set all the variables to their default values
            mQuestionCount = QuizPreferences.getQuestionCount(this);
            mQuestionPosition = 0;

            // Get the questions from API and display them eventually
            DataFetcher dataFetcher = new DataFetcher(this);
            dataFetcher.setDataFetchedListener(new DataFetcher.DataFetchedListener() {
                @Override
                public void onDataFetched(JsonApiObject response, int statusCode, String etag) {
                    Log.i(TAG, "Listener called with " + response.getMeta().getCount() + " Question resource objects.");
                    response.setStatus(statusCode);
                    response.setEtag(etag);

                    new GetQuestionsTask(response).execute();
                }
            });

            dataFetcher.getQuestions(mQuestionCount);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_QUESTION_COUNT, mQuestionCount);
        outState.putInt(KEY_QUESTION_POSITION, mQuestionPosition);
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

    // This class is not static and therefore can block the garbage collection of its parent class, but it's useful to update the fragment from here
    private class GetQuestionsTask extends AsyncTask<Void, Void, Void> {
        private JsonApiObject mResponse;

        public GetQuestionsTask(JsonApiObject response) {
            mResponse = response;
        }

        // Parse the response and update the manager with newly acquired data
        @Override
        protected Void doInBackground(Void... voids) {
            List<JsonApiObject.Resource> data = mResponse.getData();
            Gson gson = new Gson();

            for (int i = 0; i < data.size(); i++) {
                Question question = gson.fromJson(data.get(i).getDocument(), Question.class);
                question.setId(data.get(i).getId());
                mQuestionManager.addQuestion(question);
            }

            // Don't return anything, there's no need
            return null;
        }

        // This method is handled by the UI thread so it can update the UI safely after all items have been updated
        @Override
        protected void onPostExecute(Void v) {
            // The questions have been imported and the first one can finally be displayed
            replaceQuestionFragment();
        }
    }
}

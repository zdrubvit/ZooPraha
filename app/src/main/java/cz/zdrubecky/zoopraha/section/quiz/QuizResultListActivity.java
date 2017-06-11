package cz.zdrubecky.zoopraha.section.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.manager.QuizResultManager;
import cz.zdrubecky.zoopraha.model.QuizResult;

public class QuizResultListActivity extends AppCompatActivity {
    private QuizResultManager mQuizResultManager;
    private RecyclerView mRecyclerView;
    private ResultAdapter mResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        mQuizResultManager = new QuizResultManager(this);
        List<QuizResult> quizResults = mQuizResultManager.getQuizResults();

        if (quizResults.size() > 0) {
            if (mResultAdapter == null) {
                mResultAdapter = new ResultAdapter(quizResults);
                mRecyclerView.setAdapter(mResultAdapter);
            }
        } else {
            RelativeLayout emptyList = (RelativeLayout) findViewById(R.id.list_empty);
            emptyList.setVisibility(View.VISIBLE);
        }
    }
    
    private class ResultHolder extends RecyclerView.ViewHolder {
        private QuizResult mQuizResult;

        private TextView mPositionTextView;
        private TextView mNameTextView;
        private TextView mScoreTextView;
        
        public ResultHolder(View itemView) {
            super(itemView);

            mPositionTextView = (TextView) itemView.findViewById(R.id.activity_quiz_result_list_item_position_textview);
            mNameTextView = (TextView) itemView.findViewById(R.id.activity_quiz_result_list_item_name_textview);
            mScoreTextView = (TextView) itemView.findViewById(R.id.activity_quiz_result_list_item_score_textview);
        }
        
        public void bindResult(QuizResult quizResult, int position) {
            mQuizResult = quizResult;

            mPositionTextView.setText(Integer.toString(position));
            mNameTextView.setText(mQuizResult.getName());
            mScoreTextView.setText(Integer.toString(mQuizResult.getScore()));
        }
    }
    
    private class ResultAdapter extends RecyclerView.Adapter<ResultHolder> {
        private List<QuizResult> mQuizResults;
        
        public ResultAdapter(List<QuizResult> quizResults) {
            mQuizResults = quizResults;
        }

        @Override
        public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(QuizResultListActivity.this);
            
            View v = inflater.inflate(R.layout.activity_quiz_result_list_item, parent, false);
            
            return new ResultHolder(v);
        }

        @Override
        public void onBindViewHolder(ResultHolder holder, int position) {
            QuizResult quizResult = mQuizResults.get(position);
            
            holder.bindResult(quizResult, position + 1);
        }

        @Override
        public int getItemCount() {
            return mQuizResults.size();
        }
    } 
}

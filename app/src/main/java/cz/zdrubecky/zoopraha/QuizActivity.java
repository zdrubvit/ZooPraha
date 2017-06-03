package cz.zdrubecky.zoopraha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class QuizActivity extends SingleFragmentActivity {
    private int mQuestionPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuestionPosition = 1;
    }

    @Override
    protected Fragment createFragment() {
        return QuestionFragment.newInstance(mQuestionPosition);
    }
}

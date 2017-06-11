package cz.zdrubecky.zoopraha.section.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

import cz.zdrubecky.zoopraha.R;

public class QuizSettingsActivity extends AppCompatActivity {
    private EditText mTimeEditText;
    private Spinner mQuestionsSpinner;
    private EditText mNameEditText;
    private Button mSaveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_settings);

        // Get the value from preferences and set it in the view
        mTimeEditText = (EditText) findViewById(R.id.quiz_settings_time_editview);
        mTimeEditText.setText(Integer.toString(QuizPreferences.getQuestionTime(this)));

        mQuestionsSpinner = (Spinner) findViewById(R.id.quiz_settings_questions_spinner);
        // If there's already some value present, pre-select it
        String currentSpinnerValue = Integer.toString(QuizPreferences.getQuestionCount(this));
        String[] spinnerOptionsArray = getResources().getStringArray(R.array.quiz_settings_questions_options_array);
        mQuestionsSpinner.setSelection(Arrays.asList(spinnerOptionsArray).indexOf(currentSpinnerValue));

        mNameEditText = (EditText) findViewById(R.id.quiz_settings_name_editview);
        mNameEditText.setText(QuizPreferences.getUserName(this));

        mSaveButton = (Button) findViewById(R.id.quiz_settings_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new settings and save them in preferences
                QuizPreferences.setQuestionTime(QuizSettingsActivity.this, Integer.parseInt(mTimeEditText.getText().toString()));
                QuizPreferences.setQuestionCount(QuizSettingsActivity.this, Integer.parseInt(mQuestionsSpinner.getSelectedItem().toString()));
                QuizPreferences.setUserName(QuizSettingsActivity.this, mNameEditText.getText().toString());

                // Send the user back to the menu
                Intent i = new Intent(QuizSettingsActivity.this, QuizMenuActivity.class);

                startActivity(i);
            }
        });
    }
}

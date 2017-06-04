package cz.zdrubecky.zoopraha.model;

import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("_id")
    private String mId;
    @SerializedName("text")
    private String mText;
    @SerializedName("correct_answer")
    private String mCorrectAnswer;
    @SerializedName("incorrect_answers")
    private String[] mIncorrectAnswers;
    @SerializedName("difficulty")
    private int mDifficulty;
    @SerializedName("type")
    private String mType;
    @SerializedName("answer_object_id")
    private String mAnswerObjectId;
    @SerializedName("shown")
    private boolean mShown;
    @SerializedName("answered_correctly")
    private boolean mAnsweredCorrectly;
    @SerializedName("flagged")
    private boolean mFlagged;
    private int mTimeToAnswer;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        mCorrectAnswer = correctAnswer;
    }

    public String[] getIncorrectAnswers() {
        return mIncorrectAnswers;
    }

    public void setIncorrectAnswers(String[] incorrectAnswers) {
        mIncorrectAnswers = incorrectAnswers;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getAnswerObjectId() {
        return mAnswerObjectId;
    }

    public void setAnswerObjectId(String answerObjectId) {
        mAnswerObjectId = answerObjectId;
    }

    public boolean isShown() {
        return mShown;
    }

    public void setShown(boolean shown) {
        mShown = shown;
    }

    public boolean isAnsweredCorrectly() {
        return mAnsweredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        mAnsweredCorrectly = answeredCorrectly;
    }

    public boolean isFlagged() {
        return mFlagged;
    }

    public void setFlagged(boolean flagged) {
        mFlagged = flagged;
    }

    public int getTimeToAnswer() {
        return mTimeToAnswer;
    }

    public void setTimeToAnswer(int timeToAnswer) {
        mTimeToAnswer = timeToAnswer;
    }
}

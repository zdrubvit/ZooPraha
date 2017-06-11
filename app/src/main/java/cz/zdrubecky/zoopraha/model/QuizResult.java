package cz.zdrubecky.zoopraha.model;

public class QuizResult {
    private String mDate;
    private String mName;
    private int mScore;
    private int mQuestionTime;
    private int mTotalTime;
    private int mQuestionCount;
    private int mCorrectAnswerCount;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getQuestionTime() {
        return mQuestionTime;
    }

    public void setQuestionTime(int questionTime) {
        mQuestionTime = questionTime;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(int totalTime) {
        mTotalTime = totalTime;
    }

    public int getQuestionCount() {
        return mQuestionCount;
    }

    public void setQuestionCount(int questionCount) {
        mQuestionCount = questionCount;
    }

    public int getCorrectAnswerCount() {
        return mCorrectAnswerCount;
    }

    public void setCorrectAnswerCount(int correctAnswerCount) {
        mCorrectAnswerCount = correctAnswerCount;
    }
}

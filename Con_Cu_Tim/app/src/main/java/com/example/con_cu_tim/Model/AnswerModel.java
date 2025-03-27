package com.example.con_cu_tim.Model;

public class AnswerModel {

    private int id;
    private int questionId;
    private String answer;
    private int status;
    private boolean isCorrect;

    public AnswerModel() {
    }

    public AnswerModel(int id, int questionId, String answer, int status, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.answer = answer;
        this.status = status;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}

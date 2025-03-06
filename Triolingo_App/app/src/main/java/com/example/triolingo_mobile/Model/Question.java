package com.example.triolingo_mobile.Model;

import com.example.triolingo_mobile.DAO.ExerciseDAO;

public class Question {
    private int id;
    private String question1;
    private int status;
    private int exerciseId;
    private int mark;

    public Question() {
    }

    public Question(int id, String question1, int status, int exerciseId, int mark) {
        this.id = id;
        this.question1 = question1;
        this.status = status;
        this.exerciseId = exerciseId;
        this.mark = mark;
    }

    public Exercise getExercise(){
        return ExerciseDAO.getInstance().getExercise(getExerciseId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question1='" + question1 + '\'' +
                ", status=" + status +
                ", exerciseId=" + exerciseId +
                ", mark=" + mark +
                '}';
    }
}

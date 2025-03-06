package com.example.triolingo_mobile.Model;

public class Course {
//    {
//        "id": 1,
//            "name": "Course 1",
//            "description": "day la course 1",
//            "rateAverage": 5,
//            "status": 1,
//            "note": "cours 1"
//    }
    private int id;
    private String name;
    private String description;
    private float rateAverage;
    private int status;
    private String note;
    private boolean isAssign;

    public Course() {
    }

    public Course(int id, String name, String description, float rateAverage, int status, String note) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rateAverage = rateAverage;
        this.status = status;
        this.note = note;
    }

    public boolean isAssign() {
        return isAssign;
    }

    public void setAssign(boolean assign) {
        isAssign = assign;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rateAverage=" + rateAverage +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRateAverage() {
        return rateAverage;
    }

    public void setRateAverage(float rateAverage) {
        this.rateAverage = rateAverage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

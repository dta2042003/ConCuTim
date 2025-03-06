package com.example.triolingo_mobile.Model;

import com.example.triolingo_mobile.DAO.UnitDAO;

public class LessonModel {
    private int id;
    private String name;
    private int unitId;
    private String description;
    private int status;
    private String note;

    private int totalMark;
    private int userMark;
    private boolean isPreviousActived;
    private int studentCourse;

    public int getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(int studentCourse) {
        this.studentCourse = studentCourse;
    }

    public boolean isPreviousActived() {
        return isPreviousActived;
    }

    public void setPreviousActived(boolean previousActived) {
        isPreviousActived = previousActived;
    }

    public int getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(int totalMark) {
        this.totalMark = totalMark;
    }

    public int getUserMark() {
        return userMark;
    }

    public void setUserMark(int userMark) {
        this.userMark = userMark;
    }

    public LessonModel() {
    }

    public LessonModel(int id, String name, int unitId, String description, int status, String note) {
        this.id = id;
        this.name = name;
        this.unitId = unitId;
        this.description = description;
        this.status = status;
        this.note = note;
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

    public int getUnitId() {
        return unitId;
    }

    public UnitModel getUnit() {
        return UnitDAO.getInstance().getDetail(getUnitId());
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "LessonModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitId=" + unitId +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }
}

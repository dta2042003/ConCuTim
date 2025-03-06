package com.example.triolingo_mobile.Model;

public class UnitModel {
    private int id;
    private String name;
    private String description;
    private int order;
    private int courseId;
    private int status;
    private String note;

    public UnitModel() {
    }

    public UnitModel(int id, String name, String description, int order, int courseId, int status, String note) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.order = order;
        this.courseId = courseId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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
        return "UnitModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", order=" + order +
                ", courseId=" + courseId +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }
}

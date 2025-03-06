package com.example.triolingo_mobile.Model;

import com.example.triolingo_mobile.DAO.LessonDAO;

import java.io.Serializable;

public class Exercise implements Serializable {
    private int id;
    private int status;
    private String title;
    private String description;
    private int typeId;
    private int lessonId;
    private String file;
    private String fileName;

    public Exercise() {
    }

    public Exercise(int id, int status, String title, String description, int typeId, int lessonId, String file, String fileName) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.typeId = typeId;
        this.lessonId = lessonId;
        this.file = file;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getLessonId() {
        return lessonId;
    }
    public LessonModel getLesson() {
        return LessonDAO.getInstance().getDetail(getLessonId());
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", typeId=" + typeId +
                ", lessonId=" + lessonId +
                ", file='" + file + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}

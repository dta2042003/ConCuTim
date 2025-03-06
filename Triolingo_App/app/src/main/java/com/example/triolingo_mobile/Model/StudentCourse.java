package com.example.triolingo_mobile.Model;

import com.example.triolingo_mobile.DAO.CourseDAO;
import com.example.triolingo_mobile.DAO.UserDAO;

public class StudentCourse {
    public int id;
    public int studentId;
    public int courseId;
    public float rate;
    public String comment;

    public StudentCourse(int id, int studentId, int courseId, float rate, String comment) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.rate = rate;
        this.comment = comment;
    }

    public StudentCourse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public UserEntity getStudent() {
        return UserDAO.getInstance().GetUserById(getStudentId());
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public Course getCourse() {
        return CourseDAO.getInstance().getDetail(getCourseId());
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

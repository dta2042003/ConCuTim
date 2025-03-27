package com.example.con_cu_tim.DAO;

import com.example.con_cu_tim.DataAccess.DbContext;
import com.example.con_cu_tim.Model.StudentCourse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseDAO extends DbContext {

    private static StudentCourseDAO instance;
    public static String DB_TABLE_NAME="StudentCourse";

    public static StudentCourseDAO getInstance() {
        if (StudentCourseDAO.instance == null) {
            StudentCourseDAO.instance = new StudentCourseDAO();
        }
        return StudentCourseDAO.instance;
    }

    public List<StudentCourse> getList(String search) {
        List<StudentCourse> list = new ArrayList<>();
        String sql = "Select * from "+DB_TABLE_NAME+" where  "+search;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                StudentCourse c = new StudentCourse();
                c.setStudentId(rs.getInt("StudentId"));
                c.setCourseId(rs.getInt("CourseId"));
                c.setComment(rs.getString("Comment"));
                c.setRate(rs.getFloat("Rate"));
                c.setId(rs.getInt("Id"));
                list.add(c);
            }
            rs.close();
        } catch (Exception ex) {

        }
        return list;
    }
    public boolean assignStudentToCourse(int studentId,int courseId){
        boolean check = false;
        String sql = "Insert into "+DB_TABLE_NAME+" (StudentId,CourseId) Values (?,?)";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, studentId);
            pre.setInt(2, courseId);
            check = pre.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;

    }
    public StudentCourse getDetail(int id) {
        String sql = "Select * from "+DB_TABLE_NAME+" where id= "+id;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                StudentCourse c = new StudentCourse();
                c.setStudentId(rs.getInt("StudentId"));
                c.setCourseId(rs.getInt("CourseId"));
                c.setComment(rs.getString("Comment"));
                c.setRate(rs.getFloat("Rate"));
                c.setId(rs.getInt("Id"));
                return c;
            }
            rs.close();
        } catch (Exception ex) {

        }
        return null;
    }
}

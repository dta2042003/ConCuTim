package com.example.con_cu_tim.DAO;

import com.example.con_cu_tim.DataAccess.DbContext;
import com.example.con_cu_tim.Model.StudentLesson;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentLessonDAO extends DbContext {

    private static StudentLessonDAO instance;
    private static String DB_TABLE_NAME="[StudentLesson]";

    public static StudentLessonDAO getInstance() {
        if (StudentLessonDAO.instance == null) {
            StudentLessonDAO.instance = new StudentLessonDAO();
        }
        return StudentLessonDAO.instance;
    }

    public List<StudentLesson> getList(String search) {
        List<StudentLesson> list = new ArrayList<>();
        String sql = "Select * from "+DB_TABLE_NAME+" where  "+search;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                StudentLesson c = new StudentLesson();
                c.setId(rs.getInt("Id"));
                c.setMark(rs.getInt("Mark"));
                c.setLessonId(rs.getInt("LessionId"));
                c.setStudentCourseId(rs.getInt("StudentCourseId"));
                list.add(c);
            }
            rs.close();
        } catch (Exception ex) {

        }
        return list;
    }

    public boolean createStudentLesson(StudentLesson studentLesson){
        boolean check = false;
        String sql = "Insert into "+DB_TABLE_NAME+" (LessionId,StudentCourseId,Mark) Values (?,?,?)";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, studentLesson.getLessonId());
            pre.setInt(2, studentLesson.getStudentCourseId());
            pre.setInt(3,studentLesson.getMark());
            check = pre.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }

    public boolean updateStudentLesson(StudentLesson studentLesson){
        boolean check = false;
        String sql = "UPDATE "+DB_TABLE_NAME+" SET [Mark] = ? WHERE LessionId = ? AND StudentCourseId = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1,studentLesson.getMark());
            pre.setInt(2, studentLesson.getLessonId());
            pre.setInt(3, studentLesson.getStudentCourseId());
            check = pre.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }
    public StudentLesson getDetail(int studentCourse,int lessonId) {
        String sql = "Select * from "+DB_TABLE_NAME+" where LessionId= "+lessonId+" AND StudentCourseId="+studentCourse;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                StudentLesson c = new StudentLesson();
                c.setId(rs.getInt("Id"));
                c.setMark(rs.getInt("Mark"));
                c.setLessonId(rs.getInt("LessionId"));
                c.setStudentCourseId(rs.getInt("StudentCourseId"));
                return c;
            }
            rs.close();
        } catch (Exception ex) {

        }
        return null;
    }

    //Lấy điểm tổng đã đạt được trong Course của Student
    public int getMarkByUser(int userId){
        Integer sum= 0;
        String sql ="select sum(Mark) as Mark from "+DB_TABLE_NAME+" where StudentCourseId in (select id from StudentCourse where StudentId="+userId+")";
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                sum = rs.getInt("Mark");
                System.out.println(sum);
            }
            rs.close();
        } catch (Exception ex) {
        }
        return sum;
    }

    public StudentLesson getDetail(int id) {
        String sql = "Select * from "+DB_TABLE_NAME+" where id= "+id;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                StudentLesson c = new StudentLesson();
                c.setId(rs.getInt("Id"));
                c.setMark(rs.getInt("Mark"));
                c.setLessonId(rs.getInt("LessionId"));
                c.setStudentCourseId(rs.getInt("StudentCourseId"));
                return c;
            }
            rs.close();
        } catch (Exception ex) {

        }
        return null;
    }
}

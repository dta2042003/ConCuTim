package com.example.triolingo_mobile.DAO;

import com.example.triolingo_mobile.DataAccess.DbContext;
import com.example.triolingo_mobile.Model.Course;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DbContext {

    private static CourseDAO instance;
    private static String DB_TABLE_NAME="Course";

    public static CourseDAO getInstance() {
        if (CourseDAO.instance == null) {
            CourseDAO.instance = new CourseDAO();
        }
        return CourseDAO.instance;
    }

    public List<Course> getList(String search) {
        List<Course> list = new ArrayList<>();
        String sql = "Select * from "+DB_TABLE_NAME+" where  "+search;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                Course c = new Course();
                c.setName(rs.getString("Name"));
                c.setDescription(rs.getString("Description"));
                c.setNote(rs.getString("Note"));
                c.setRateAverage(rs.getFloat("RateAverage"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                list.add(c);
            }
            rs.close();
        } catch (Exception ex) {

        }
        return list;
    }
    public Course getDetail(int id) {
        String sql = "Select * from "+DB_TABLE_NAME+" where id= "+id;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                Course c = new Course();
                c.setName(rs.getString("Name"));
                c.setDescription(rs.getString("Description"));
                c.setNote(rs.getString("Note"));
                c.setRateAverage(rs.getFloat("RateAverage"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                return c;
            }
            rs.close();
        } catch (Exception ex) {

        }
        return null;
    }
}

package com.example.triolingo_mobile.DAO;

import com.example.triolingo_mobile.DataAccess.DbContext;
import com.example.triolingo_mobile.Model.LessonModel;
import com.example.triolingo_mobile.Model.StudentCourse;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO extends DbContext {

    private static LessonDAO instance;
    private static String DB_TABLE_NAME="Lesson";

    public static LessonDAO getInstance() {
        if (LessonDAO.instance == null) {
            LessonDAO.instance = new LessonDAO();
        }
        return LessonDAO.instance;
    }

    public List<LessonModel> getList(String search) {
        List<LessonModel> list = new ArrayList<>();
        String sql = "Select * from "+DB_TABLE_NAME+" where  "+search;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                LessonModel c = new LessonModel();
                c.setName(rs.getString("Name"));
                c.setDescription(rs.getString("Description"));
                c.setNote(rs.getString("Note"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                c.setUnitId(rs.getInt("UnitId"));
                c.setTotalMark(QuestionDAO.getInstance().getMarkByLesson(c.getId()));
                list.add(c);
            }
            rs.close();
        } catch (Exception ex) {

        }
        return list;
    }
    public LessonModel getDetail(int id) {
        String sql = "Select * from "+DB_TABLE_NAME+" where id= "+id;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                LessonModel c = new LessonModel();
                c.setName(rs.getString("Name"));
                c.setDescription(rs.getString("Description"));
                c.setNote(rs.getString("Note"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                c.setUnitId(rs.getInt("UnitId"));
                c.setTotalMark(QuestionDAO.getInstance().getMarkByLesson(c.getId()));
                return c;
            }
            rs.close();
        } catch (Exception ex) {

        }
        return null;
    }
}

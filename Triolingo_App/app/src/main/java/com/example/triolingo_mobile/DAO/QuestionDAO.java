package com.example.triolingo_mobile.DAO;

import android.util.Log;

import com.example.triolingo_mobile.DataAccess.DbContext;
import com.example.triolingo_mobile.Model.Exercise;
import com.example.triolingo_mobile.Model.LessonModel;
import com.example.triolingo_mobile.Model.Question;
import com.example.triolingo_mobile.Model.StudentCourse;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO extends DbContext {

    private static QuestionDAO instance;
    private static String DB_TABLE_NAME="Question";

    public static QuestionDAO getInstance() {
        if (QuestionDAO.instance == null) {
            QuestionDAO.instance = new QuestionDAO();
        }
        return QuestionDAO.instance;
    }

    public ArrayList<Question> getQuestionsByExId(int exId,String search){
        ArrayList<Question> list = new ArrayList<>();
        String sql = "select * from " +  DB_TABLE_NAME+ " where ExerciseId = " + exId +(search!=null&&search.trim()!=""?(" AND "+search):"");
        ResultSet rs = getData(sql);
        try{
            while (rs.next()){
                Question e = new Question();
                e.setId(rs.getInt("Id"));
                e.setStatus(rs.getInt("Status"));
                e.setQuestion1(rs.getString("Question1"));
                e.setExerciseId(rs.getInt("ExerciseId"));
                e.setMark(rs.getInt("Mark"));
                list.add(e);
            }
            rs.close();
        }
        catch (Exception ex) {
            Log.d("sql error", "error in ExerciseDAO");
        }
        return list;
    }

    public int getMarkByLesson(int lessonid){
        Integer sum= 0;
        String sql ="select sum(Mark) as Mark from "+DB_TABLE_NAME+" where ExerciseId in (select id from Exercise where LessonId="+lessonid+" AND STATUS>0) AND STATUS>0";
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                sum = rs.getInt("Mark");
            }
            rs.close();
        } catch (Exception ex) {

        }

        return sum;
    }
}

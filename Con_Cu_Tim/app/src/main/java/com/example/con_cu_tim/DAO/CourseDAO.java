package com.example.con_cu_tim.DAO;

import android.os.Handler;
import android.os.Looper;

import com.example.con_cu_tim.DataAccess.DbContext;
import com.example.con_cu_tim.Model.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseDAO extends DbContext {

    private static CourseDAO instance;
    private static final String DB_TABLE_NAME = "Course";
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static CourseDAO getInstance() {
        if (instance == null) {
            instance = new CourseDAO();
        }
        return instance;
    }

    public List<Course> getTop3CoursesByStudentId(int studentId) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT TOP 3 c.* FROM Course c " +
                "JOIN StudentCourse sc ON c.Id = sc.CourseId " +
                "WHERE sc.StudentId = " + studentId +
                " ORDER BY sc.Id DESC";

        try (ResultSet rs = getData(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<Course> getList(String search) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + search;

        try (ResultSet rs = getData(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Course getDetail(int id) {
        String sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE id = " + id;
        try (ResultSet rs = getData(sql)) {
            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public interface OnCourseDetailListener {
        void onCourseLoaded(Course course);
    }

    public void getDetailAsync(int id, OnCourseDetailListener listener) {
        executor.execute(() -> {
            Course course = getDetail(id);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (listener != null) {
                    listener.onCourseLoaded(course);
                }
            });
        });
    }

    // 🔹 Hàm tiện ích để ánh xạ dữ liệu từ ResultSet vào Course
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getInt("Id"));
        c.setName(rs.getString("Name"));
        c.setDescription(rs.getString("Description"));
        c.setNote(rs.getString("Note"));
        c.setRateAverage(rs.getFloat("RateAverage"));
        c.setStatus(rs.getInt("Status"));
        return c;
    }
}

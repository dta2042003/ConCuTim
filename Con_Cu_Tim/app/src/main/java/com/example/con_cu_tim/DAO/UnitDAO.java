package com.example.con_cu_tim.DAO;

import com.example.con_cu_tim.DataAccess.DbContext;
import com.example.con_cu_tim.Model.UnitModel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO extends DbContext {

    private static UnitDAO instance;
    private static String DB_TABLE_NAME="Unit";

    public static UnitDAO getInstance() {
        if (UnitDAO.instance == null) {
            UnitDAO.instance = new UnitDAO();
        }
        return UnitDAO.instance;
    }

    public List<UnitModel> getList(String search) {
        List<UnitModel> list = new ArrayList<>();
        String sql = "Select * from "+DB_TABLE_NAME+" where  "+search;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                UnitModel c = new UnitModel();
                c.setName(rs.getString("Name"));
                c.setDescription(rs.getString("Description"));
                c.setNote(rs.getString("Note"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                c.setCourseId(rs.getInt("CourseId"));
                list.add(c);
            }
            rs.close();
        } catch (Exception ex) {

        }
        return list;
    }
    public UnitModel getDetail(int unitId) {
        String sql = "Select * from "+DB_TABLE_NAME+" where ID= "+unitId;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                UnitModel c = new UnitModel();
                c.setName(rs.getString("Name"));
                c.setDescription(rs.getString("Description"));
                c.setNote(rs.getString("Note"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                c.setCourseId(rs.getInt("CourseId"));
                return c;
            }
            rs.close();
        } catch (Exception ex) {

        }
        return null;
    }
}

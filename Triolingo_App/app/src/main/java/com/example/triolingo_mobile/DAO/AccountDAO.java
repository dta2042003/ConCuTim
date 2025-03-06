package com.example.triolingo_mobile.DAO;

import android.util.Log;

import com.example.triolingo_mobile.DataAccess.DbContext;
import com.example.triolingo_mobile.Model.AccountModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends DbContext {

    private static AccountDAO instance;
    private static String DB_TABLE_NAME = "[User]";

    public static AccountDAO getInstance() {
        if (AccountDAO.instance == null) {
            AccountDAO.instance = new AccountDAO();
        }
        return AccountDAO.instance;
    }

    public List<AccountModel> getList(String search) {
        List<AccountModel> list = new ArrayList<>();
        String sql = "select * from "+DB_TABLE_NAME+" where "+search;
        ResultSet rs = getData(sql);
        try {
            while (rs.next()) {
                AccountModel c = new AccountModel();
                c.setEmail(rs.getString("Email"));
                c.setAvatarUrl(rs.getString("AvatarUrl"));
                c.setNote(rs.getString("Note"));
                c.setFullName(rs.getString("FullName"));
                c.setStatus(rs.getInt("Status"));
                c.setId(rs.getInt("Id"));
                list.add(c);
            }
            rs.close();
        } catch (Exception ex) {

        }
        return list;
    }

    public boolean registerAccount (AccountModel acc) {
        String sql = "INSERT INTO " + DB_TABLE_NAME + " VALUES (N'" + acc.getFullName() + "', '" +
                                    acc.getEmail() + "', '" + acc.getPassword() + "', " + (acc.getAvatarUrl() == null ? "null" : "'" + acc.getAvatarUrl() + "'") +
                                    ", " + acc.getStatus() + ", " + (acc.getNote() == null ? "null" : "'" + acc.getNote() + "'") + ")";
        try {
            return executeUpdate(sql) > 0;
        } catch (SQLException e) {
            Log.e("Error:", e.getMessage() );
        }
        return false;
    }

    int executeUpdate(String sql) throws SQLException {
        Statement stm = conn.createStatement();
        return stm.executeUpdate(sql);
    }
}

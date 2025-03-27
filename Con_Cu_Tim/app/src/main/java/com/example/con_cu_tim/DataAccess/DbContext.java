package com.example.con_cu_tim.DataAccess;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbContext {

    private final String connectionURL = "jdbc:jtds:sqlserver://172.16.0.244:1433;databasename=ConCuTim;user=sa;password=123456;";

    public Connection conn = ConnectionClass();

    public ResultSet getData(String sql) {
        try {
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return stm.executeQuery(sql);
        } catch (Exception e) {
            Log.e("DbContext Error", "Error executing query: ", e);
            return null;
        }
    }

    public int countRows(String table, String search) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE " + search;
        ResultSet rs = getData(sql);
        try {
            if (rs != null && rs.next()) {
                count = rs.getInt(1);
            }
            if (rs != null) rs.close();
        } catch (Exception ex) {
            Log.e("DbContext Error", "Error counting rows: ", ex);
        }
        return count;
    }

    public Connection ConnectionClass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = DriverManager.getConnection(connectionURL);
            Log.i("DB Connection", "✅ Kết nối cơ sở dữ liệu thành công!");
            return con;
        } catch (Exception ex) {
            Log.e("DB Connection Error", "❌ Lỗi kết nối DB: ", ex);
            return null;
        }
    }
}
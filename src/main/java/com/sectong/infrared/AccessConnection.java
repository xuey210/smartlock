package com.sectong.infrared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by xueyong on 16/9/8.
 * mobileeasy.
 */

public class AccessConnection {

    private static final String CONNECTION = "jdbc:ucanaccess:///alidata/server/infrared/db/BRANDS.mdb";
//    private static final String CONNECTION = "jdbc:ucanaccess:///Users/xueyong/Downloads/BRANDS.mdb";

    public AccessConnection() {

    }

    public ResultSet getResult(final String sql) {
        ResultSet rs = null;
        try {
            Connection conn = DriverManager.getConnection(CONNECTION);
            Statement s = conn.createStatement();
            rs = s.executeQuery(sql);
//            rs.close();
            s.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}

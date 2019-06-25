package com.qf.utils;

import com.alibaba.druid.pool.GetConnectionTimeoutException;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;


/**
 * @author blxf
 * @Date ${Date}
 */
public class GetCOnnection {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://120.77.154.118:3306/v13?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false", "blxf", "457862154");
        return connection;
    }

    public static void save(Connection connection, TProduct tProduct) throws SQLException {
        String sql = "insert into t_product(id,name,price,sale_price,image,sale_point,type_id,type_name,flag,create_time) values (?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setLong(1, tProduct.getId());
        pstm.setString(2, tProduct.getName());
        pstm.setBigDecimal(3, tProduct.getPrice());
        pstm.setBigDecimal(4, tProduct.getSalePrice());
        pstm.setString(5, tProduct.getImage());
        pstm.setString(6, tProduct.getSalePoint());
        pstm.setLong(7, tProduct.getTypeId());
        pstm.setString(8, tProduct.getTypeName());
        pstm.setBoolean(9, true);
        pstm.setDate(10, new java.sql.Date(System.currentTimeMillis()));
        System.out.println(pstm.executeUpdate());

    }

    public static void save(Connection connection, TProductType tProductType) throws SQLException {
        System.out.println("开启存储");
        String sql = "insert into t_product_type(id,pid,name" +
                ",flag,create_time) values (?,?,?,?,?)";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setLong(1, tProductType.getId());
        pstm.setLong(2, tProductType.getPid());
        pstm.setString(3, tProductType.getName());
        pstm.setBoolean(4, true);
        pstm.setDate(5, new java.sql.Date(System.currentTimeMillis()));
        System.out.println(pstm.executeUpdate());

    }
}

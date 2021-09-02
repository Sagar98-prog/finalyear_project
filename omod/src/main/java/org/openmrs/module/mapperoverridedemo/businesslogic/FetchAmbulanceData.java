package org.openmrs.module.mapperoverridedemo.businesslogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FetchAmbulanceData {

    public static String VEHICLE_DB = "vehicle_lut";
    public static String HOSPITAL_DB = "hospital_lut";
    public static String PARAMEDIC_DB = "paramedics";
    private static String DB_URL = "jdbc:mysql://localhost:3306/";
    private static String USER_NAME = "root";
    private static String PASSWORD = "toor";

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    public void connect_to_db(String db) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection(DB_URL+db, USER_NAME, PASSWORD);
    }

    /*

    public void connect_to_vehicledb() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection(DB_URL+VEHICLE_DB, USER_NAME, PASSWORD);
    }
    public void connect_to_hospitaldb() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection(DB_URL+HOSPITAL_DB, USER_NAME, PASSWORD);
    }
    public void connect_to_paramedicdb() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection(DB_URL+PARAMEDIC_DB, USER_NAME, PASSWORD);
    }
     */



    public void close_db_connection() throws SQLException {
        connect.close();
    }

    public String get_vehicle_data(String vehicle_reg) throws SQLException {

        connect.prepareStatement("use "+ VEHICLE_DB).executeQuery();


        String QUERY = "select * from vehicle_records where vehicle_reg_no = (?)";
        PreparedStatement preparedStatement = connect.prepareStatement(QUERY);
        preparedStatement.setString(1, vehicle_reg);
        resultSet = preparedStatement.executeQuery();
        if(!resultSet.next())
            return "";
        String vehicle_no = resultSet.getString("vehicle_reg_no");
        String vehicle_location = resultSet.getString("vehicle_location");
        String vehicle_type = String.valueOf(resultSet.getInt("vehicle_type"));
        return vehicle_no + ":" + vehicle_location + ":" + vehicle_type;
    }

    public String get_hospital_data(String HospitalName) throws SQLException {

        connect.prepareStatement("use "+ HOSPITAL_DB).executeQuery();

        String QUERY = "select * from hospitals where hid = (?)";
        PreparedStatement preparedStatement = connect.prepareStatement(QUERY);
        preparedStatement.setString(1, HospitalName);
        resultSet = preparedStatement.executeQuery();
        if(!resultSet.next())
            return "";
        return resultSet.getString("hid");
    }

    public String get_paramedics_data(String paramId) throws SQLException {
        connect.prepareStatement("use "+PARAMEDIC_DB).executeQuery();

        String QUERY = "select * from paramedic_lists where pid = (?)";
        PreparedStatement preparedStatement = connect.prepareStatement(QUERY);
        preparedStatement.setString(1, paramId);
        resultSet = preparedStatement.executeQuery();
        if(!resultSet.next())
            return "";

        return resultSet.getString("pid");

    }

}

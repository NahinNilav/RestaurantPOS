package org.example.restaurant_management_system.Controllers;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.example.restaurant_management_system.Model.Database;


public class DashboardController {

    @FXML
    public Label dashboard_NC;

    @FXML
    public Label dashboard_TI;

    @FXML
    public Label dashboard_TotalI;

    @FXML
    public Label dashboard_NSP;

    @FXML
    public AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    public BarChart<?, ?> dashboard_CustomerChart;

    public Connection connect;
    public PreparedStatement prepare;
    public Statement statement;
    public ResultSet result;
    public Alert alert;


    public void dashboardDisplayNC() {

        String sql = "SELECT COUNT(id) FROM receipt";
        connect = (Connection) Database.connectDB();

        try {
            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt("COUNT(id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayTI() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(total) FROM receipt WHERE date = '"
                + sqlDate + "'";

        connect = (Connection) Database.connectDB();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble("SUM(total)");
            }

            dashboard_TI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT SUM(total) FROM receipt";

        connect = (Connection) Database.connectDB();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("SUM(total)");
            }
            dashboard_TotalI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dashboardTotalSoldProducts() {
        String sql = "SELECT SUM(quantity) FROM customer";

        try {
            connect = Database.connectDB();
            int totalQuantity = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                totalQuantity = result.getInt("SUM(quantity)");
            }
            dashboard_NSP.setText(String.valueOf(totalQuantity));

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = (Connection) Database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart(){
        dashboard_CustomerChart.getData().clear();

        String sql = "SELECT date, COUNT(id) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = (Connection) Database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboard_CustomerChart.getData().add(chart);

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
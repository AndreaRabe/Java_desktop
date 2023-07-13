package com.example.employeemanagement;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.embed.swing.SwingFXUtils;


import javax.imageio.ImageIO;

public class Dashboard implements Initializable {

    @FXML
    private AnchorPane work_anchor;

    @FXML
    private AnchorPane enterprise_anchor;

    @FXML
    private Button enterprise_pdfBtn;

    @FXML
    private AnchorPane employee_anchor;

    @FXML
    private Button employee_pdfBtn;

    @FXML
    private PieChart moneyPieChart;

    @FXML
    private AnchorPane employee_form;

    @FXML
    private AnchorPane money_anchor;

    @FXML
    private Button pdf;

    @FXML
    private AnchorPane piechart_form;

    @FXML
    private AnchorPane enterprise_form;

    @FXML
    private AnchorPane work_form;

    @FXML
    private Button close;

    @FXML
    private Button piechart_btn;

    @FXML
    private Button employee_addBtn;

    @FXML
    private TextField employee_address;

    @FXML
    private Button employee_btn;

    @FXML
    private Button employee_clearBtn;

    @FXML
    private  TableColumn<employeeData, String> employee_col_tools;

    @FXML
    private TableColumn<employeeData, String> employee_col_address;

    @FXML
    private TableColumn<employeeData, String> employee_col_name;

    @FXML
    private TableColumn<employeeData, String> employee_col_numEmployee;

    @FXML
    private Button employee_deleteBtn;

    @FXML
    private TextField employee_name;

    @FXML
    private TextField employee_numEmployee;

    @FXML
    private TextField employee_search;

    @FXML
    private TableView<employeeData> employee_tableView;

    @FXML
    private Button employee_updateBtn;

    @FXML
    private Button enterprise_addBtn;

    @FXML
    private Button enterprise_clearBtn;

    @FXML
    private TableColumn<enterpriseData, String> enterprise_col_designation;

    @FXML
    private TableColumn<enterpriseData, String> enterprise_col_numEnterprise;

    @FXML
    private Button enterprise_deleteBtn;

    @FXML
    private TextField enterprise_designation;

    @FXML
    private TextField enterprise_numEnterprise;

    @FXML
    private TextField enterprise_search;

    @FXML
    private TableView<enterpriseData> enterprise_tableView;

    @FXML
    private Button enterprise_updateBtn;

    @FXML
    private Button enterprise_btn;

    @FXML
    private TableColumn<workData, String> money_col_name;

    @FXML
    private TableColumn<workData, String> money_col_numEmployee;

    @FXML
    private TableColumn<workData, String> money_col_salary;

    @FXML
    private TableColumn<workData, String> money_col_date;

    @FXML
    private AnchorPane money_form;

    @FXML
    private TextField money_search;

    @FXML
    private TableView<workData> money_tableView;

    @FXML
    private ComboBox<?> money_year;

    @FXML
    private TextField money_totalSalary;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button work_addBtn;

    @FXML
    private Button work_btn;

    @FXML
    private Button money_btn;

    @FXML
    private Button work_clearBtn;

    @FXML
    private TextField work_enterprise;

    @FXML
    private TextField work_designation;

    @FXML
    private TextField work_year;

    @FXML
    private TableColumn<workData, String> work_col_address;

    @FXML
    private TableColumn<workData, String> work_col_name;

    @FXML
    private TableColumn<workData, String> work_col_numEmployee;

    @FXML
    private TableColumn<workData, String> work_col_salary;

    @FXML
    private Button work_deleteBtn;

    @FXML
    private TextField work_hour;

    @FXML
    private TextField work_hourSalary;

    @FXML
    private ComboBox<String> work_numEmployee;

    @FXML
    private ComboBox<String> work_numEnterprise;

    @FXML
    private TextField work_search;

    @FXML
    private TableView<workData> work_tableView;

    @FXML
    private TextField work_totalEmployee;

    @FXML
    private TextField work_totalSalary;

    @FXML
    private Button work_updateBtn;


    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    // PieChart manipulation
    public void pieChart() {
        moneyList = moneyListData();
        money_tableView.setItems(moneyList);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (workData item : moneyList) {
            double totalSalary = item.getSalary() * 12;
            String formattedValue = String.format("%.0f", totalSalary);
            pieChartData.add(new PieChart.Data(item.getName(), Double.parseDouble(formattedValue)));
        }

        moneyPieChart.setData(pieChartData);
    }

    ///

    // money manipulation

    public ObservableList<workData> moneyListData() {
        ObservableList<workData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM work_data";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                workData moneyD = new workData(
                        result.getString("numEmployee"),
                        result.getString("name"),
                        result.getString("address"),
                        result.getDouble("salary"),
                        result.getDate("date"),
                        result.getString("numEnterprise"),
                        result.getString("designation"));

                listData.add(moneyD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<workData> moneyList;

    public void moneySearch() {
        FilteredList<workData> filter = new FilteredList<>(moneyList, e -> true);

        money_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateWorkData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                String numEnterprise = predicateWorkData.getNumEnterprise().toLowerCase();
                String designation = predicateWorkData.getDesignation().toLowerCase();
                String date = String.valueOf(predicateWorkData.getDate());

                String numEmployee = predicateWorkData.getNumEmployee().toLowerCase();
                String name = predicateWorkData.getName().toLowerCase();
                String address = predicateWorkData.getAddress().toLowerCase();
                String salary = String.valueOf(predicateWorkData.getSalary());

                return numEnterprise.contains(searchKey) ||
                        designation.contains(searchKey) ||
                        numEmployee.contains(searchKey) ||
                        name.contains(searchKey) ||
                        address.contains(searchKey) ||
                        salary.contains(searchKey) ||
                        date.contains(searchKey);
            });
        });

        SortedList<workData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(money_tableView.comparatorProperty());
        money_tableView.setItems(sortList);
        moneyTotalSalary();
    }


    public void moneyShowList(){
        moneyList = moneyListData();

        money_col_numEmployee.setCellValueFactory(new PropertyValueFactory<>("numEmployee"));
        money_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        money_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        money_col_salary.setCellValueFactory(cellData -> {
            double totalSalary = cellData.getValue().getSalary() * 12;
            String formattedValue = String.format("%.0f", totalSalary);
            return new SimpleStringProperty(formattedValue);
        });

        money_tableView.setItems(moneyList);
    }

    ////

    // work manipulation

    public void workAdd() {
        String sql = "INSERT INTO work_data" +
                "(numEmployee, name, address, salary, date, numEnterprise, designation)" +
                "VALUES(?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (work_numEmployee.getSelectionModel().getSelectedItem() == null ||
                    work_numEnterprise.getSelectionModel().getSelectedItem() == null ||
                    work_hour.getText().isEmpty() || work_hourSalary.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String check = "SELECT numEmployee FROM work_data WHERE numEmployee = ?";
                prepare = connect.prepareStatement(check);
                prepare.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee num : " + work_numEmployee.getSelectionModel().getSelectedItem() + " already exists!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                    prepare.setString(6, work_numEnterprise.getSelectionModel().getSelectedItem());

                    String employeeSql = "SELECT name, address FROM employee_data WHERE numEmployee = ?";
                    PreparedStatement employeeStatement = connect.prepareStatement(employeeSql);
                    employeeStatement.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                    ResultSet employeeResult = employeeStatement.executeQuery();

                    if (employeeResult.next()) {
                        String name = employeeResult.getString("name");
                        String address = employeeResult.getString("address");
                        prepare.setString(2, name);
                        prepare.setString(3, address);
                    }

                    String hourText = work_hour.getText();
                    String hourSalaryText = work_hourSalary.getText();

                    try {
                        double workHour = Double.parseDouble(hourText);
                        double workHourSalary = Double.parseDouble(hourSalaryText);
                        double salary = workHour * workHourSalary * 24;
                        prepare.setString(4, String.valueOf(salary));
                    } catch (NumberFormatException e) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error message");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid hour or hour salary value. Please enter numeric values.");
                        alert.showAndWait();
                        return;
                    }

                    java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                    prepare.setDate(5, currentDate);

                    String enterpriseSql = "SELECT designation FROM enterprise_data WHERE numEnterprise = ?";
                    PreparedStatement enterpriseStatement = connect.prepareStatement(enterpriseSql);
                    enterpriseStatement.setString(1, work_numEnterprise.getSelectionModel().getSelectedItem());
                    ResultSet enterpriseResult = enterpriseStatement.executeQuery();

                    if (enterpriseResult.next()) {
                        String designation = enterpriseResult.getString("designation");
                        prepare.setString(7, designation);
                    }

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    workShowList();
                    workReset();
                    workTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void workDelete() {
        String numEmployee = work_numEmployee.getSelectionModel().getSelectedItem();
        String numEnterprise = work_numEnterprise.getSelectionModel().getSelectedItem();

        if (work_numEmployee.getSelectionModel().getSelectedItem() == null || work_numEnterprise.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String sql = "DELETE FROM work_data WHERE numEmployee = ? AND numEnterprise = ?";
            connect = database.connectDb();

            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE employee with num: " + numEmployee + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setString(1, numEmployee);
                    statement.setString(2, numEnterprise);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        workShowList();
                        workReset();
                        workTotal();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error message");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Information incorrect. Employee not found.");
                        errorAlert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void workUpdate() {
        String sql = "UPDATE work_data SET numEmployee = ?, name = ?, address = ?, salary = ?, numEnterprise = ?, designation = ? WHERE numEmployee = ?";
        connect = database.connectDb();

        try {
            Alert alert;
            if (work_numEmployee.getSelectionModel().getSelectedItem() == null ||
                    work_numEnterprise.getSelectionModel().getSelectedItem() == null ||
                    work_hour.getText().isEmpty() || work_hourSalary.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkEmployee = "SELECT numEmployee FROM employee_data WHERE numEmployee = ?";
                prepare = connect.prepareStatement(checkEmployee);
                prepare.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                result = prepare.executeQuery();

                if (!result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee num : " + work_numEmployee.getSelectionModel().getSelectedItem() + " does not exist!");
                    alert.showAndWait();
                } else {
                    // Check if numEmployee exists in work_data table
                    String checkNumEmployee = "SELECT numEmployee FROM work_data WHERE numEmployee = ?";
                    prepare = connect.prepareStatement(checkNumEmployee);
                    prepare.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        // numEmployee exists, execute SQL update statement
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                        prepare.setString(5, work_numEnterprise.getSelectionModel().getSelectedItem());

                        String employeeSql = "SELECT name, address FROM employee_data WHERE numEmployee = ?";
                        PreparedStatement employeeStatement = connect.prepareStatement(employeeSql);
                        employeeStatement.setString(1, work_numEmployee.getSelectionModel().getSelectedItem());
                        ResultSet employeeResult = employeeStatement.executeQuery();

                        if (employeeResult.next()) {
                            String name = employeeResult.getString("name");
                            String address = employeeResult.getString("address");
                            prepare.setString(2, name);
                            prepare.setString(3, address);
                        }

                        String hourText = work_hour.getText();
                        String hourSalaryText = work_hourSalary.getText();

                        try {
                            double workHour = Double.parseDouble(hourText);
                            double workHourSalary = Double.parseDouble(hourSalaryText);
                            double salary = workHour * workHourSalary * 24;
                            prepare.setString(4, String.valueOf(salary));
                        } catch (NumberFormatException e) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error message");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid hour or hour salary value. Please enter numeric values.");
                            alert.showAndWait();
                            return;
                        }

                        String enterpriseSql = "SELECT designation FROM enterprise_data WHERE numEnterprise = ?";
                        PreparedStatement enterpriseStatement = connect.prepareStatement(enterpriseSql);
                        enterpriseStatement.setString(1, work_numEnterprise.getSelectionModel().getSelectedItem());
                        ResultSet enterpriseResult = enterpriseStatement.executeQuery();

                        if (enterpriseResult.next()) {
                            String designation = enterpriseResult.getString("designation");
                            prepare.setString(6, designation);
                        }
                        prepare.setString(7, work_numEmployee.getSelectionModel().getSelectedItem());

                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        workShowList();
                        workReset();
                        workTotal();
                    } else {
                        // numEmployee does not exist in work_data table, show error message
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error message");
                        alert.setHeaderText(null);
                        alert.setContentText("Employee num : " + work_numEmployee.getSelectionModel().getSelectedItem() + " does not exist!");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ObservableList<workData> workListData() {
        ObservableList<workData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM work_data";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                workData workD = new workData(
                        result.getString("numEmployee"),
                        result.getString("name"),
                        result.getString("address"),
                        result.getDouble("salary"),
                        result.getDate("date"),
                        result.getString("numEnterprise"),
                        result.getString("designation"));

                listData.add(workD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<workData> workList;

    public void workSearch() {
        FilteredList<workData> filter = new FilteredList<>(workList, e -> true);

        work_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateWorkData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                String numEnterprise = predicateWorkData.getNumEnterprise().toLowerCase();
                String designation = predicateWorkData.getDesignation().toLowerCase();
                String date = String.valueOf(predicateWorkData.getDate());

                String numEmployee = predicateWorkData.getNumEmployee().toLowerCase();
                String name = predicateWorkData.getName().toLowerCase();
                String address = predicateWorkData.getAddress().toLowerCase();
                String salary = String.valueOf(predicateWorkData.getSalary());

                return numEnterprise.contains(searchKey) ||
                        designation.contains(searchKey) ||
                        numEmployee.contains(searchKey) ||
                        name.contains(searchKey) ||
                        address.contains(searchKey) ||
                        salary.contains(searchKey) ||
                        date.contains(searchKey);
            });
        });

        SortedList<workData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(work_tableView.comparatorProperty());
        work_tableView.setItems(sortList);
    }


    public void workShowList(){
        workList = workListData();

        work_col_numEmployee.setCellValueFactory(new PropertyValueFactory<>("numEmployee"));
        work_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        work_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        work_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        work_tableView.setItems(workList);
    }

    public void workSelect() {
        workData workD = work_tableView.getSelectionModel().getSelectedItem();

        if (workD != null) {
            String selectedNumEmployee = workD.getNumEmployee();
            work_numEmployee.getSelectionModel().select(selectedNumEmployee);

            String employeeSql = "SELECT numEnterprise, designation, date FROM work_data WHERE numEmployee = ?";
            try {
                Connection connection = database.connectDb();
                PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
                employeeStatement.setString(1, selectedNumEmployee);
                ResultSet employeeResult = employeeStatement.executeQuery();

                if (employeeResult.next()) {
                    String numEnterprise = employeeResult.getString("numEnterprise");
                    String designation = employeeResult.getString("designation");
                    String date = String.valueOf(employeeResult.getDate("date"));
                    work_numEnterprise.getSelectionModel().select(numEnterprise);
                    work_enterprise.setText(numEnterprise);
                    work_year.setText(date);
                    work_designation.setText(designation);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            work_hourSalary.setText("");
            work_hour.setText("");
        }
    }


    ////


    // enterprise manipulation

    public void enterpriseAdd(){

        String sql = "INSERT INTO enterprise_data" +
                "(numEnterprise, designation)" +
                "VALUES(?,?)";

        connect = database.connectDb();

        try{
            Alert alert;
            if(enterprise_numEnterprise.getText().isEmpty() || enterprise_designation.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            }else {

                String check = "SELECT numEnterprise FROM enterprise_data WHERE numEnterprise = '"
                        +enterprise_numEnterprise.getText()+"'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Enterprise num : "+ enterprise_numEnterprise.getText() + " was already exist !");
                    alert.showAndWait();
                }
                else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, enterprise_numEnterprise.getText());
                    prepare.setString(2, enterprise_designation.getText());

                    prepare.executeUpdate();


                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added !");
                    alert.showAndWait();

                    enterpriseShowList();
                    enterpriseReset();

                }
            }
        }catch (Exception e){ e.printStackTrace(); }


    }

    public void enterpriseDelete() {
        String numEnterprise = enterprise_numEnterprise.getText();
        String designation = enterprise_designation.getText();

        if (numEnterprise.isEmpty() || designation.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String sql = "DELETE FROM enterprise_data WHERE numEnterprise = ? AND designation = ?";
            connect = database.connectDb();

            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE enterprise with num: " + numEnterprise + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setString(1, numEnterprise);
                    statement.setString(2, designation);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        enterpriseShowList();
                        enterpriseReset();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error message");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Information incorrect. Enterprise not found.");
                        errorAlert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void enterpriseUpdate() {
        String numEnterprise = enterprise_numEnterprise.getText();
        String designation = enterprise_designation.getText();


        if (numEnterprise.isEmpty() || designation.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {

            if (!isEnterpriseExist(numEnterprise)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Enterprise number. Enterprise not found !");
                alert.showAndWait();
            } else {
                String sql = "UPDATE enterprise_data SET numEnterprise = ?, designation = ? WHERE numEnterprise = ?";
                connect = database.connectDb();

                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to UPDATE enterprise_num: " + numEnterprise + "?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        PreparedStatement statement = connect.prepareStatement(sql);
                        statement.setString(1, numEnterprise);
                        statement.setString(2, designation);
                        statement.setString(3, numEnterprise);
                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            enterpriseShowList();
                            enterpriseReset();
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error message");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Invalid enterprise number. Enterprise not found.");
                            errorAlert.showAndWait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isEnterpriseExist(String numEnterprise) {
        String sql = "SELECT COUNT(*) FROM enterprise_data WHERE numEnterprise = ?";
        connect = database.connectDb();

        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, numEnterprise);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void enterpriseSearch() {

        FilteredList<enterpriseData> filter = new FilteredList<>(enterpriseList, e -> true);

        enterprise_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateEnterpriseData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();
                String numEnterprise = predicateEnterpriseData.getNumEnterprise().toLowerCase();
                String designation = predicateEnterpriseData.getDesignation().toLowerCase();

                return numEnterprise.contains(searchKey) || designation.contains(searchKey);
            });
        });

        SortedList<enterpriseData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(enterprise_tableView.comparatorProperty());
        enterprise_tableView.setItems(sortList);
    }

    public ObservableList<enterpriseData> enterpriseListData(){

        ObservableList<enterpriseData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * from enterprise_data";

        connect = database.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            enterpriseData enterpriseD;

            while (result.next()){
                enterpriseD = new enterpriseData(
                        result.getString("numEnterprise"),
                        result.getString("designation"));
                listData.add(enterpriseD);
            }

        }catch (Exception e){ e.printStackTrace(); }
        return listData;
    }

    private ObservableList<enterpriseData> enterpriseList;

    public void enterpriseShowList(){
        enterpriseList = enterpriseListData();

        enterprise_col_numEnterprise.setCellValueFactory(new PropertyValueFactory<>("numEnterprise"));
        enterprise_col_designation.setCellValueFactory(new PropertyValueFactory<>("designation"));

        enterprise_tableView.setItems(enterpriseList);
    }

    public void enterpriseSelect(){
        enterpriseData enterpriseD = enterprise_tableView.getSelectionModel().getSelectedItem();

        if(enterpriseD != null) {
            enterprise_numEnterprise.setText(enterpriseD.getNumEnterprise());
            enterprise_designation.setText(enterpriseD.getDesignation());
        }
    }

    ////



    // employee manipulation

    public void employeeAdd(){

        String sql = "INSERT INTO employee_data" +
                "(numEmployee, name, address)" +
                "VALUES(?,?,?)";

        connect = database.connectDb();

        try{
            Alert alert;
            if(employee_numEmployee.getText().isEmpty() || employee_name.getText().isEmpty() || employee_address.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            }else {

                String check = "SELECT numEmployee FROM employee_data WHERE numEmployee = '"
                        +employee_numEmployee.getText()+"'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee num : "+ employee_numEmployee.getText() + " was already exist !");
                    alert.showAndWait();
                }
                else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, employee_numEmployee.getText());
                    prepare.setString(2, employee_name.getText());
                    prepare.setString(3, employee_address.getText());

                    prepare.executeUpdate();


                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added !");
                    alert.showAndWait();

                    employeeShowList();
                    employeeReset();

                }
            }
        }catch (Exception e){ e.printStackTrace(); }


    }

    public void employeeDelete() {
        String numEmployee = employee_numEmployee.getText();
        String name = employee_name.getText();
        String address = employee_address.getText();

        if (numEmployee.isEmpty() || name.isEmpty() || address.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String sql = "DELETE FROM employee_data WHERE numEmployee = ? AND name = ? AND address = ?";
            connect = database.connectDb();

            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE employee with num: " + numEmployee + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setString(1, numEmployee);
                    statement.setString(2, name);
                    statement.setString(3, address);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        employeeShowList();
                        employeeReset();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error message");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Information incorrect. Employee not found.");
                        errorAlert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void employeeUpdate() {
        String numEmployee = employee_numEmployee.getText();
        String name = employee_name.getText();
        String address = employee_address.getText();


        if (numEmployee.isEmpty() || name.isEmpty() || address.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {

            if (!isEmployeeExist(numEmployee)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid employee number. Employee not found !");
                alert.showAndWait();
            } else {
                String sql = "UPDATE employee_data SET numEmployee = ?, name = ?, address = ? WHERE numEmployee = ?";
                connect = database.connectDb();

                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to UPDATE employee_ID: " + numEmployee + "?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        PreparedStatement statement = connect.prepareStatement(sql);
                        statement.setString(1, numEmployee);
                        statement.setString(2, name);
                        statement.setString(3, address);
                        statement.setString(4, numEmployee);
                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            employeeShowList();
                            employeeReset();
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error message");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Invalid employee number. Employee not found.");
                            errorAlert.showAndWait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isEmployeeExist(String numEmployee) {
        String sql = "SELECT COUNT(*) FROM employee_data WHERE numEmployee = ?";
        connect = database.connectDb();

        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, numEmployee);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void employeeSearch() {

        FilteredList<employeeData> filter = new FilteredList<>(employeeList, e -> true);

        employee_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateEmployeeData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();
                String numEmployee = predicateEmployeeData.getNumEmployee().toLowerCase();
                String name = predicateEmployeeData.getName().toLowerCase();
                String address = predicateEmployeeData.getAddress().toLowerCase();

                return numEmployee.contains(searchKey) || name.contains(searchKey) || address.contains(searchKey);
            });
        });

        SortedList<employeeData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(employee_tableView.comparatorProperty());
        employee_tableView.setItems(sortList);
    }

    public ObservableList<employeeData> employeeListData(){

        ObservableList<employeeData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * from employee_data";

        connect = database.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            employeeData employeeD;

            while (result.next()){
                employeeD = new employeeData(
                        result.getString("numEmployee"),
                        result.getString("name"),
                        result.getString("address"));
                listData.add(employeeD);
            }

        }catch (Exception e){ e.printStackTrace(); }
        return listData;
    }

    private ObservableList<employeeData> employeeList;

    public void employeeShowList(){
        employeeList = employeeListData();

        employee_col_numEmployee.setCellValueFactory(new PropertyValueFactory<>("numEmployee"));
        employee_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        employee_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));

        employee_tableView.setItems(employeeList);
    }

    public void employeeSelect(){
        employeeData employeeD = employee_tableView.getSelectionModel().getSelectedItem();

        if(employeeD != null) {
            employee_numEmployee.setText(employeeD.getNumEmployee());
            employee_name.setText(employeeD.getName());
            employee_address.setText(employeeD.getAddress());
        }
    }

    ////



    // edit and delete button on the tableView
/*    private Callback<TableColumn<employeeData, String>, TableCell<employeeData, String>> createCellFactory() {
        return param -> {
            final TableCell<employeeData, String> cell = new TableCell<employeeData, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = createDeleteIcon();
                        FontAwesomeIconView editIcon = createEditIcon();
                        configureDeleteIcon(deleteIcon);
                        configureEditIcon(editIcon);
                        configureDeleteIconOnClick(deleteIcon);
                        configureEditIconOnClick(editIcon);
                        HBox managebtn = createManageButtonBox(editIcon, deleteIcon);
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
    }

    private FontAwesomeIconView createDeleteIcon() {
        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteIcon.setStyle("-fx-cursor: hand ;" + "-glyph-size:28px;" + "-fx-fill:#ff1744;");
        return deleteIcon;
    }

    private FontAwesomeIconView createEditIcon() {
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
        editIcon.setStyle("-fx-cursor: hand ;" + "-glyph-size:28px;" + "-fx-fill:#00E676;");
        return editIcon;
    }

    private void configureDeleteIcon(FontAwesomeIconView deleteIcon) {
        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
            try {
                student = studentsTable.getSelectionModel().getSelectedItem();
                query = "DELETE FROM `student` WHERE id =" + student.getId();
                connection = DbConnect.getConnect();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                refreshTable();
            } catch (SQLException ex) {
                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void configureEditIcon(FontAwesomeIconView editIcon) {
        editIcon.setOnMouseClicked((MouseEvent event) -> {
            student = studentsTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/tableView/addStudent.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            AddStudentController addStudentController = loader.getController();
            addStudentController.setUpdate(true);
            addStudentController.setTextField(student.getId(), student.getName(), student.getBirth().toLocalDate(),
                    student.getAdress(), student.getEmail());
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        });
    }

    private void configureDeleteIconOnClick(FontAwesomeIconView deleteIcon) {
        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
    }

    private void configureEditIconOnClick(FontAwesomeIconView editIcon) {
        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
    }

    private HBox createManageButtonBox(FontAwesomeIconView editIcon, FontAwesomeIconView deleteIcon) {
        HBox managebtn = new HBox(editIcon, deleteIcon);
        managebtn.setStyle("-fx-alignment:center");
        return managebtn;
    }*/
    ////



    // simple function

    public void employeeAnchorPdf() {
        try {
            // Create a PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Convert AnchorPane to an image
                SnapshotParameters params = new SnapshotParameters();
                params.setTransform(Transform.scale(1.0, 1.0));
                WritableImage snapshot = employee_anchor.snapshot(params, null);

                // Save the image to a temporary file
                File tempFile = File.createTempFile("snapshot", ".png");
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
                ImageIO.write(bufferedImage, "png", tempFile);

                // Load the image as PDImageXObject
                PDImageXObject image = PDImageXObject.createFromFileByContent(tempFile, document);

                // Draw the image on the PDF
                contentStream.drawImage(
                        image,
                        30F, 350F, 552F, 412F
                );

                // Get the current date and time
                java.util.Date currentDate = new java.util.Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Add text to the PDF
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(30, 770); // Modify the coordinates for the text position
                contentStream.showText("Management System, employee information " + dateFormat.format(currentDate));
                contentStream.endText();
            }

            // Random number
            int min = 1;
            int max = 1000;

            java.util.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            Random random = new Random();
            int randomNumber = random.nextInt(max - min + 1) + min;

            // Generate the filename with the random number

            // Check if the file already exists
            File outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_employee.pdf");
            while (outputFile.exists()) {
                // Generate a new random number and update the filename
                randomNumber = random.nextInt(max - min + 1) + min;
                outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_employee_" + randomNumber + ".pdf");
            }

            // Save the PDF document with the updated filename
            document.save(outputFile);
            System.out.println("PDF with AnchorPane generated successfully!");

            // Close the PDF document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterpriseAnchorPdf() {
        try {
            // Create a PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Convert AnchorPane to an image
                SnapshotParameters params = new SnapshotParameters();
                params.setTransform(Transform.scale(1.0, 1.0));
                WritableImage snapshot = enterprise_anchor.snapshot(params, null);

                // Save the image to a temporary file
                File tempFile = File.createTempFile("snapshot", ".png");
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
                ImageIO.write(bufferedImage, "png", tempFile);

                // Load the image as PDImageXObject
                PDImageXObject image = PDImageXObject.createFromFileByContent(tempFile, document);

                // Draw the image on the PDF
                contentStream.drawImage(
                        image,
                        30F, 350F, 552F, 412F
                );

                // Get the current date and time
                java.util.Date currentDate = new java.util.Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Add text to the PDF
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(30, 770); // Modify the coordinates for the text position
                contentStream.showText("Management System, enterprise information " + dateFormat.format(currentDate));
                contentStream.endText();
            }

            // Random number
            int min = 1;
            int max = 1000;

            java.util.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            Random random = new Random();
            int randomNumber = random.nextInt(max - min + 1) + min;

            // Generate the filename with the random number

            // Check if the file already exists
            File outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_enterprise.pdf");
            while (outputFile.exists()) {
                // Generate a new random number and update the filename
                randomNumber = random.nextInt(max - min + 1) + min;
                outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_enterprise_" + randomNumber + ".pdf");
            }

            // Save the PDF document with the updated filename
            document.save(outputFile);
            System.out.println("PDF with AnchorPane generated successfully!");
            // Close the PDF document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void workAnchorPdf() {
        try {
            // Create a PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Convert AnchorPane to an image
                SnapshotParameters params = new SnapshotParameters();
                params.setTransform(Transform.scale(1.0, 1.0));
                WritableImage snapshot = work_anchor.snapshot(params, null);

                // Save the image to a temporary file
                File tempFile = File.createTempFile("snapshot", ".png");
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
                ImageIO.write(bufferedImage, "png", tempFile);

                // Load the image as PDImageXObject
                PDImageXObject image = PDImageXObject.createFromFileByContent(tempFile, document);

                // Draw the image on the PDF
                contentStream.drawImage(
                        image,
                        30F, 350F, 552F, 412F
                );

                // Get the current date and time
                java.util.Date currentDate = new java.util.Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Add text to the PDF
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(30, 770); // Modify the coordinates for the text position
                contentStream.showText("Management System, work information " + dateFormat.format(currentDate));
                contentStream.endText();
            }

            // Random number
            int min = 1;
            int max = 1000;

            java.util.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            Random random = new Random();
            int randomNumber = random.nextInt(max - min + 1) + min;

            // Generate the filename with the random number

            // Check if the file already exists
            File outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_work.pdf");
            while (outputFile.exists()) {
                // Generate a new random number and update the filename
                randomNumber = random.nextInt(max - min + 1) + min;
                outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_work_" + randomNumber + ".pdf");
            }

            // Save the PDF document with the updated filename
            document.save(outputFile);
            System.out.println("PDF with AnchorPane generated successfully!");

            // Close the PDF document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void anchorPdf() {
        try {
            // Create a PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Convert AnchorPane to an image
                SnapshotParameters params = new SnapshotParameters();
                params.setTransform(Transform.scale(1.0, 1.0));
                WritableImage snapshot = money_anchor.snapshot(params, null);

                // Save the image to a temporary file
                File tempFile = File.createTempFile("snapshot", ".png");
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
                ImageIO.write(bufferedImage, "png", tempFile);

                // Load the image as PDImageXObject
                PDImageXObject image = PDImageXObject.createFromFileByContent(tempFile, document);

                // Draw the image on the PDF
                contentStream.drawImage(
                        image,
                        30F, 350F, 552F, 412F
                );

                // Get the current date and time
                java.util.Date currentDate = new java.util.Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Add text to the PDF
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(30, 770); // Modify the coordinates for the text position
                contentStream.showText("Management System, money information " + dateFormat.format(currentDate)); // Add the text "Management System"
                contentStream.endText();
            }

            // Random number
            int min = 1;
            int max = 1000;

            java.util.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            Random random = new Random();
            int randomNumber = random.nextInt(max - min + 1) + min;

            // Generate the filename with the random number

            // Check if the file already exists
            File outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_money.pdf");
            while (outputFile.exists()) {
                // Generate a new random number and update the filename
                randomNumber = random.nextInt(max - min + 1) + min;
                outputFile = new File("E:/Documents/ENI L2/Projet_L3/Java_pdf/" + currentDate + "_money_" + randomNumber + ".pdf");
            }

            // Save the PDF document with the updated filename
            document.save(outputFile);
            System.out.println("PDF with AnchorPane generated successfully!");

            // Close the PDF document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void moneyTotalSalary() {
        double totalSalary = 0.0;

        TableColumn<workData, String> salaryColumn = money_col_salary;

        for (workData item : money_tableView.getItems()) {
            Double salary = Double.valueOf(salaryColumn.getCellData(item));
            totalSalary += salary;
        }
        String formattedValue = String.format("%.0f", totalSalary);
        money_totalSalary.setText(String.valueOf(formattedValue));

    }


    @FXML
    private void loadEnterpriseNumbers() {

        String sql = "SELECT numEnterprise FROM enterprise_data";
        connect = database.connectDb();

        try{
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                String numEnterprise = result.getString("numEnterprise");
                work_numEnterprise.getItems().add(numEnterprise);
            }
            result.close();
        } catch (SQLException e) {e.printStackTrace();}
    }

    @FXML
    private void loadEmployeeNumbers() {

        String sql = "SELECT numEmployee FROM employee_data";
        connect = database.connectDb();

        try{
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                String numEmployee = result.getString("numEmployee");
                work_numEmployee.getItems().add(numEmployee);
            }
            result.close();
        } catch (SQLException e) {e.printStackTrace();}
    }

    public void workTotal(){

        String sql1 = "SELECT COUNT(*) FROM work_data";
        String sql2 = "SELECT SUM(salary) FROM work_data";
        connect = database.connectDb();

        try{
            PreparedStatement statement = connect.prepareStatement(sql1);
            PreparedStatement statement1 = connect.prepareStatement(sql2);
            ResultSet resultSet = statement.executeQuery();
            ResultSet resultSet1 = statement1.executeQuery();

            if(resultSet.next() && resultSet1.next()){
                int count = resultSet.getInt(1);
                work_totalEmployee.setText(String.valueOf(count));
                double countSalary = resultSet1.getDouble(1);
                work_totalSalary.setText(String.valueOf(countSalary));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void workReset(){
        work_numEmployee.setValue(null);
        work_numEnterprise.setValue(null);
        work_hour.setText("");
        work_hourSalary.setText("");
    }

    public void employeeReset(){
        employee_numEmployee.setText("");
        employee_name.setText("");
        employee_address.setText("");
    }

    public void enterpriseReset(){
        enterprise_numEnterprise.setText("");
        enterprise_designation.setText("");
    }

    public void close (){System.exit(0);}

    public void minimize(){
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }


    private double x = 0;
    private double y = 0;

    public void logout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure, you want to logout ?");
        Optional<ButtonType> option = alert.showAndWait();

        try{
            if(option.get().equals(ButtonType.OK)){
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    }
                });

                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                        stage.setOpacity(.8);
                    }
                });

                root.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        stage.setOpacity(1);
                    }
                });


                // to hide the default button in window
                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    public void defaultNav() {
        money_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == work_btn) {
            work_form.setVisible(true);
            money_form.setVisible(false);
            enterprise_form.setVisible(false);
            employee_form.setVisible(false);
            piechart_form.setVisible(false);

            work_btn.setStyle("-fx-background-color : linear-gradient(to bottom right, #3a4368, #28966c);");
            money_btn.setStyle("-fx-background-color:transparent;");
            enterprise_btn.setStyle("-fx-background-color:transparent;");
            employee_btn.setStyle("-fx-background-color:transparent;");
            piechart_btn.setStyle("-fx-background-color:transparent;");

            loadEmployeeNumbers();
            loadEnterpriseNumbers();

            workShowList();

        } else if (event.getSource() == money_btn) {
            work_form.setVisible(false);
            money_form.setVisible(true);
            enterprise_form.setVisible(false);
            employee_form.setVisible(false);
            piechart_form.setVisible(false);

            money_btn.setStyle("-fx-background-color : linear-gradient(to bottom right, #3a4368, #28966c);");
            work_btn.setStyle("-fx-background-color:transparent;");
            enterprise_btn.setStyle("-fx-background-color:transparent;");
            employee_btn.setStyle("-fx-background-color:transparent;");
            piechart_btn.setStyle("-fx-background-color:transparent;");

            moneyShowList();
            moneyTotalSalary();
        } else if (event.getSource() == enterprise_btn) {
            work_form.setVisible(false);
            money_form.setVisible(false);
            enterprise_form.setVisible(true);
            employee_form.setVisible(false);
            piechart_form.setVisible(false);

            enterprise_btn.setStyle("-fx-background-color : linear-gradient(to bottom right, #3a4368, #28966c);");
            work_btn.setStyle("-fx-background-color:transparent;");
            money_btn.setStyle("-fx-background-color:transparent;");
            employee_btn.setStyle("-fx-background-color:transparent;");
            piechart_btn.setStyle("-fx-background-color:transparent;");

            enterpriseShowList();
        } else if (event.getSource() == employee_btn) {
            work_form.setVisible(false);
            money_form.setVisible(false);
            enterprise_form.setVisible(false);
            employee_form.setVisible(true);
            piechart_form.setVisible(false);

            employee_btn.setStyle("-fx-background-color : linear-gradient(to bottom right, #3a4368, #28966c);");
            work_btn.setStyle("-fx-background-color:transparent;");
            money_btn.setStyle("-fx-background-color:transparent;");
            enterprise_btn.setStyle("-fx-background-color:transparent;");
            piechart_btn.setStyle("-fx-background-color:transparent;");

            employeeShowList();
        } else if (event.getSource() == piechart_btn) {
            work_form.setVisible(false);
            money_form.setVisible(false);
            enterprise_form.setVisible(false);
            employee_form.setVisible(false);
            piechart_form.setVisible(true);

            piechart_btn.setStyle("-fx-background-color : linear-gradient(to bottom right, #3a4368, #28966c);");
            work_btn.setStyle("-fx-background-color:transparent;");
            money_btn.setStyle("-fx-background-color:transparent;");
            enterprise_btn.setStyle("-fx-background-color:transparent;");
            employee_btn.setStyle("-fx-background-color:transparent;");

            pieChart();
        }
    }


    ////

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pieChart();

        defaultNav();
        workTotal();

        employeeShowList();
        enterpriseShowList();
        workShowList();
        moneyShowList();

    }
}



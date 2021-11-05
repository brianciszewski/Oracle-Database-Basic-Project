package com.Application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

  private TextArea TextBox = new TextArea();
  private Button btShowJobs = new Button("Show Records");
  private ComboBox<String> cboTableName = new ComboBox<>();
  private Statement stmt;

  @Override
  public void start(Stage primaryStage) {
    // establish the database connection
    initializeDB();
    // display the JOB Data
    btShowJobs.setOnAction(e -> showData());
    HBox hBox = new HBox(30);
    hBox.getChildren().addAll(new Label("Table Name"), cboTableName, btShowJobs);
    hBox.setAlignment(Pos.CENTER);
    TextBox.setPrefWidth(745);
    TextBox.setPrefHeight(345);

    BorderPane bpane = new BorderPane();
    bpane.setCenter(new ScrollPane(TextBox));
    bpane.setTop(hBox);
    Scene scene = new Scene(bpane, 750, 375);
    primaryStage.setTitle("Dispaly JOB Information");
    primaryStage.setScene(scene);
    primaryStage.show();
  } // end method start

  private void initializeDB() {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/HR";
    Connection conn = null;

    try {
      //Add code that does the following
      //Create a connection to your Oracle database using the orcluser account
      //Use the connection to create a statement
      //Use the Database MetaData to generate a resultSet based on tables that
      // contain the word job
      // Add the returned table names to the comboBox, selecting the first item
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL);
      stmt = conn.createStatement();
      DatabaseMetaData dbmd = conn.getMetaData();
      ResultSet rsTable = dbmd.getTables(null, null,
          "JOB%", new String[]{"TABLE"});

      while (rsTable.next()){
          cboTableName.getItems().add(rsTable.getString("TABLE_NAME"));
      }
      /*
      cboTableName.getItems().add("JOBS");
      cboTableName.getItems().add("JOB_HISTORY");
      cboTableName.getItems().add("EMPLOYEES");
      cboTableName.getItems().add("DEPARTMENTS");
       */
    } catch (Exception ex) {
      ex.printStackTrace();
    } // end try catch
  } // end method initializeDB

  private void showData() {
    TextBox.clear();
    String tableName = cboTableName.getValue();
    try {
      //Add code that does the following
      //Create a connection to your Oracle database using the orcluser account
      //Use the connection to create a statement
      // Use the Database MetaData to generate a resultSet based on tables that
      // contain the word job
      // Add the returned table names to the comboBox, selecting the first item
      String sql = "SELECT * FROM " + tableName;
      ResultSet rs = stmt.executeQuery(sql);
      ResultSetMetaData rsmd = rs.getMetaData();
      int numberColumns = rsmd.getColumnCount();
      // get column names
      for (int i = 1; i <= numberColumns; i++) {
        TextBox.appendText(rsmd.getColumnName(i) + "    ");
      }
      TextBox.appendText("\n");



// gets column data
      while (rs.next()) {
        for (int i = 1; i <= numberColumns; i++) {
          TextBox.appendText(rs.getString(i) + "    ");

        }
        TextBox.appendText("\n");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } // end try catch
    // end method showData
  }

  public static void main(String[] args) {
    launch(args);
  } // end method main
} // end class DisplayJobs

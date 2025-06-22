package com.hasandag.performance.automated;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetricsExporter {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            
            File exportDir = new File("metrics-export");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            
            String dbUrl = "jdbc:h2:file:./test-results";
            Connection conn = DriverManager.getConnection(dbUrl, "sa", "");
            System.out.println("Connected to H2 database");
            
            String csvFilename = "metrics-export/test_results_" + timestamp + ".csv";
            PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFilename));
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM automated_test_results");
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) {
                csvWriter.print(metaData.getColumnName(i));
                if (i < columnCount) {
                    csvWriter.print(",");
                }
            }
            csvWriter.println();
            
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null) {
                        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
                            value = "\"" + value.replace("\"", "\"\"") + "\"";
                        }
                    } else {
                        value = "";
                    }
                    csvWriter.print(value);
                    if (i < columnCount) {
                        csvWriter.print(",");
                    }
                }
                csvWriter.println();
            }
            
            rs.close();
            stmt.close();
            conn.close();
            csvWriter.close();
            
            System.out.println("Exported " + rowCount + " records to " + csvFilename);
            
        } catch (Exception e) {
            System.err.println("Error exporting metrics: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
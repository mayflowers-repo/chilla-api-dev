package com.mayflowertech.chilla.config.custom;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PopulateDataFromFile {

	public static void main(String[] args) throws Exception {
	    if (args.length < 1) {
	        System.err.println("Please provide the CSV file path as a command-line argument.");
	        System.exit(1);
	    }
	    
	    String csvFilePath = args[0];  // First argument is the file path
	    conn = getConnection();

	    List<String> locations = readFileByLine(csvFilePath);  // Use the file path provided via command line
	    for (String location : locations) {
	        if (location.startsWith("pincode")) {
	            continue;
	        }

	        String[] values = location.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	        if (values.length != 15) {
	            System.err.println(values.length + " Invalid row: " + location);
	            continue; // Skip invalid rows
	        }

	        String poffice = values[0].trim();
	        String pincode = values[1].trim();
	        String officeType = values[2].trim();

	        if (poffice.endsWith(officeType)) {
	            poffice = poffice.substring(0, poffice.length() - officeType.length()).trim();
	        }

	        String name = poffice;
	        String districtName = values[8].trim();
	        String stateName = values[9].trim();

	        //insertLocation(conn, pincode, name, districtName, stateName);
	    }
	    System.out.println("--");
	}


	private static void insertLocation(Connection connection, String pincode, String name, String districtName, String stateName) {
		final String INSERT_QUERY = "INSERT INTO locations (pincode, name, district_name, state_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, pincode);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, districtName);
            preparedStatement.setString(4, stateName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to insert location (" + pincode + ", " + name + ", " + districtName + ", " + stateName + "): " + e.getMessage());
        }
    }
	  public static List<String> readFileByLine(String fileName) {
	        List<String> list = new ArrayList<>();
	        try {
	            File file = new File(fileName);
	            Scanner scanner = new Scanner(file);

	            // Read line by line
	            while (scanner.hasNextLine()) {
	                list.add(scanner.nextLine());
	            }
	            scanner.close();
	        } catch (FileNotFoundException e) {
	            System.err.println("File not found: " + fileName);
	            e.printStackTrace();
	        }

	        // Ensure list is not empty before accessing the first element
	        if (!list.isEmpty()) {
	            System.out.println("First line: " + list.get(0));
	        }
	        System.out.println("Total lines read: " + list.size());

	        return list;
	    }
	  
	private static Connection conn = null;

	public static Connection getConnection() throws SQLException {

		if (conn == null) {
			System.out.println("creating connection..");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/karuthaldev1", "cloner", "team123");
			//conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/karuthaldev1", "anania", "team123");			
			// conn =
			// DriverManager.getConnection("jdbc:postgresql://104.237.2.242:5432/myderric",
			// "cloner", "team123");

		}
		return conn;
	}

}

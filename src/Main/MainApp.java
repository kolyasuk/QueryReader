package Main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApp {
	private static int fileReaderIndex = 0;
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet res = null;

	public static void main(String[] args) {
		File file = new File("C:\\Users\\kolyasuk\\Desktop\\Query.txt");
		executeQuery(getQuery(file));
		System.out.println("----------------------------");
		executeQuery(getQuery(file));

	}

	private static void executeQuery(String query) {
		try {
			Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
			DriverManager.deregisterDriver(driver);
			
			String url = "jdbc:sqlite:C:\\Users\\kolyasuk\\Desktop\\CarShop_lesson10.db";
			
			con = DriverManager.getConnection(url);
			stmt = con.createStatement();
			
			res = stmt.executeQuery(query);
			while(res.next()) {
				System.out.println(res.getString("name_en")+" - "+res.getString("name_ru"));
			}
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getQuery(File file) {
		String query = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempQuery = reader.readLine();
			
			if (tempQuery==null) return null;
			
			else {
				for (int i = fileReaderIndex; i < tempQuery.length(); i++) {
					String temp = "" + tempQuery.charAt(i);
					fileReaderIndex = i + 1;
					
					if (temp.equals(";")) break;
					
					query += tempQuery.charAt(i) + "";
				}
				return query;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}

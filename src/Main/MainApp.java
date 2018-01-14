package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainApp {
	private static final String JDBC_DRIVER = "org.sqlite.JDBC";
	private static String URL = "jdbc:sqlite:";
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet res = null;
	static StringBuilder resultB;

	public static void main(String[] args) {
		File file = new File("C:/Users/kolyasuk/Desktop/Query.txt");
		readQuery(file);
		System.out.println(resultB.toString());
		writeResult();

	}

	private static void readQuery(File file) {
		try {
			StringBuilder strb = new StringBuilder();
			Scanner scan = new Scanner(file);
			if (!scan.hasNextLine()) {
				System.out.println("Файл Пуст!");
				return;
			}

			String dbPath = scan.nextLine();

			if (!new File(dbPath).exists()) {
				System.out.println("DataBase file not exist");
				return;
			}

			URL += dbPath;
			while (scan.hasNextLine()) {
				strb.append(scan.nextLine());
			}

			String[] query = strb.toString().split(";");
			resultB = new StringBuilder();

			for (int i = 0; i < query.length; i++) {
				resultB.append("Запрос " + (i + 1) + ":" + "\n" + query[i] + "\n");
				resultB.append("Результат:" + "\n");
				executeQuery(query[i]);
			}

		} catch (FileNotFoundException e) {
			System.out.println("Query file not exist");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void executeQuery(String query) {
		try {
			Driver driver = (Driver) Class.forName(JDBC_DRIVER).newInstance();
			DriverManager.deregisterDriver(driver);

			con = DriverManager.getConnection(URL);
			stmt = con.createStatement();
			res = stmt.executeQuery(query);

			while (res.next()) {
				resultB.append(res.getString("name_en") + " - " + res.getString("name_ru") + "\n");
			}
			resultB.append("\n");

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			System.out.println("Query incorrect");
		}
	}

	private static void writeResult() {
		File file = new File("C:/Users/kolyasuk/Desktop/result.txt");
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(resultB.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

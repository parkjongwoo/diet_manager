package diet_manager.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
	static Connection conn;
	String url = "jdbc:oracle:thin:@172.16.3.4:1521:orcl";
	String user = "scott";
	String password = "tiger";
	
	private DBConn() throws Exception {
		// 1. 드라이버 로딩
		// 2. Connection 객체 얻어오기
		// 1. 드라이버로딩
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// 2. Connection 연결객체 얻어오기

		conn = DriverManager.getConnection(url, user, password);
	}
	
	public static Connection getConnection() throws Exception{
		if(conn == null) new DBConn();
		return conn;
	}
}

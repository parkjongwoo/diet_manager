package diet_manager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import diet_manager.model.vo.EatVO;

public class DetailViewModel {
	Connection con;
	String url = "jdbc:oracle:thin:@172.16.3.3:1521:orcl";
	String user = "scott";
	String pass = "tiger";
	
	CustomerModel db;
	
	public DetailViewModel() throws Exception {
		// 1. 드라이버로딩
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// 2. Connection 연결객체 얻어오기
		con = DriverManager.getConnection(url, user, pass);
	}
	
	
	
	public ArrayList find_food(EatVO eat) throws Exception {
		ArrayList list = new ArrayList();
		String sql = "SELECT F.FNAME FNAME, E.EINTAKE EINTAKE, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN " 
					+ " FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID " 
					+ " WHERE AID=? AND ETIME=?"
					+ " ORDER BY ETIME DESC, EDATE ASC";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, db.customer.getCustId());
		st.setString(2, eat.getEtime());
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			JCheckBox box = new JCheckBox();
			ArrayList data = new ArrayList();
			data.add(box);
			data.add(rs.getString(1));
			data.add(rs.getString(2));
			data.add(rs.getString(3));
			data.add(rs.getString(4));
			data.add(rs.getString(5));
			data.add(rs.getString(6));
			data.add(rs.getString(7));
			data.add(rs.getString(8));
			data.add(rs.getString(9));
			data.add(rs.getString(10));
			data.add(rs.getString(11));
			data.add(rs.getString(12));
			list.add(data);
		}
		st.close();
		return list;
	}

}

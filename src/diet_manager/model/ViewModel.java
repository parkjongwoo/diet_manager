package diet_manager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import diet_manager.model.vo.Customer;

public class ViewModel {
	String url = "jdbc:oracle:thin:@172.16.3.4:1521:orcl";
	String user = "scott";
	String pass = "tiger";
	Connection con;

	public ViewModel() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection(url, user, pass);		
	}
	
	public Customer cusInfo(String id) throws Exception{
		Customer dao = new Customer();
		String sql = "SELECT a.aname name, a.azender gender, to_char(a.abirth, 'YYYY') birth, a.aheight height,"
				     + " w.aweight weight, a.aactive active"
				     + " FROM d_acount a, d_weight w WHERE a.aid=w.aid and a.aid=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, id);
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			dao.setCustName(rs.getString("name"));
			dao.setCustGender(rs.getString("gender"));
			dao.setCustBirth(rs.getString("birth"));
			dao.setCustHeight(rs.getInt("height"));
			dao.setCustWeight(rs.getInt("weight"));
			dao.setCustEtc(rs.getInt("active"));			
		}
		return dao;
	}
	public void modifyWeight(Customer dao, String id) throws Exception {
		String sql = "UPDATE d_weight SET aweight=? WHERE aid=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, String.valueOf((dao.getCustWeight())));
		st.setString(2, id);
		st.executeUpdate();		
		st.close();
	}
}


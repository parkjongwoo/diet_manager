package diet_manager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import diet_manager.model.vo.Customer;
import diet_manager.util.Util;

public class CustomerModel {
	Connection con;	
	
	public CustomerModel() throws Exception {
		con = DBConn.getConnection();
	}
	
	public void insertCustomer(Customer dao) throws Exception {
		String sql = " INSERT ALL" + 
				" INTO d_acount VALUES (aid, aname, apass, azender, abirth, aheight, aactive)" + 
				" INTO d_weight VALUES (aid, adate, aweight)" + 
				" SELECT ? aid, ? aname,? apass, ? azender, to_char(?,'yyyy-mm-dd') abirth," + 
				" ? aheight, ? aactive, TO_CHAR(SYSDATE,'yyyy-mm-dd') adate, ? aweight from dual";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, dao.getCustId());
		st.setString(2, dao.getCustName());
		st.setString(3, dao.getCustPass());
		st.setString(4, dao.getCustGender());
		st.setDate(5, Util.convertDtoD(dao.getCustBirth()));
		st.setDouble(6, dao.getCustHeight());
		st.setInt(7, dao.getCustEtc());
		st.setDouble(8, dao.getCustWeight());
		st.executeUpdate();
		st.close();
	}
	
	public int checkPass(String id,String pwHash) throws Exception {
		System.out.println("id:"+id+" pwHash:"+pwHash);
		String sql = "SELECT a.aname name, a.azender gender, a.abirth birth, a.aheight height, " + 
				"			    w.aweight weight, a.aactive active " + 
				"			    FROM d_acount a INNER JOIN " + 
				"                (select * from d_weight where aid=? and adate in " + 
				"                (select max(adate) from d_weight where aid=?)) w " + 
				"			    ON a.aid=w.aid " + 
				"			    WHERE a.aid=? and a.apass=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, id.trim());
		st.setString(2, id.trim());
		st.setString(3, id.trim());
		st.setString(4, pwHash);
		ResultSet rs = st.executeQuery();
		int result = 0;
		while(rs.next()) {
			ViewModel.loginUser = new Customer();
			ViewModel.loginUser.setCustId(id);
			ViewModel.loginUser.setCustName(rs.getString(1));
			ViewModel.loginUser.setCustGender(rs.getString(2));
			ViewModel.loginUser.setCustBirth(rs.getDate(3));
			ViewModel.loginUser.setCustHeight(rs.getDouble(4));
			ViewModel.loginUser.setCustWeight(rs.getDouble(5));
			ViewModel.loginUser.setCustEtc(rs.getInt(6));
			result++;
			System.out.println("customer:"+ViewModel.loginUser.toString());
		}
		
		rs.close();
		st.close();
		return result;
	}	
}

package diet_manager.model;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.vo.Customer;

public class CustomerModel {
	Connection con;
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String user = "scott";
	String pass = "tiger";
	public CustomerModel() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con=DriverManager.getConnection(url, user, pass);
	}
	
	public void insertCustomer(Customer dao) throws Exception {
		String sql = "INSERT INTO " + " d_acount(aid, aname, apass, azender, abirth, aheight, aweight, aetc) " + " VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, dao.getCustId());
		st.setString(2, dao.getCustName());
		st.setString(3, encrypt(String.valueOf(dao.getCustPass())));
		st.setString(4, dao.getCustGender());
		st.setString(5, dao.getCustBirth());
		st.setString(6, Double.toString(dao.getCustHeight()));
		st.setString(7, Double.toString(dao.getCustWeight()));
		st.setString(8, dao.getCustEtc());
		st.executeUpdate();
		st.close();
	}
	
	public Customer checkPass(String id,String pw) throws Exception {
		String sql = "SELECT apass " + " FROM d_acount " + " WHERE aid=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, id);
		st.setString(2, pw);
		ResultSet rs = st.executeQuery();
		Customer ch = new Customer();
		
		while(rs.next()) {
			ch.setCustName(rs.getString("aname"));
			ch.setCustBirth(rs.getString("abirth"));
			ch.setCustGender(rs.getString("azender"));
			ch.setCustId(rs.getString("aid"));
			ch.setCustPass(rs.getString("apass"));
			ch.setCustEtc(rs.getString("aetc"));
			ch.setCustHeight(rs.getDouble("aheight"));
			ch.setCustWeight(rs.getDouble("aweight"));
		}
			
		
		
		rs.close();
		st.close();
		return ch;
	}
	
	public static String encrypt(String planText) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }          

            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

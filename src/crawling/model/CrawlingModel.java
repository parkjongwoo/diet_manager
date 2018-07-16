package crawling.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import diet_manager.model.vo.FoodVO;

public class CrawlingModel {
	Connection conn;
	
	public CrawlingModel() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		conn = DBConn.getConnection();
	}
	
	public int insertFoodInfo(ArrayList<FoodVO> list) throws Exception{
		int result = 0;
		String sql = "INSERT INTO D_FOOD ( " +
				"fid, fname, fper, fcal, fco, fpro, ffat, fsu, fna, fcho, fsat, ftran, fnum, fcate) "+
				"VALUES (d_seq_fid.nextval, ?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		FoodVO dao=null;
		PreparedStatement ps = conn.prepareStatement(sql);
		for(int i=0;i<list.size();i++) {
			dao = list.get(i);
			ps.setString(1, dao.getFname());
			ps.setDouble(2, dao.getFper());
			ps.setDouble(3, dao.getFcal());
			ps.setDouble(4, dao.getFco());
			ps.setDouble(5, dao.getFpro());
			ps.setDouble(6, dao.getFfat());
			ps.setDouble(7, dao.getFsu());
			ps.setDouble(8, dao.getFna());
			ps.setDouble(9, dao.getFcho());
			ps.setDouble(10, dao.getFsat());
			ps.setDouble(11, dao.getFtran());
			ps.setDouble(12, dao.getFnum());
			ps.setDouble(13, dao.getFcate());			
			result += ps.executeUpdate();
		}
		return result;
	}
}

package diet_manager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JCheckBox;

import diet_manager.model.vo.EatVO;
import diet_manager.util.Util;

public class DetailViewModel {
	
	Connection con;
	
	public DetailViewModel() throws Exception {
		con = DBConn.getConnection();
	}
	
	public ArrayList find_food(EatVO eat) throws Exception {
		ArrayList list = new ArrayList();
		String sql = "SELECT F.FNAME FNAME, E.EINTAKE EINTAKE, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN " 
					+ " FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID " 
					+ " WHERE AID=? AND ETIME=?"
					+ " ORDER BY ETIME DESC, EDATE ASC";
		PreparedStatement st = con.prepareStatement(sql);
//		st.setString(1, db.customer.getCustId());
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
	public ArrayList<EatVO> searchEat(String aid, Date eatdate) throws SQLException{
		ArrayList<EatVO> list = new ArrayList<EatVO>();
		String sql = "SELECT E.EID EID, E.AID AID, E.EDATE EDATE, E.ETIME ETIME, E.FID FID, E.EINTAKE EINTAKE, "
				+ " F.FNAME FNAME, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, "
				+ " F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN " + 
				"FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID " + 
				"WHERE TO_CHAR(EDATE,'YYYY-MM-DD')=TO_CHAR(?,'YYYY-MM-DD') AND E.AID=?" + 
				"ORDER BY E.ETIME ASC, EDATE ASC";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDate(1, Util.convertDtoD(eatdate));
		ps.setString(2, aid);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			EatVO dao = new EatVO();
			dao.setEid(rs.getInt(1));
			dao.setAid(rs.getString(2));
			dao.setEdate(rs.getDate(3));
			dao.setEtime(rs.getString(4));
			dao.setFid(rs.getInt(5));
			dao.seteIntake(rs.getDouble(6));
			dao.setFname(rs.getString(7));
			dao.setFper(rs.getDouble(8));
			dao.setFcal(rs.getDouble(9));
			dao.setFco(rs.getDouble(10));
			dao.setFpro(rs.getDouble(11));
			dao.setFfat(rs.getDouble(12));
			dao.setFsu(rs.getDouble(13));
			dao.setFna(rs.getDouble(14));
			dao.setFcho(rs.getDouble(15));
			dao.setFsat(rs.getDouble(16));
			dao.setFtran(rs.getDouble(17));
			list.add(dao);
		}
		rs.close();
		ps.close();
		return list;
	}
}

package diet_manager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import diet_manager.model.vo.Customer;
import diet_manager.model.vo.EatVO;
import diet_manager.model.vo.WeightVO;
import diet_manager.util.Util;

public class ViewModel {
	
	Connection con;
	public static Customer loginUser = null;
	public ArrayList<EatVO> todayEatList;
	public ArrayList<WeightVO> weightList;
	public ViewModel() throws Exception {		
		con = DBConn.getConnection();	
	}
	
	public void searchTodayEatList(String aid) throws SQLException{
		todayEatList = searchEat(aid);		
	}
	
	public void modifyWeight(double weight, String id) throws Exception {
		String sql = "MERGE INTO d_weight w " + 
				" USING (SELECT ? aid,TO_CHAR(SYSDATE,'yyyy-mm-dd') adate,? aweight FROM dual) n " + 
				" ON ( w.aid = n.aid and w.adate=n.adate) " + 
				" WHEN MATCHED THEN " + 
				" UPDATE " + 
				" SET w.aweight = n.aweight " + 
				" WHEN NOT MATCHED THEN " + 
				" INSERT ( aid,adate,aweight) " + 
				" VALUES (n.aid,n.adate,n.aweight)";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, id);
		st.setDouble(2, weight);
		st.executeUpdate();		
		st.close();
	}
	
	public ArrayList<EatVO> searchEat(String aid) throws SQLException{
		ArrayList<EatVO> list = new ArrayList<EatVO>();
		String sql = "SELECT E.EID EID, E.AID AID, E.EDATE EDATE, E.ETIME ETIME, E.FID FID, E.EINTAKE EINTAKE, "
				+ " F.FNAME FNAME, F.FPER FPER, F.FCAL FCAL, F.FCO FCO, F.FPRO FPRO, "
				+ " F.FFAT FFAT, F.FSU FSU, F.FNA FNA, F.FCHO FCHO, F.FSAT FSAT, F.FTRAN FTRAN " + 
				"FROM D_EAT E INNER JOIN D_FOOD F ON E.FID=F.FID " + 
				"WHERE TO_CHAR(EDATE,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD') AND E.AID=?" + 
				"ORDER BY E.ETIME ASC, EDATE ASC";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, aid);
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

	public void searchWeightList(String custId) throws SQLException {
		weightList = new ArrayList<WeightVO>();
		String sql = "SELECT ADATE,AWEIGHT FROM D_WEIGHT WHERE AID=? ORDER BY ADATE";
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1, custId);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			WeightVO dao = new WeightVO();
			dao.setAdate(rs.getDate(1));
			dao.setWeight(rs.getDouble(2));
			dao.setAid(custId);
			weightList.add(dao);
		}
		rs.close();
		ps.close();
	}
	
	public int checkPass(String id) throws Exception {
//		System.out.println("id:"+id+" pwHash:"+pwHash);
		String sql = "SELECT a.aname name, a.azender gender, a.abirth birth, a.aheight height, " + 
				"			    w.aweight weight, a.aactive active " + 
				"			    FROM d_acount a INNER JOIN " + 
				"                (select * from d_weight where aid=? and adate in " + 
				"                (select max(adate) from d_weight where aid=?)) w " + 
				"			    ON a.aid=w.aid " + 
				"			    WHERE a.aid=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, id.trim());
		st.setString(2, id.trim());
		st.setString(3, id.trim());
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
//			System.out.println("customer:"+ViewModel.loginUser.toString());
		}
		
		rs.close();
		st.close();
		return result;
	}

	public double getTodayCal() {
		if(todayEatList==null) return 0;
		double result = 0.0;
		
		for(int i=0;i<todayEatList.size();i++) {
			result += todayEatList.get(i).geteIntake()*todayEatList.get(i).getFcal();
		}
		return Util.formating(result,2);
	}
}


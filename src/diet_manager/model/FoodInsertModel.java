package diet_manager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import diet_manager.model.vo.EatVO;
import diet_manager.model.vo.FoodVO;
import diet_manager.util.Util;

public class FoodInsertModel {
	Connection conn;
	
	String eatingTime="아침";
	
	public FoodInsertModel() throws Exception {
		conn = DBConn.getConnection();
	}
	
	/**
	 * 먹은 식사를 DB에 입력
	 * @param id 사용자ID
	 * @param eatingTime 먹은 시간대 "아침","점심","저녁"
	 * @param foodID 먹은 음식의 음식ID(d_food 테이블의 fid)
	 * @param intake 먹은 량. (g)단위
	 * @return 처리된 데이터 수
	 * @throws SQLException 
	 */
	public int insertEat(ArrayList<EatVO> list) throws SQLException {
		int result = 0;		
		String sql = "INSERT INTO D_EAT(EID,AID,EDATE,ETIME,FID,EINTAKE) " + 
				" VALUES (D_SEQ_EID.NEXTVAL, ?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		for(int i=0;i<list.size();i++) {
			EatVO vo = list.get(i);
			ps.setString(1, vo.getAid());
			ps.setDate(2, Util.convertDtoD(vo.getEdate()));
			ps.setString(3, vo.getEtime());
			ps.setInt(4, vo.getFid());
			ps.setDouble(5, vo.geteIntake());
			ps.addBatch();
			ps.clearParameters();
		}
		int[] cnt = ps.executeBatch();
		for(int i=0;i<cnt.length;i++) {
			if(cnt[i]>0)
				result += cnt[i];
		}
		ps.close();
		
		return result;
	}
	
	public ArrayList<FoodVO> searchFood(String foodName) throws SQLException {
		ArrayList<FoodVO> list = new ArrayList<FoodVO>();
		String sql = "SELECT * FROM D_FOOD WHERE FNAME LIKE ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, "%"+foodName.trim()+"%");
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			FoodVO dao = new FoodVO();
			dao.setFid(rs.getInt(1));
			dao.setFname(rs.getString(2));
			dao.setFper(rs.getDouble(3));
			dao.setFcal(rs.getDouble(4));
			dao.setFco(rs.getDouble(5));
			dao.setFpro(rs.getDouble(6));
			dao.setFfat(rs.getDouble(7));
			dao.setFsu(rs.getDouble(8));
			dao.setFna(rs.getDouble(9));
			dao.setFcho(rs.getDouble(10));
			dao.setFsat(rs.getDouble(11));
			dao.setFtran(rs.getDouble(12));
			list.add(dao);
		}
		rs.close();
		ps.close();
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
		PreparedStatement ps = conn.prepareStatement(sql);
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

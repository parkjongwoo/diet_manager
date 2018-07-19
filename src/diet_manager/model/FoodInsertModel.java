package diet_manager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
	 * @param foodID 먹은 음식의 음식ID(d_food 테이블의 fid)
	 * @param eatingDate 먹은 날짜의 java.util.Date 객체
	 * @param eatingTime 먹은 시간대 "아침","점심","저녁"
	 * @param intake 먹은 량. (g)단위
	 * @return 처리된 데이터 수
	 * @throws SQLException 
	 */
	public int insertMeal(String id, int foodID, Date eatingDate, 
			String eatingTime, double intake) throws SQLException {
		int result = 0;
		
		String sql = "INSERT INTO D_EAT(EID,AID,EDATE,ETIME,FID,EINTAKE) " + 
				" VALUES (D_SEQ_EID.NEXTVAL, ?, SYSDATE, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		ps.setDate(2, Util.convertDtoD(eatingDate));
		ps.setString(3, eatingTime);
		ps.setInt(4, foodID);
		ps.setDouble(5, intake);
		result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public ArrayList<FoodVO> searchFood(String foodName) throws SQLException {
		ArrayList<FoodVO> list = new ArrayList<FoodVO>();
		String sql = "SELECT * FROM D_FOOD WHERE FNAME LIKE ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, "%"+foodName+"%");
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
		return list;
	}
}

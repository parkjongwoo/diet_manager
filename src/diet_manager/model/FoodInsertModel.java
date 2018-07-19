package diet_manager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class FoodInsertModel {
	Connection conn;
	
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
				" VALUES (D_SEQ_EID.NEXTVAL, ?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		
		return result;
	}
}

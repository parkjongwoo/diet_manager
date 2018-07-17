package jfreechartdemo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import jfreechartdemo.model.vo.EmpVO;

public class ChartDemoModel {
	
	Connection conn;
	
	public ChartDemoModel() throws Exception {
		conn = DBConn.getConnection();
	}
	
	public ArrayList<EmpVO> searchAllEmp() throws Exception{
		
		ArrayList<EmpVO> list = new ArrayList<EmpVO>();
		EmpVO vo = null;
		String sql = "SELECT * FROM EMP";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			vo = new EmpVO();
			
			vo.setEmpno(rs.getInt(1));
			vo.setEname(rs.getString(2));
			vo.setJob(rs.getString(3));
			vo.setMgr(rs.getInt(4));
			vo.setHiredate(rs.getDate(5, Calendar.getInstance()));
			vo.setSal(rs.getInt(6));
			vo.setComm(rs.getInt(7));
			vo.setDeptno(rs.getInt(8));
			
			list.add(vo);
		}
		return list;
	}
	
	public ArrayList<ArrayList<String>> searchJobList() throws Exception{
		String sql = "SELECT COUNT(JOB) CNT,JOB FROM EMP GROUP BY JOB";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		ArrayList<String> tuple;
		
		while(rs.next()) {
			tuple = new ArrayList<String>();
			tuple.add(rs.getString(1));
			tuple.add(rs.getString(2));			
			list.add(tuple);
		}
		
		return list;
	}
}

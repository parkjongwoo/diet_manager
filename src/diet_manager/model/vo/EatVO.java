package diet_manager.model.vo;

import java.util.Date;

public class EatVO {
	boolean isSelected;//테이블 체크박스용
	
	int eid;//섭취아이디
	String aid;//사용자아이디
	Date edate;//먹은날짜
	String etime;//먹은 시간대'아침''점심''저녁'
	double eIntake;//먹은량 (g)
	
	int fid;//음식아이디
	String fname;//음식명
	double fper;//1회제공량
	double fcal;//1회칼로리
	double fco;//1회탄수
	double fpro;//1회단백
	double ffat;//1회지방
	double fsu;//1회당분
	double fna;//1회나트륨
	double fcho;//1회콜레스테롤
	double fsat;//1회포화지방
	double ftran;//1회트랜스지방
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public Date getEdate() {
		return edate;
	}
	public void setEdate(Date edate) {
		this.edate = edate;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public double geteIntake() {
		return eIntake;
	}
	public void seteIntake(double eIntake) {
		this.eIntake = eIntake;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public double getFper() {
		return fper;
	}
	public void setFper(double fper) {
		this.fper = fper;
	}
	public double getFcal() {
		return fcal;
	}
	public void setFcal(double fcal) {
		this.fcal = fcal;
	}
	public double getFco() {
		return fco;
	}
	public void setFco(double fco) {
		this.fco = fco;
	}
	public double getFpro() {
		return fpro;
	}
	public void setFpro(double fpro) {
		this.fpro = fpro;
	}
	public double getFfat() {
		return ffat;
	}
	public void setFfat(double ffat) {
		this.ffat = ffat;
	}
	public double getFsu() {
		return fsu;
	}
	public void setFsu(double fsu) {
		this.fsu = fsu;
	}
	public double getFna() {
		return fna;
	}
	public void setFna(double fna) {
		this.fna = fna;
	}
	public double getFcho() {
		return fcho;
	}
	public void setFcho(double fcho) {
		this.fcho = fcho;
	}
	public double getFsat() {
		return fsat;
	}
	public void setFsat(double fsat) {
		this.fsat = fsat;
	}
	public double getFtran() {
		return ftran;
	}
	public void setFtran(double ftran) {
		this.ftran = ftran;
	}
	public void setFoodInfo(FoodVO vo) {
		this.fid = vo.getFid();//음식아이디
		this.fname = vo.getFname();//음식명
		this.fper = vo.getFper();//1회제공량
		this.fcal = vo.getFcal();//1회칼로리
		this.fco = vo.getFco();//1회탄수
		this.fpro = vo.getFpro();//1회단백
		this.ffat = vo.getFfat();//1회지방
		this.fsu = vo.getFsu();//1회당분
		this.fna = vo.getFna();//1회나트륨
		this.fcho = vo.getFcho();//1회콜레스테롤
		this.fsat = vo.getFsat();//1회포화지방
		this.ftran = vo.getFtran();//1회트랜스지방
	}
	@Override
	public String toString() {
			return eid + ":" + aid+ ":" + fid + ":"+ edate+ ":" +etime + ":" +eIntake;		
	}
}

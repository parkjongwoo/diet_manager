package diet_manager.model.vo;

public class Customer {
	String custName;  //고객명
	String custGender; //성별
	String custId;    //아이디
	String custPass;  //비밀번호
	String custBirth; //생년월일
	double custHeight;   //키
	double custWeight;   //무게
	int custEtc;	  //특이사항
	
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustGender() {
		return custGender;
	}
	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustPass() {
		return custPass;
	}
	public void setCustPass(String custPass) {
		this.custPass = custPass;
	}
	public String getCustBirth() {
		return custBirth;
	}
	public void setCustBirth(String custBirth) {
		this.custBirth = custBirth;
	}
	public double getCustHeight() {
		return custHeight;
	}
	public void setCustHeight(double custHeight) {
		this.custHeight = custHeight;
	}
	public double getCustWeight() {
		return custWeight;
	}
	public void setCustWeight(double custWeight) {
		this.custWeight = custWeight;
	}
	public int getCustEtc() {
		return custEtc;
	}
	public void setCustEtc(int custEtc) {
		this.custEtc = custEtc;
	}
	
	
}

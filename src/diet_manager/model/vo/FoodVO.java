package diet_manager.model.vo;

public class FoodVO {
	boolean isChecked;
	
	int fid;
	String fname;
	double fper;
	double fcal;
	double fco;
	double fpro;
	double ffat;
	double fsu;
	double fna;
	double fcho;
	double fsat;
	double ftran;
	
	double eIntake = 1.0; 
	
	

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
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
	
	public double geteIntake() {
		return eIntake;
	}

	public void seteIntake(double eIntake) {
		this.eIntake = eIntake;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(isChecked)).append(':')
		.append(String.valueOf(fid)).append(':')
		.append(String.valueOf(fname)).append(':')
		.append(String.valueOf(fper)).append(':')
		.append(String.valueOf(fcal)).append(':')
		.append(String.valueOf(fco)).append(':')
		.append(String.valueOf(fpro)).append(':')
		.append(String.valueOf(ffat)).append(':')
		.append(String.valueOf(fsu)).append(':')
		.append(String.valueOf(fna)).append(':')
		.append(String.valueOf(fcho)).append(':')
		.append(String.valueOf(fsat)).append(':')
		.append(String.valueOf(ftran)).append(':')
		.append(String.valueOf(eIntake));
		return sb.toString();
	}

}

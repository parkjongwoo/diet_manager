package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import diet_manager.model.CustomerModel;
import diet_manager.model.vo.Customer;
import diet_manager.util.Util;
import diet_manager.view.component.DatePicker;

public class RegistView extends JFrame{
	JTextField tfName, tfId, tfTel, 
				tfAge, tfHeight, tfWeight, tfEtc;
	JPasswordField pfPass;
	JRadioButton male, female;
	JButton bRegist;
	ButtonGroup group;
	DatePicker dp;
	CustomerModel db;
	
	public RegistView() {
		
		connectDB();
		addLayout(); 	// 화면설계
		eventProc();
	}
	
	private void connectDB() {
		try {
			db = new CustomerModel();
		} catch (Exception e) {
			System.out.println("디비연결 실패");
			e.printStackTrace();
		}
	}
	
	public void addLayout() {
		tfName = new JTextField(10);
		tfId = new JTextField(10);
		pfPass = new JPasswordField(10);
		tfTel = new JTextField(10);
		tfAge = new JTextField(10);
		tfHeight = new JTextField(10);
		tfWeight = new JTextField(10);
		tfEtc = new JTextField(10);
		
		male = new JRadioButton("남자");
		female = new JRadioButton("여자");
		bRegist = new JButton("등록");
		
		group = new ButtonGroup();
		group.add(male);
		group.add(female);
		
		dp = new DatePicker();
		
		JPanel p_back = new JPanel();
		p_back.setBorder(new TitledBorder("회원가입"));
		p_back.setLayout(new BorderLayout());
		
		JPanel p_category = new JPanel();
		p_category.setLayout(new GridLayout(9,1));
		
		JPanel p_grid1 = new JPanel();
		p_grid1.add(new JLabel("  이     름    "));
		p_grid1.add(tfName);
		p_category.add(p_grid1);
		
		JPanel p_grid2 = new JPanel();
		p_grid2.add(male);
		p_grid2.add(female);
		p_category.add(p_grid2);
		
		JPanel p_grid3 = new JPanel();
		p_grid3.add(new JLabel(" 아 이 디    "));
		p_grid3.add(tfId);
		p_category.add(p_grid3);
		
		JPanel p_grid4 = new JPanel();
		p_grid4.add(new JLabel("비밀번호   "));
		p_grid4.add(pfPass);
		p_category.add(p_grid4);
		
		JPanel p_grid5 = new JPanel();
		p_grid5.add(new JLabel("전화번호   "));
		p_grid5.add(tfTel);
		p_category.add(p_grid5);
		
		JPanel p_grid6 = new JPanel();
		p_grid6.add(new JLabel("생년월일   "));
		p_grid6.add(dp);
		p_category.add(p_grid6);
		
//		JPanel p_grid7 = new JPanel();
//		p_grid7.add(new JLabel("    나    이    "));
//		p_grid7.add(tfAge);
//		p_category.add(p_grid7);
		
		JPanel p_grid8 = new JPanel();
		p_grid8.add(new JLabel("   키 (cm)   "));
		p_grid8.add(tfHeight);
		p_category.add(p_grid8);
		
		JPanel p_grid9 = new JPanel();
		p_grid9.add(new JLabel("몸무게(kg)"));
		p_grid9.add(tfWeight);
		p_category.add(p_grid9);
		
		JPanel p_grid10 = new JPanel();
		p_grid10.add(new JLabel("특이사항   "));
		p_grid10.add(tfEtc);
		p_category.add(p_grid10);
		
		p_back.add(p_category);
		p_back.add(bRegist, BorderLayout.SOUTH);
		
		
		
		
		setLayout(new BorderLayout());
		add(p_back, BorderLayout.CENTER);
		setSize(300,500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void eventProc() {
		ButtonEventHandler btnHandler = new ButtonEventHandler();
		bRegist.addActionListener(btnHandler);
		
	}
	
	private boolean checkFormData() {
		String msg = null;
		if("".equals(tfName.getText().trim())){
			msg="이름을 입력하세요.";
		}else if("".equals(tfId.getText().trim())){
			msg="아이디를 입력하세요.";
		}else if("".equals(tfTel.getText().trim())){
			msg="전화번호를 입력하세요.";
		}else if("".equals(tfAge.getText().trim())){
			msg="나이를 입력하세요.";
		}else if("".equals(tfHeight.getText().trim())){
			msg="신장을  입력하세요.";
		}else if("".equals(tfWeight.getText().trim())){
			msg="몸무게를 입력하세요.";
		}else if("".equals(tfEtc.getText().trim())){
			msg="활동지수를 입력하세요.";
		}
		if(msg!=null) {
			JOptionPane.showMessageDialog(this, msg);
			return false;
		}else {
			return true;
		}
	}
	
	class ButtonEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Object o = ev.getSource();
			if(o==bRegist) {
				if(checkFormData())
					registCustomer();
			}
		}
	}
	public void registCustomer() {
		Customer c = new Customer();
		char[] ca = pfPass.getPassword();
		String pw = Util.encrypt(new String(ca));
		c.setCustName(tfName.getText());
		c.setCustGender(group.getElements().nextElement().getText());
		c.setCustId(tfId.getText());
		c.setCustPass(pw);
		c.setCustBirth(dp.getDate());
		c.setCustHeight(Double.parseDouble((tfHeight.getText())));
		c.setCustEtc(Integer.parseInt(tfEtc.getText()));
		
		try {
			db.insertCustomer(c);
			Arrays.fill(ca, '0');
			pw = null;
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "접속실패:"+e.getMessage());
		}
	}	
}

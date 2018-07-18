package foodmanagement;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import model.CustomerModel;
import model.vo.Customer;

public class RegistView extends JFrame {
	JTextField tfName, tfId, tfTel, tfBirth, tfAge, tfHeight, tfWeight, tfEtc;
	JPasswordField pfPass;
	JRadioButton male, female;
	JButton bRegist;
	ButtonGroup group;
	
	CustomerModel db;
	
	public RegistView() {
		tfName = new JTextField(10);
		tfId = new JTextField(10);
		pfPass = new JPasswordField(10);
		tfTel = new JTextField(10);
		tfBirth = new JTextField(10);
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
		
		addLayout(); 	// 화면설계
		eventProc();
	}
	
	public void addLayout() {
				
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
		p_grid6.add(tfBirth);
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void eventProc() {
		ButtonEventHandler btnHandler = new ButtonEventHandler();
		bRegist.addActionListener(btnHandler);
	}
	
	class ButtonEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Object o = ev.getSource();
			
			if(o==bRegist) {
				registCustomer();
			}
		}
	}
	public void registCustomer() {
		Customer c = new Customer();
		c.setCustName(tfName.getText());
		c.setCustGender(group.getElements().nextElement().getText());
		c.setCustId(tfId.getText());
		c.setCustPass(pfPass.getPassword());
		c.setCustTel(tfTel.getText());
		c.setCustBirth(tfBirth.getText());
//		c.setCustAge(Integer.parseInt(tfAge.getText()));
		c.setCustHeight(Double.parseDouble((tfHeight.getText())));
		c.setCustWeight(Double.parseDouble(tfWeight.getText()));
		c.setCustEtc(tfEtc.getText());
		
		try {
			db.insertCustomer(c);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "접속실패:"+e.getMessage());
		}
	}
	
}

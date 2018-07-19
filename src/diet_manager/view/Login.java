package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import diet_manager.model.CustomerModel;
import diet_manager.model.ViewModel;
import diet_manager.model.vo.Customer;


public class Login extends JFrame {
	JTextField tfId;
	JButton bRegist, bLogin;
	JPasswordField pfPass;
	
	
	CustomerModel db;
	
	public Login() {
		tfId = new JTextField(10);
		pfPass = new JPasswordField(10);
		bRegist = new JButton("회원가입");
		bLogin = new JButton("로그인");
		connectDB();
		addLayout(); 	// 화면설계
		eventProc();
	}
	
	private void connectDB() {
		try {
			db = new CustomerModel();
			System.out.println("디비연결 성공");
		}
		catch (Exception e) {
			System.out.println("대여디비 실패");
		}
	}

	public void eventProc() {
		ButtonEventHandler btnHandler = new ButtonEventHandler();
		bRegist.addActionListener(btnHandler);
		bLogin.addActionListener(btnHandler);
	}
	
	class ButtonEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Object o = ev.getSource();
			
			if(o==bRegist) {
				registUser();
				
			}
			
			if(o==bLogin) {
				passCheck();
			}
		}		
	}
	
	private void registUser() {
		new RegistView();
	}
	
	public void passCheck() {
//		Customer c = new Customer();
//		c.setCustId(tfId.getText());
//		c.setCustPass(pfPass.getPassword());
		
		String id = tfId.getText();
		char[] pw = pfPass.getPassword();
		String pwHash = encrypt(pw);
		Arrays.fill(pw, '0');
		try {
			int result = db.checkPass(id,pwHash);
			
			if(result>0) {				
		    	System.out.println("로그인 성공:"+ViewModel.loginUser.getCustId());
			}
			else {
				System.out.println("실패");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"접속실패"+ e.getMessage());
		}
	}
	
	public void addLayout() {
		
		JPanel p_back = new JPanel();
	
		p_back.setLayout(new GridLayout(3,3));
		
		JPanel p_north_west = new JPanel();
		p_back.add(p_north_west);
		
		JPanel p_title = new JPanel();
		p_back.add(p_title);
		
		JPanel p_north_east = new JPanel();
		p_back.add(p_north_east);
		
		JPanel p_west = new JPanel();
		p_back.add(p_west);
		
		JPanel p_login = new JPanel();
		p_login.setBorder(new TitledBorder("식단 관리 프로그램"));
		p_login.setLayout(new GridLayout(4,1));
		JPanel p_login_back1 = new JPanel();
		p_login.add(p_login_back1);
		JPanel p_login_id = new JPanel();
		p_login_id.add(new JLabel("    I      D    "));
		p_login_id.add(tfId);
		p_login.add(p_login_id);
		JPanel p_login_pass = new JPanel();
		p_login_pass.add(new JLabel("비밀번호"));
		p_login_pass.add(pfPass);
		p_login.add(p_login_pass);
		JPanel p_login_back2 = new JPanel();
		p_login.add(p_login_back2);
		p_back.add(p_login);
		
		JPanel p_east = new JPanel();
		p_back.add(p_east);
		
		JPanel p_south_west = new JPanel();
		p_back.add(p_south_west);
		
		JPanel p_button = new JPanel();
		p_button.add(bRegist);
		p_button.add(bLogin);
		p_back.add(p_button);
		
		JPanel p_south_east = new JPanel();
		p_back.add(p_south_east);
		
		setLayout(new BorderLayout());
		add(p_back);
		
		

		setSize(600,500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static String encrypt(String planText) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }          

            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
	public static String encrypt(char[] cArr) {
		String a = new String(cArr);
		String r = encrypt(a);
		a = null;
		return r;
	}
}

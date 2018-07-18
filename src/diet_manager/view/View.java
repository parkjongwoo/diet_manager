package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import diet_manager.model.ViewModel;
import diet_manager.model.vo.Customer;

public class View extends JFrame{
	JTextArea ta, ta2;
	JTextField tfUpdate, tfID, tfWeight, tfHeight, tfName, tfGender, tfAge, tfEtc, tfKcal, tfTot, tfState, tfDiet;
	JComboBox cb;
	JLabel LLogin, LJoin;
	JButton bInsert, bShow, bExit, bCheck, bWeight;	
	String[] str = {"일별", "주별", "월별"};

	ViewModel db;

	public View(){
		addLayout();
		connectDB();		
		cusInfo();
		eventProc();
	}
	public void eventProc() {
		EvtHdlr eh = new EvtHdlr();
		LinkMouseListener ml = new LinkMouseListener();

		LLogin.addMouseListener(ml);
		bShow.addActionListener(eh);
		bExit.addActionListener(eh);
		bWeight.addActionListener(eh);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?","종료",JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.OK_OPTION) {
					System.exit(0);
				}
				else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}				
		});
	}
	class EvtHdlr implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object evt = e.getSource();
			if(evt==bInsert) {

			}			
			else if(evt==bShow) {
				new DetailView();
			}
			else if(evt==bExit) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?","종료",JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.OK_OPTION) {
					System.exit(0);
				}
				else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
			else if(evt==bWeight) {
				modifyWeight();
				tfWeight.setText(tfUpdate.getText());
				diet();
			}
		}		
	}
	class LinkMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
			JLabel l = (JLabel) evt.getSource();
			if(l==LLogin) {
				
			}
			else if (l==LJoin) {

			}
		}
	}
	public void cusInfo() {
		String id = tfID.getText();
		try {
			Customer c = db.cusInfo(id);
			tfName.setText(c.getCustName());
			tfGender.setText(c.getCustGender());
			Calendar now = Calendar.getInstance();
			Calendar birth = Calendar.getInstance();
			birth.setTime(c.getCustBirth());
			tfAge.setText(String.valueOf(now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)+1));
			tfHeight.setText(String.valueOf(c.getCustHeight()));
			tfWeight.setText(String.valueOf(c.getCustWeight()));
			tfEtc.setText(String.valueOf(c.getCustEtc()));
			tfUpdate.setText(String.valueOf(c.getCustWeight()));
			diet();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			int result = JOptionPane.showConfirmDialog(null, "로그인하시겠습니까?","Error",JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.OK_OPTION) {
				
			}
			else {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}
	}
	public void diet() {
		tfKcal.setText(String.valueOf(Math.round((((Double.parseDouble(tfHeight.getText())-100)*0.9))*Double.parseDouble(tfEtc.getText()))));
		double diet = Double.parseDouble(tfWeight.getText())/(Double.parseDouble(tfHeight.getText())*2/100);
		if (diet <= 18.5) {
			tfDiet.setText("저체중");
		}
		else if (diet <= 23 && diet > 18.5) {
			tfDiet.setText("정상");
		}
		else if (diet <= 25 && diet > 23) {
			tfDiet.setText("과체중");
		}
		else if (diet <= 30 && diet > 25) {
			tfDiet.setText("비만");
		}
		else if (diet > 30) {
			tfDiet.setText("고도비만");
		}	
	}
	public void modifyWeight() {
		String id = tfID.getText();
		Customer c = new Customer();
		c.setCustWeight(Double.parseDouble((tfUpdate.getText())));
		try {
			db.modifyWeight(c, id);
			JOptionPane.showMessageDialog(null, "수정되었습니다.");			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "수정 실패"+e.getMessage());
		}
	}
	public void connectDB(){
		try {
			db = new ViewModel();
			System.out.println("고객디비 연결");			
		}
		catch (Exception e){
			System.out.println("고객디비 연결 실패:"+e.getMessage());

		}
	}

	public void addLayout() {
		ta = new JTextArea("그래프 공간");	    
		ta2 = new JTextArea("그래프 공간");
		ta2.setLineWrap(true);
		LLogin = new JLabel("로그인");	    
		LJoin = new JLabel("회원가입");
		bInsert = new JButton("식단입력");
		bInsert.setFont(new Font("돋움", Font.BOLD, 15));
		bShow = new JButton("상세조회");
		bShow.setFont(new Font("돋움", Font.BOLD, 15));
		bExit = new JButton("종  료");
		bExit.setFont(new Font("돋움", Font.BOLD, 15));
		bCheck = new JButton("조회");
		bWeight = new JButton("수정");
		bWeight.setFont(new Font("맑은고딕", Font.BOLD, 11));
		tfUpdate = new JTextField(5);
		tfUpdate.setHorizontalAlignment(JTextField.RIGHT);
		tfID = new JTextField("pcw7895", 8);
		tfID.setHorizontalAlignment(JTextField.CENTER);
		tfID.setBorder(BorderFactory.createEmptyBorder());
		tfWeight = new JTextField(8);
		tfWeight.setHorizontalAlignment(JTextField.CENTER);
		tfWeight.setBorder(BorderFactory.createEmptyBorder());
		tfHeight = new JTextField(8);
		tfHeight.setHorizontalAlignment(JTextField.CENTER);
		tfHeight.setBorder(BorderFactory.createEmptyBorder());
		tfName = new JTextField(8);
		tfName.setHorizontalAlignment(JTextField.CENTER);
		tfName.setBorder(BorderFactory.createEmptyBorder());
		tfGender = new JTextField(8);
		tfGender.setHorizontalAlignment(JTextField.CENTER);
		tfGender.setBorder(BorderFactory.createEmptyBorder());
		tfAge = new JTextField(8);
		tfAge.setHorizontalAlignment(JTextField.CENTER);
		tfAge.setBorder(BorderFactory.createEmptyBorder());
		tfEtc = new JTextField(8);
		tfEtc.setHorizontalAlignment(JTextField.CENTER);
		tfEtc.setBorder(BorderFactory.createEmptyBorder());
		tfDiet = new JTextField(8);
		tfDiet.setHorizontalAlignment(JTextField.CENTER);
		tfDiet.setBorder(BorderFactory.createEmptyBorder());
		tfKcal = new JTextField(5);
		tfKcal.setHorizontalAlignment(JTextField.RIGHT);
		tfKcal.setBorder(BorderFactory.createEmptyBorder());
		tfTot = new JTextField(5);
		tfTot.setHorizontalAlignment(JTextField.RIGHT);
		tfState = new JTextField("현재 349Kcal가 부족합니다.", 20);
		tfState.setHorizontalAlignment(JTextField.CENTER);
		tfState.setBorder(BorderFactory.createEmptyBorder());

		cb = new JComboBox(str);

		JPanel p_up = new JPanel(new BorderLayout());		

		JPanel p_title = new JPanel();		
		JLabel LTitle = new JLabel("식단 관리 모니터링 시스템");
		LTitle.setFont(new Font("돋움", Font.BOLD, 25));		
		p_title.add(LTitle);

		JPanel p_up_down = new JPanel(new BorderLayout());

		JPanel p_up_down_north= new JPanel(new FlowLayout(FlowLayout.TRAILING));		
		p_up_down_north.add(LLogin);
		p_up_down_north.add(new JLabel(" | "));
		p_up_down_north.add(LJoin);		

		p_up_down.add(p_up_down_north, BorderLayout.NORTH);

		JPanel p_up_down_south = new JPanel(new BorderLayout());
		JScrollPane p_up_down_south_east = new JScrollPane(ta);		
		p_up_down_south_east.setBorder(new TitledBorder("일일 섭취 칼로리(Kcal)"));		

		p_up_down_south.add(p_up_down_south_east, BorderLayout.CENTER);

		JPanel p_up_down_south_west = new JPanel(new GridLayout(11,1));		
		p_up_down_south_west.setBorder(new TitledBorder("요 약"));
		p_up_down_south_west.add(new JLabel(" "));
		p_up_down_south_west.add(new JLabel(" "));

		JPanel p_west_kcal = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		p_west_kcal.add(new JLabel("∙ 권장 섭취량       "));
		p_west_kcal.add(tfKcal);
		p_west_kcal.add(new JLabel("Kcal"));
		p_up_down_south_west.add(p_west_kcal);
		JPanel p_west_func = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel func= new JLabel("    ※권장 칼로리 = {(본인키-100) × 0.9} × 활동지수    ");
		func.setFont(new Font("맑은고딕", Font.ITALIC, 11));
		func.setForeground(Color.RED);
		p_west_func.add(func);
		p_up_down_south_west.add(p_west_func);	
		JPanel p_west_tot = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		p_west_tot.add(new JLabel("∙ 현재 섭취량       "));
		p_west_tot.add(tfTot);
		p_west_tot.add(new JLabel("Kcal"));
		p_up_down_south_west.add(p_west_tot);
		p_up_down_south_west.add(new JLabel(" "));
		JPanel p_west_state = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel warn = new JLabel("      ※Warning※");
		warn.setFont(new Font("맑은고딕", Font.ITALIC, 13));
		warn.setForeground(Color.RED);

		p_west_state.add(warn);
		p_up_down_south_west.add(p_west_state);		
		JPanel p_west_tf = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_west_tf.add(tfState);
		p_up_down_south_west.add(p_west_tf);								

		p_up_down_south.add(p_up_down_south_west, BorderLayout.EAST);			
		p_up_down.add(p_up_down_south);		

		p_up.add(p_title, BorderLayout.NORTH);
		p_up.add(p_up_down, BorderLayout.CENTER);		

		JPanel p_down = new JPanel(new BorderLayout());
		p_down.setBorder(new TitledBorder("체중관리"));	

		JPanel p_down_top_weight = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		p_down_top_weight.add(new JLabel("현재 몸무게"));
		p_down_top_weight.add(tfUpdate);
		p_down_top_weight.add(new JLabel("Kg"));		
		p_down_top_weight.add(bWeight);
		p_down_top_weight.add(cb);
		p_down_top_weight.add(bCheck);		
		p_down.add(p_down_top_weight, BorderLayout.NORTH);

		JPanel p_down_bottom = new JPanel(new BorderLayout());		
		p_down.add(p_down_bottom, BorderLayout.CENTER);

		JPanel p_down_bottom_east = new JPanel(new BorderLayout());
		p_down_bottom_east.setBorder(new TitledBorder("칼로리&몸무게 변화량"));
		p_down_bottom_east.add(ta2);		
		p_down_bottom.add(p_down_bottom_east, BorderLayout.CENTER);

		JPanel p_down_bottom_east_combo = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		p_down_bottom_east_combo.add(cb);
		p_down_bottom_east_combo.add(bCheck);
		p_down_bottom_east.add(p_down_bottom_east_combo, BorderLayout.NORTH);

		JPanel p_down_bottom_west = new JPanel(new GridLayout(8,1));
		p_down_bottom_west.setBorder(new TitledBorder("회원 정보"));
		JPanel p_bottom_id = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_id.add(new JLabel("   아    이    디            ")); p_bottom_id.add(tfID);
		p_down_bottom_west.add(p_bottom_id);
		JPanel p_bottom_name = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_name.add(new JLabel("  이            름            ")); p_bottom_name.add(tfName);
		p_down_bottom_west.add(p_bottom_name);
		JPanel p_bottom_gender = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_gender.add(new JLabel("  성            별            ")); p_bottom_gender.add(tfGender);
		p_down_bottom_west.add(p_bottom_gender);
		JPanel p_bottom_age = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_age.add(new JLabel("  나    이(세)            ")); p_bottom_age.add(tfAge);
		p_down_bottom_west.add(p_bottom_age);
		JPanel p_bottom_height = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_height.add(new JLabel("  키(cm)                ")); p_bottom_height.add(tfHeight);
		p_down_bottom_west.add(p_bottom_height);
		JPanel p_bottom_weight = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_weight.add(new JLabel("  몸무게(Kg)            ")); p_bottom_weight.add(tfWeight);
		p_down_bottom_west.add(p_bottom_weight);
		JPanel p_bottom_etc = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_etc.add(new JLabel("  활 동 지 수            ")); p_bottom_etc.add(tfEtc);
		p_down_bottom_west.add(p_bottom_etc);
		JPanel p_bottom_diet = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		p_bottom_diet.add(new JLabel("  비    만    도            ")); p_bottom_diet.add(tfDiet);
		p_down_bottom_west.add(p_bottom_diet);

		p_down_bottom.add(p_down_bottom_west,BorderLayout.WEST); 

		JPanel p_down_menu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_down_menu.add(bInsert);
		p_down_menu.add(bShow);
		p_down_menu.add(bExit);		

		p_down.add(p_down_menu, BorderLayout.SOUTH);

		setLayout(new GridLayout(2,1));
		add(p_up);
		add(p_down);

		setSize(900,800);
		setVisible( true );		
	}
}

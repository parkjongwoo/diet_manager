package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.TableOrder;

import diet_manager.model.ViewModel;
import diet_manager.model.vo.Customer;
import diet_manager.model.vo.EatVO;
import diet_manager.model.vo.WeightVO;
import diet_manager.util.Util;

public class View extends JFrame {
//	JTextArea ta, ta2;JLabel 
	JTextField tfUpdate, tfID, tfWeight, tfHeight, tfName, tfGender, tfAge, tfEtc, tfKcal, tfTot, tfState, tfDiet;
	JComboBox cb;
	JLabel LLogin, LJoin,warn;
	JButton bInsert, bShow, bExit, bCheck, bWeight;
	JFreeChart chart_eat;
	JFreeChart chart_weight;
	String[] str = { "일별", "주별", "월별" };

	ViewModel db;

	public View() {
		addLayout();
		connectDB();
		eventProc();
		cusInfo();
		updateEatGraph();
	}

	

	public void eventProc() {
		EvtHdlr eh = new EvtHdlr();
		LinkMouseListener ml = new LinkMouseListener();

		LLogin.addMouseListener(ml);
		bInsert.addActionListener(eh);
		bShow.addActionListener(eh);
		bExit.addActionListener(eh);
		bCheck.addActionListener(eh);
		bWeight.addActionListener(eh);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				super.windowGainedFocus(e);
				updateData();
			}
		});
	}

	class EvtHdlr implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object evt = e.getSource();
//			System.out.println("이벤트:" + evt);
			if (evt == bInsert) {
				new FoodInsert();
			} else if (evt == bShow) {
				new DetailView();
			} else if (evt == bExit) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			} else if (evt == bCheck) {
				JOptionPane.showMessageDialog(null, "체크버튼");
			} else if (evt == bWeight) {
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
			if (l == LLogin) {
				if (ViewModel.loginUser == null) {
					new Login();
				} else {
					JOptionPane.showMessageDialog(null, ViewModel.loginUser.getCustName() + "님이 로그인 중입니다.");
				}
			} else if (l == LJoin) {
				if (ViewModel.loginUser == null) {
					new RegistView();
				} else {
					JOptionPane.showMessageDialog(null, ViewModel.loginUser.getCustName() + "님이 로그인 중입니다.");
				}
			}
		}
	}

	public void cusInfo() {
		// String id = tfID.getText();
		// try {
		// Customer c = db.cusInfo(id);
		// tfName.setText(c.getCustName());
		// tfGender.setText(c.getCustGender());
		// Calendar now = Calendar.getInstance();
		// Calendar birth = Calendar.getInstance();
		// birth.setTime(c.getCustBirth());
		// tfAge.setText(String.valueOf(now.get(Calendar.YEAR) -
		// birth.get(Calendar.YEAR)+1));
		// tfHeight.setText(String.valueOf(c.getCustHeight()));
		// tfWeight.setText(String.valueOf(c.getCustWeight()));
		// tfEtc.setText(String.valueOf(c.getCustEtc()));
		// tfUpdate.setText(String.valueOf(c.getCustWeight()));
		// diet();
		// }
		// catch(Exception e) {
		// System.out.println(e.getMessage());
		// int result = JOptionPane.showConfirmDialog(null,
		// "로그인하시겠습니까?","Error",JOptionPane.YES_NO_OPTION);
		// if(result==JOptionPane.OK_OPTION) {
		//
		// }
		// else {
		// setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// }
		// }
		if (ViewModel.loginUser == null) {
			int result = JOptionPane.showConfirmDialog(null, "로그인하시겠습니까?", "Error", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				new Login();
			}
		} else {
			JOptionPane.showMessageDialog(null, ViewModel.loginUser.getCustName() + "님이 로그인 중입니다.");
		}
	}
	
	private void updateEatGraph() {
		try {			
			((PiePlot)chart_eat.getPlot()).setDataset(getPieDataSet());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void updateWeightGraph() {
		try {			
			chart_weight.getXYPlot().setDataset(getWeightDataSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateData() {
		if (ViewModel.loginUser == null)
			return;
		try {
			db.checkPass(ViewModel.loginUser.getCustId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tfID.setText(ViewModel.loginUser.getCustId());
		tfName.setText(ViewModel.loginUser.getCustName());
		tfGender.setText(ViewModel.loginUser.getCustGender());
		Calendar now = Calendar.getInstance();
		Calendar birth = Calendar.getInstance();
		birth.setTime(ViewModel.loginUser.getCustBirth());
		tfAge.setText(String.valueOf(now.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1));
		tfHeight.setText(String.valueOf(ViewModel.loginUser.getCustHeight()));
		tfWeight.setText(String.valueOf(ViewModel.loginUser.getCustWeight()));
		tfEtc.setText(String.valueOf(ViewModel.loginUser.getCustEtc()));
		tfUpdate.setText(String.valueOf(ViewModel.loginUser.getCustWeight()));
		
		try {
			db.searchTodayEatList(ViewModel.loginUser.getCustId());
			db.searchWeightList(ViewModel.loginUser.getCustId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		updateEatGraph();
		updateWeightGraph();
		diet();
		
		
	}

	



	public void diet() {
		double goodCal = Math.round(((ViewModel.loginUser.getCustHeight() - 100) * 0.9)) *
				ViewModel.loginUser.getCustEtc();
		double todayCal = db.getTodayCal();
		double differ = Util.formating(todayCal - goodCal,0);
		tfKcal.setText(String.valueOf(goodCal));
		double diet = ViewModel.loginUser.getCustWeight() / (ViewModel.loginUser.getCustHeight() * 2 / 100);
		if (diet <= 18.5) {
			tfDiet.setText("저체중");
		} else if (diet <= 23 && diet > 18.5) {
			tfDiet.setText("정상");
		} else if (diet <= 25 && diet > 23) {
			tfDiet.setText("과체중");
		} else if (diet <= 30 && diet > 25) {
			tfDiet.setText("비만");
		} else if (diet > 30) {
			tfDiet.setText("고도비만");
		}
		
		tfTot.setText(String.valueOf(todayCal));
		StringBuffer sb = new StringBuffer();
		if(differ<100) {
			sb.append("칼로리 섭취가 ").append(differ).append("Cal 부족합니다.");
			warn.setVisible(false);
		}else if(differ>100) {
			sb.append("칼로리 섭취가 ").append(differ).append("Cal 많습니다.");
			warn.setVisible(true);
		}else {
			sb.append("적정량 섭취하셨습니다.");
			warn.setVisible(false);
		}
		tfState.setText(sb.toString());
	}

	public void modifyWeight() {
		if(ViewModel.loginUser==null)return;
		try {			
			db.modifyWeight(Double.parseDouble((tfUpdate.getText())), ViewModel.loginUser.getCustId());
			JOptionPane.showMessageDialog(null, "수정되었습니다.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "수정 실패" + e.getMessage());
		}
	}

	public void connectDB() {
		try {
			db = new ViewModel();
			System.out.println("고객디비 연결");
		} catch (Exception e) {
			System.out.println("고객디비 연결 실패:" + e.getMessage());

		}
	}

	public void addLayout() {
//		ta = new JTextArea("그래프 공간");
//		ta2 = new JTextArea("그래프 공간");
//		ta2.setLineWrap(true);
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
		tfID = new JTextField(8);
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
		tfState = new JTextField(20);
		tfState.setHorizontalAlignment(JTextField.CENTER);
		tfState.setBorder(BorderFactory.createEmptyBorder());

		cb = new JComboBox(str);

		JPanel p_up = new JPanel(new BorderLayout());

		JPanel p_title = new JPanel();
		JLabel LTitle = new JLabel("식단 관리 모니터링 시스템");
		LTitle.setFont(new Font("돋움", Font.BOLD, 25));
		p_title.add(LTitle);

		JPanel p_up_down = new JPanel(new BorderLayout());

		JPanel p_up_down_north = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		p_up_down_north.add(LLogin);
		p_up_down_north.add(new JLabel(" | "));
		p_up_down_north.add(LJoin);

		p_up_down.add(p_up_down_north, BorderLayout.NORTH);

		JPanel p_up_down_south = new JPanel(new BorderLayout());
		chart_eat = getPieChart();
		JScrollPane p_up_down_south_east = new JScrollPane(getChartPanel(chart_eat));
		p_up_down_south_east.setBorder(new TitledBorder("일일 섭취 칼로리(Kcal)"));

		p_up_down_south.add(p_up_down_south_east, BorderLayout.CENTER);

		JPanel p_up_down_south_west = new JPanel(new GridLayout(11, 1));
		p_up_down_south_west.setBorder(new TitledBorder("요 약"));
		p_up_down_south_west.add(new JLabel(" "));
		p_up_down_south_west.add(new JLabel(" "));

		JPanel p_west_kcal = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p_west_kcal.add(new JLabel("∙ 권장 섭취량       "));
		p_west_kcal.add(tfKcal);
		p_west_kcal.add(new JLabel("Kcal"));
		p_up_down_south_west.add(p_west_kcal);
		JPanel p_west_func = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel func = new JLabel("    ※권장 칼로리 = {(본인키-100) × 0.9} × 활동지수    ");
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
		warn = new JLabel("      ※Warning※");
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
		chart_weight = getLineChart();
		p_down_bottom_east.add(getChartPanel(chart_weight));
		p_down_bottom.add(p_down_bottom_east, BorderLayout.CENTER);

		JPanel p_down_bottom_east_combo = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		p_down_bottom_east_combo.add(cb);
		p_down_bottom_east_combo.add(bCheck);
		p_down_bottom_east.add(p_down_bottom_east_combo, BorderLayout.NORTH);

		JPanel p_down_bottom_west = new JPanel(new GridLayout(8, 1));
		p_down_bottom_west.setBorder(new TitledBorder("회원 정보"));
		JPanel p_bottom_id = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_id.add(new JLabel("   아    이    디            "));
		p_bottom_id.add(tfID);
		p_down_bottom_west.add(p_bottom_id);
		JPanel p_bottom_name = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_name.add(new JLabel("  이            름            "));
		p_bottom_name.add(tfName);
		p_down_bottom_west.add(p_bottom_name);
		JPanel p_bottom_gender = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_gender.add(new JLabel("  성            별            "));
		p_bottom_gender.add(tfGender);
		p_down_bottom_west.add(p_bottom_gender);
		JPanel p_bottom_age = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_age.add(new JLabel("  나    이(세)            "));
		p_bottom_age.add(tfAge);
		p_down_bottom_west.add(p_bottom_age);
		JPanel p_bottom_height = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_height.add(new JLabel("  키(cm)                "));
		p_bottom_height.add(tfHeight);
		p_down_bottom_west.add(p_bottom_height);
		JPanel p_bottom_weight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_weight.add(new JLabel("  몸무게(Kg)            "));
		p_bottom_weight.add(tfWeight);
		p_down_bottom_west.add(p_bottom_weight);
		JPanel p_bottom_etc = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_etc.add(new JLabel("  활 동 지 수            "));
		p_bottom_etc.add(tfEtc);
		p_down_bottom_west.add(p_bottom_etc);
		JPanel p_bottom_diet = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_bottom_diet.add(new JLabel("  비    만    도            "));
		p_bottom_diet.add(tfDiet);
		p_down_bottom_west.add(p_bottom_diet);

		p_down_bottom.add(p_down_bottom_west, BorderLayout.WEST);

		JPanel p_down_menu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_down_menu.add(bInsert);
		p_down_menu.add(bShow);
		p_down_menu.add(bExit);

		p_down.add(p_down_menu, BorderLayout.SOUTH);

		setLayout(new GridLayout(2, 1));
		add(p_up);
		add(p_down);

		setSize(900, 800);
		setVisible(true);
	}
	private Component getChartPanel(JFreeChart chart2) {
		ChartPanel cp = new ChartPanel(chart2,
				600,//가로사이즈
				150,//세로사이즈
				600,//최소가로사이즈
				150,//최소세로사이즈
				600,//최대가로사이즈
				150,false,false,false,false,false,false);//최소가로사이즈
				
		return cp;
	}
	
	private JFreeChart getPieChart()  {

		JFreeChart chart = ChartFactory.createPieChart("오늘 섭취량", // title
				getPieDataSet(), // dataset
				false, // legend
				true, // tooltips
				false); // url
		PiePlot p = (PiePlot)chart.getPlot();
		// 차트의 배경색 설정입니다.
		p.setBackgroundPaint(Color.white);
//		// 라벨 설정입니다. 
		p.setLabelFont(new Font("돋움", Font.BOLD, 8));
		chart.getTitle().setFont(new Font("돋움", Font.BOLD, 8));
//		chart.getLegend().setItemFont(new Font("돋움", Font.BOLD, 8));
		return chart;
	}
	private XYDataset getWeightDataSet()  {
		DefaultXYDataset dataSet = new DefaultXYDataset();
		TimeSeriesCollection colection = new TimeSeriesCollection();
		if(ViewModel.loginUser == null) 
			return dataSet;			
		
		ArrayList<WeightVO> list = db.weightList;
		// addValue() 메서드를 이용해서 값을 추가함
		dataSet.addSeries("체중", getSeriesData(list));

		return dataSet;
	}
	private double[][] getSeriesData(ArrayList<WeightVO> list) {
		double[][] seriese = new double[2][list.size()];
		for(int i=0;i<list.size();i++) {
			seriese[0][i] = list.get(i).getAdate().getTime();
			seriese[1][i] = list.get(i).getWeight();
		}
		return seriese;
	}



	private DefaultPieDataset getPieDataSet()  {
		DefaultPieDataset dataSet = new DefaultPieDataset();
		
		if(ViewModel.loginUser == null) 
			return dataSet;			
		
		ArrayList<EatVO> list = db.todayEatList;		
		double co=0,fat=0,pro=0;
		// addValue() 메서드를 이용해서 값을 추가함
		for (int i = 0; i < list.size(); i++) {
			co += list.get(i).getFco();
			fat += list.get(i).getFfat();
			pro += list.get(i).getFpro();
		}
//		double sum = co+fat+pro;
		dataSet.setValue("탄수화물",co);
		dataSet.setValue("지방",fat);
		dataSet.setValue("단백질",pro);
		
		return dataSet;
	}
//	private CategoryToPieDataset getPieDataSet()  {
//		CategoryToPieDataset dataSet = new CategoryToPieDataset(
//				getDataSet(),//변환해서 사용할 CategoryDataset
//				TableOrder.BY_ROW,
//				0);
//		
//		return dataSet;
//	}
	
	public JFreeChart getLineChart() {

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"몸무게 변화표", 
				"시간변화", 
				"변량", 
				getWeightDataSet()); // url
		XYPlot p = chart.getXYPlot();
		chart.getTitle().setFont(new Font("돋움", Font.BOLD, 8));
		// 차트의 배경색 설정입니다.
		p.setBackgroundPaint(Color.white);
		// 차트의 배경 라인 색상입니다.
		p.setRangeGridlinePaint(Color.gray);
		// X 축의 라벨 설정입니다. (보조 타이틀)
		p.getDomainAxis().setLabelFont(new Font("돋움", Font.BOLD, 8));
		// X 축의 도메인 설정입니다.
		p.getDomainAxis().setTickLabelFont(new Font("돋움", Font.BOLD, 8));
		// Y 축의 라벨 설정입니다. (보조 타이틀)
		p.getRangeAxis().setLabelFont(new Font("돋움", Font.BOLD, 8));
		// Y 축의 도메인 설정입니다.
		p.getRangeAxis().setTickLabelFont(new Font("돋움", Font.BOLD, 8));
		chart.getLegend().setItemFont(new Font("돋움", Font.BOLD, 8));
		return chart;
	}
}

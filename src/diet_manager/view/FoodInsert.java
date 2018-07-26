package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import diet_manager.model.FoodInsertModel;
import diet_manager.model.ViewModel;
import diet_manager.model.vo.EatVO;
import diet_manager.model.vo.FoodVO;
import diet_manager.util.Util;
import diet_manager.view.EatJPannel.TableModel;
import diet_manager.view.component.DatePicker;

public class FoodInsert extends JFrame {
	String[] paneNames = {"아침","점심","저녁"};
	EatJPannel breakfast;
	EatJPannel lunch;
	EatJPannel dinner;
	EatJPannel allDay;
	
	JTextField tfFood, tfKcal;
	JButton bSearch, bInsert, bModify, bDelete;
//	JTextArea ta;

	DatePicker dp_searchDate;
	JButton b_resetDate;

	JTable tableRecentList;
	VOTableModel tbModel;

	JTabbedPane pane;
	
	JFreeChart chart;
	
	FoodInsertModel db;

	public FoodInsert() {

		connectDB();
		addLayout();
		eventProc();
		searchEat();
	}

	public void addLayout() {
		breakfast = new EatJPannel(paneNames[0]);
		lunch = new EatJPannel(paneNames[1]);
		dinner = new EatJPannel(paneNames[2]);

		tfFood = new JTextField();
		tfKcal = new JTextField();
		bSearch = new JButton("검색");
		bInsert = new JButton("등록");
		bModify = new JButton("수정");
		bDelete = new JButton("선택 삭제");
//		ta = new JTextArea("그래프");
		
		JCheckBox checkBox = new JCheckBox();
		tbModel = new VOTableModel();
		tableRecentList = new JTable(tbModel);
		tableRecentList.getColumn("선택").setCellRenderer(dtcr);
		tableRecentList.getColumn("선택").setCellEditor(new DefaultCellEditor(checkBox));
		tableRecentList.getColumn("선택").setPreferredWidth(20);
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		
		pane = new JTabbedPane();
		pane.addTab("아침", breakfast);
		pane.addTab("점심", lunch);
		pane.addTab("저녁", dinner);
		updatePaneByTime();
		

		JPanel p_main_top = new JPanel();
		p_main_top.setBorder(new TitledBorder("날짜별 조회"));
		dp_searchDate = new DatePicker();
		b_resetDate = new JButton("조회");
		b_resetDate.setFont(new Font("맑은고딕", Font.BOLD, 12));
		p_main_top.add(dp_searchDate);
		p_main_top.add(b_resetDate);

		JPanel p_back = new JPanel();
		p_back.setLayout(new BorderLayout());

		JPanel p_east = new JPanel();
		p_east.setLayout(new BorderLayout());

		JPanel p_search = new JPanel();
		p_search.setBorder(new TitledBorder("음식 검색"));
		p_search.setLayout(new BorderLayout());

		JPanel p_search_food = new JPanel();
		p_search_food.setLayout(new GridLayout(1, 3));
		p_search_food.add(new JLabel("음식명"));
		p_search_food.add(tfFood);
		p_search_food.add(bSearch);
		p_search.add(p_search_food, BorderLayout.NORTH);
		p_east.add(p_search, BorderLayout.NORTH);

		JPanel p_list = new JPanel();
		p_list.setLayout(new BorderLayout());
		p_list.add(new JScrollPane(tableRecentList), BorderLayout.CENTER);

		JPanel p_layout = new JPanel();
		p_layout.setLayout(new BorderLayout());
		p_layout.add(bInsert, BorderLayout.EAST);
		p_list.add(p_layout, BorderLayout.SOUTH);

		p_east.add(p_list);

		JPanel p_west = new JPanel();
		p_west.setLayout(new BorderLayout());

		JPanel p_ingred = new JPanel();
		p_ingred.setLayout(new BorderLayout());
		p_ingred.add(pane, BorderLayout.NORTH);

		JPanel p_graph = new JPanel();
		p_graph.setBorder(new TitledBorder("섭취한 영양소"));	
		chart = getBarChart();
		p_graph.add(getChartPanel(chart));
		p_ingred.add(p_graph);

		JPanel p_button = new JPanel();
		p_button.setLayout(new GridLayout(1, 2));
		p_button.add(bModify);
		p_button.add(bDelete);

		JPanel p_kcal = new JPanel();
		p_kcal.setLayout(new GridLayout(1, 2));
		p_kcal.add(new JLabel("총 섭취량 (Kcal) : "));
		p_kcal.add(tfKcal);

		JPanel p_layout2 = new JPanel();
		p_layout2.setLayout(new BorderLayout());
		p_layout2.add(p_kcal, BorderLayout.EAST);
		p_layout2.add(p_button, BorderLayout.SOUTH);

		p_ingred.add(p_layout2, BorderLayout.SOUTH);

		p_west.add(p_ingred, BorderLayout.CENTER);

		setLayout(new BorderLayout());
		p_back.add(p_west, BorderLayout.CENTER);
		p_back.add(p_east, BorderLayout.EAST);
		p_back.add(p_main_top, BorderLayout.NORTH);
		add(p_back);

		setSize(1200, 800);
		setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

	private JFreeChart getBarChart() {

		JFreeChart chart = ChartFactory.createBarChart(getSelectedPaneName()+" 섭취량", // title
				"3대영양소", // categoryAxisLabel
				"섭취량", // valueAxisLabel
				getDataSet(), // dataset
				PlotOrientation.HORIZONTAL, // orientation
				true, // legend
				true, // tooltips
				false); // url
		CategoryPlot p = chart.getCategoryPlot();
		// 차트의 배경색 설정입니다.
		p.setBackgroundPaint(Color.white);
		// 차트의 배경 라인 색상입니다.
		p.setRangeGridlinePaint(Color.gray);
		// X 축의 라벨 설정입니다. (보조 타이틀)
		p.getDomainAxis().setLabelFont(new Font("돋움", Font.BOLD, 15));
		// X 축의 도메인 설정입니다.
		p.getDomainAxis().setTickLabelFont(new Font("돋움", Font.BOLD, 10));
		// Y 축의 라벨 설정입니다. (보조 타이틀)
		p.getRangeAxis().setLabelFont(new Font("돋움", Font.BOLD, 15));
		// Y 축의 도메인 설정입니다.
		p.getRangeAxis().setTickLabelFont(new Font("돋움", Font.BOLD, 10));
		// 범례 폰트 조정
		chart.getLegend().setItemFont(new Font("돋움", Font.BOLD, 10));
		//타이틀 폰트 조정
		chart.getTitle().setFont(new Font("돋움", Font.BOLD, 15));
		return chart;
	}
	
	private DefaultCategoryDataset getDataSet() {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		EatJPannel onShowPanel = ((EatJPannel)pane.getSelectedComponent());
		TableModel model = onShowPanel.tbModel;
		
		double co=0,fat=0,pro=0;
		// addValue() 메서드를 이용해서 값을 추가함
		for (int i = 0; i < model.getRowCount(); i++) {
			co += (Double)model.getValueAt(i, 3);
			fat += (Double)model.getValueAt(i, 5);
			pro += (Double)model.getValueAt(i, 4);
			

		}
		double sum = co+fat+pro;
		dataSet.addValue(co, Math.round(co/sum*100)+"%", "탄수화물");
		dataSet.addValue(fat,Math.round(fat/sum*100)+"%","지방");
		dataSet.addValue(pro,Math.round(pro/sum*100)+"%","단백질");
		return dataSet;
	}
	
	private void updatePaneByTime() {
		Calendar c = Calendar.getInstance();
		int h = c.get(Calendar.HOUR_OF_DAY);
		
		if(h<=10) {
			pane.setSelectedIndex(0);
		}else if(h>=16) {
			pane.setSelectedIndex(2);
		}else {
			pane.setSelectedIndex(1);
		}
	}

	private String getSelectedPaneName() {
		return paneNames[pane.getSelectedIndex()];
	}
	
	private void updateTabs(ArrayList<EatVO> list) {
		breakfast.changeData(list);
		lunch.changeData(list);
		dinner.changeData(list);
		
		tfKcal.setText(Util.formatingS(((EatJPannel)pane.getSelectedComponent()).getTotalCal(),2));
	}
	
	private void updateGraph() {		
		chart.getCategoryPlot().setDataset(getDataSet());
		
	}
	
	public void eventProc() {
		ButtonEventHandler btnHandler = new ButtonEventHandler();
		bSearch.addActionListener(btnHandler);
		tfFood.addActionListener(btnHandler);
		bInsert.addActionListener(btnHandler);
		bModify.addActionListener(btnHandler);
		bDelete.addActionListener(btnHandler);
		b_resetDate.addActionListener(btnHandler);
		pane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateGraph();
			}
		});
	}

	public void connectDB() {
		try {
			db = new FoodInsertModel();
		} catch (Exception e) {
			System.out.println("디비 연결 실패");
			e.printStackTrace();
		}
	}
	
	private void clearFoodTable() {
		((VOTableModel)tableRecentList.getModel()).data.clear();
		((VOTableModel)tableRecentList.getModel()).fireTableDataChanged();
		tfFood.setText(null);
	}
	
	public void SearchFood() {
		ArrayList<FoodVO> list = null;
		try {
			list = db.searchFood(tfFood.getText());
			tbModel.data = list;
			tbModel.fireTableDataChanged();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "검색실패");
			e.printStackTrace();
		}
	}
	
	public void searchEat() {
		try {
			ArrayList<EatVO> list = db.searchEat(ViewModel.loginUser.getCustId(),dp_searchDate.getDate());
			updateTabs(list);
			updateGraph();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "검색실패");
			e.printStackTrace();
		}
	}

	public void insertEat() {
		ArrayList<EatVO> list = new ArrayList<EatVO>();
		ArrayList<FoodVO> checkList = tbModel.data;
		try {
			for(int i=0;i<checkList.size();i++) {
				if(checkList.get(i).isChecked()) {
						FoodVO checkedVo = checkList.get(i);
						EatVO vo = new EatVO();
						vo.setAid(ViewModel.loginUser.getCustId());
						vo.setFid(checkedVo.getFid());
						vo.setEdate(dp_searchDate.getDate());
						vo.setEtime(getSelectedPaneName());
						vo.seteIntake(checkList.get(i).geteIntake());						
						list.add(vo);
				}
			}
			int resultCount = db.insertEat(list);
			searchEat();
			clearFoodTable();
			JOptionPane.showMessageDialog(this, "데이터가 입력되었습니다.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "검색실패");
			e.printStackTrace();
		}		
	}
	
	public class VOTableModel extends AbstractTableModel{

		ArrayList<FoodVO> data = new ArrayList<FoodVO>();
		String[] columnNames = { "선택", "음식명", "1회제공량(g)","칼로리","먹은비율" };
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		@Override
		public int getRowCount() {
			return data.size();
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			FoodVO temp = data.get(row);
			Object result = null;
			switch (col) {
			case 0:
				result = temp.isChecked();
				break;
			case 1:
				result = temp.getFname();
				break;
			case 2:
				result = temp.getFper();
				break;
			case 3:
				result = temp.getFcal();
				break;
			case 4:
				result = temp.geteIntake();
				break;
			}
			return result;
		}
		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex==4||columnIndex==0;
		}
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch(columnIndex) {
			case 0:
				data.get(rowIndex).setChecked((boolean)aValue);
				break;
			case 4:
				data.get(rowIndex).seteIntake(Double.parseDouble((String) aValue));
				break;
			}			
		}		
	}
	
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent
		(JTable table,Object value,boolean isSelected,boolean hasFocus, int row, int column) {
			
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(((Boolean)value).booleanValue());
			checkBox.setHorizontalAlignment(JLabel.CENTER);
			
			return checkBox;
		}
		
	};
	
	class ButtonEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Object o = ev.getSource();

			if (o == bSearch) {
				SearchFood();
			} else if (o == bInsert) {
				insertEat();
			} else if (o == bModify) {
//				tttt();
			} else if (o == bDelete) {
				
			} else if (o == b_resetDate) {
				searchEat();
			} else if (o == tfFood) {
				SearchFood();
			}
		}
	}
	
//	private void tttt() {
//		
//		db.tttt();
//	}
}

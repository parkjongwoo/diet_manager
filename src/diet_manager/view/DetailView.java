package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import diet_manager.model.DetailViewModel;
import diet_manager.model.ViewModel;
import diet_manager.model.vo.EatVO;
import diet_manager.util.Util;
import diet_manager.view.EatJPannel.TableModel;
import diet_manager.view.component.DatePicker;

public class DetailView extends JFrame {
//	JTextArea taGraph;
	DatePicker dp_searchDate;
	JButton btn, bDelete, bExit;
	JTable foodList;
	VOTableModel tbModel;
	JFreeChart chart;
	
	DetailView self;

//	CustomerModel db;
	DetailViewModel db_eat;

	public DetailView() {
		addLayout();
		eventProc();
//		connectDB();
		connectDB2();
		self = this;
		select_date();
	}

//	private void connectDB() {
//		try {
//			db = new CustomerModel();
//			System.out.println("디비연결 성공");
//		} catch (Exception e) {
//			System.out.println("대여디비 실패");
//		}
//	}

	private void connectDB2() {
		try {
			db_eat = new DetailViewModel();
			System.out.println("디비연결 성공");
		} catch (Exception e) {
			System.out.println("대여디비 실패");
		}
	}

	public void eventProc() {
		EvtHdlr eh = new EvtHdlr();
		btn.addActionListener(eh);
		bExit.addActionListener(eh);
	}

	class EvtHdlr implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object evt = e.getSource();
			if (evt == btn) {

				select_date();
			} else if (evt == bDelete) {

			} else if (evt == bExit) {
//				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
//				if (result == JOptionPane.OK_OPTION) {
//					self.dispose();
//
//				} else {
//					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//				}
				self.dispose();
			}
		}

		

	}
	public void select_date() {

		try {
			tbModel.data = db_eat.searchEat(ViewModel.loginUser.getCustId(), dp_searchDate.getDate());
			tbModel.fireTableDataChanged();
			updateGraph();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "목록실패:");
		}
	}
	public void addLayout() {
		Integer[] strM = new Integer[12];
		for (int i = 0; i < strM.length; i++) {
			strM[i] = i + 1;
		}
		Integer[] strD = new Integer[31];
		for (int i = 0; i < strD.length; i++) {
			strD[i] = i + 1;
		}

		Integer[] strY = new Integer[11];
		int i = 0;
		int thisyear = 2018;
		for (int y = thisyear - 5; i < strY.length; y++) {
			strY[i] = y;
			i++;
		}
		dp_searchDate = new DatePicker();
		btn = new JButton("조회");
		btn.setFont(new Font("맑은고딕", Font.BOLD, 12));
		bDelete = new JButton("선택 삭제");
		bDelete.setFont(new Font("맑은고딕", Font.BOLD, 15));
		bExit = new JButton("종료");
		bExit.setFont(new Font("맑은고딕", Font.BOLD, 15));
		tbModel = new VOTableModel();
		foodList = new JTable(tbModel);
		JCheckBox checkBox = new JCheckBox();

		foodList.getColumn("선택").setCellRenderer(dtcr);
		foodList.getColumn("선택").setCellEditor(new DefaultCellEditor(checkBox));
		checkBox.setHorizontalAlignment(JLabel.CENTER);

//		taGraph = new JTextArea();

		JPanel p_title = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel LTitle = new JLabel("상세 영양소 조회  ");
		LTitle.setFont(new Font("맑은고딕", Font.BOLD, 18));
		p_title.add(LTitle);

		JPanel p_main = new JPanel(new BorderLayout());
		JPanel p_main_top = new JPanel();
		p_main_top.setBorder(new TitledBorder("날짜별 조회"));
		p_main_top.add(dp_searchDate);
		p_main_top.add(btn);
		p_main.add(p_main_top, BorderLayout.NORTH);

		JPanel p_main_table = new JPanel(new GridLayout(2, 1));
		p_main_table.add(new JScrollPane(foodList));
		JPanel p_main_graph = new JPanel(new BorderLayout());		
		p_main_graph.setBorder(new TitledBorder("영양소별 섭취량(그래프)"));
		
		chart = getBarChart();
		p_main_graph.add(getChartPanel(chart));
		p_main_table.add(p_main_graph);
		p_main.add(p_main_table, BorderLayout.CENTER);

		JPanel p_main_menu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_main_menu.add(bDelete);
		p_main_menu.add(bExit);

		setLayout(new BorderLayout());
		add(p_title, BorderLayout.NORTH);
		add(p_main, BorderLayout.CENTER);
		add(p_main_menu, BorderLayout.SOUTH);

		setSize(750, 600);
		setVisible(true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void updateGraph() {		
		chart.getCategoryPlot().setDataset(getDataSet());
		
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

		JFreeChart chart = ChartFactory.createBarChart("일일 섭취량", // title
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
		
		
		
		double co=0,fat=0,pro=0;
		// addValue() 메서드를 이용해서 값을 추가함
		for (int i = 0; i < tbModel.getRowCount(); i++) {
			co += (Double)tbModel.getValueAt(i, 3);
			fat += (Double)tbModel.getValueAt(i, 5);
			pro += (Double)tbModel.getValueAt(i, 4);
			

		}
		double sum = co+fat+pro;
		dataSet.addValue(co, Math.round(co/sum*100)+"%", "탄수화물");
		dataSet.addValue(fat,Math.round(fat/sum*100)+"%","지방");
		dataSet.addValue(pro,Math.round(pro/sum*100)+"%","단백질");
		return dataSet;
	}
	
	class VOTableModel extends AbstractTableModel {

		ArrayList<EatVO> data = new ArrayList<EatVO>();
		String[] columnNames = { "선택", "식단명", "열량", "탄수화물", "단백질", "지방", "당류", "나트륨", "포화지방", "콜레스테롤", "트랜스지방", "1회제공량",
				"먹은비율" };

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
			EatVO temp = data.get(row);
			Object result = null;
			switch (col) {
			case 0:
				result = temp.isChecked();
				break;
			case 1:
				result = temp.getFname();
				break;
			case 2:
				result = Util.formating(temp.getFcal() * temp.geteIntake(), 2);
				break;
			case 3:
				result = Util.formating(temp.getFco() * temp.geteIntake(), 2);
				break;
			case 4:
				result = Util.formating(temp.getFpro() * temp.geteIntake(), 2);
				break;
			case 5:
				result = Util.formating(temp.getFfat() * temp.geteIntake(), 2);
				break;
			case 6:
				result = Util.formating(temp.getFsu() * temp.geteIntake(), 2);
				break;
			case 7:
				result = Util.formating(temp.getFna() * temp.geteIntake(), 2);
				break;
			case 8:
				result = Util.formating(temp.getFsat() * temp.geteIntake(), 2);
				break;
			case 9:
				result = Util.formating(temp.getFcho() * temp.geteIntake(), 2);
				break;
			case 10:
				result = Util.formating(temp.getFtran() * temp.geteIntake(), 2);
				break;
			case 11:
				result = temp.getFper();
				break;
			case 12:
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
			return columnIndex == 12 || columnIndex == 0;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				data.get(rowIndex).setChecked((boolean) aValue);
				break;
			case 12:
				data.get(rowIndex).seteIntake(Double.parseDouble((String) aValue));
				data.get(rowIndex).setChecked(true);
				break;
			}
		}
	}

	public DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(((Boolean) value).booleanValue());
			checkBox.setHorizontalAlignment(JLabel.CENTER);

			return checkBox;
		}

	};
}

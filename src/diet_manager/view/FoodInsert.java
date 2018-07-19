package diet_manager.view;

import java.awt.BorderLayout;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import diet_manager.model.FoodInsertModel;
import diet_manager.model.ViewModel;
import diet_manager.model.vo.EatVO;
import diet_manager.model.vo.FoodVO;
import diet_manager.view.component.DatePicker;

public class FoodInsert extends JFrame {
	String[] paneNames = {"아침","점심","저녁"};
	BFView breakfast;
	LCView lunch;
	DNView dinner;

	JTextField tfFood, tfKcal;
	JButton bSearch, bInsert, bModify, bDelete;
	JTextArea ta;

	DatePicker dp_searchDate;
	JButton b_resetDate;

	JTable tableRecentList;
	TableModel tbModel;

	JTabbedPane pane;

	FoodInsertModel db;

	public FoodInsert() {

		connectDB();
		addLayout();
		eventProc();

	}

	public void addLayout() {
		breakfast = new BFView();
		lunch = new LCView();
		dinner = new DNView();

		tfFood = new JTextField();
		tfKcal = new JTextField();
		bSearch = new JButton("검색");
		bInsert = new JButton("등록");
		bModify = new JButton("수정");
		bDelete = new JButton("선택 삭제");
		ta = new JTextArea("그래프");
		
		JCheckBox checkBox = new JCheckBox();
		tbModel = new TableModel();
		tableRecentList = new JTable(tbModel);
		tableRecentList.getColumn("선택").setCellRenderer(dtcr);
		tableRecentList.getColumn("선택").setCellEditor(new DefaultCellEditor(checkBox));
//		tableRecentList.getColumn("선택").setPreferredWidth(40);
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		
		pane = new JTabbedPane();
		pane.addTab("아침", breakfast);
		pane.addTab("점심", lunch);
		pane.addTab("저녁", dinner);
		pane.setSelectedIndex(2);

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
		p_graph.add(ta, BorderLayout.CENTER);
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
	
	private String getSelectedPaneName() {
		return paneNames[pane.getSelectedIndex()];
	}
	public void eventProc() {
		ButtonEventHandler btnHandler = new ButtonEventHandler();
		bSearch.addActionListener(btnHandler);
		tfFood.addActionListener(btnHandler);
		bInsert.addActionListener(btnHandler);
		bModify.addActionListener(btnHandler);
		bDelete.addActionListener(btnHandler);
	}

	public void connectDB() {
		try {
			db = new FoodInsertModel();
		} catch (Exception e) {
			System.out.println("디비 연결 실패");
			e.printStackTrace();
		}
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
			JOptionPane.showMessageDialog(this, resultCount+"개의 데이터가 입력되었습니다.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "검색실패");
			e.printStackTrace();
		}
		
	}
	class TableModel extends AbstractTableModel{

		ArrayList<FoodVO> data = new ArrayList<FoodVO>();
		String[] columnNames = { "선택", "음식명", "1회제공량(g)","먹은비율" };
		
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
				result = temp.getFcal();
				break;
			case 3:
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
			return columnIndex==3||columnIndex==0;
		}
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch(columnIndex) {
			case 0:
				data.get(rowIndex).setChecked((boolean)aValue);
				break;
			case 3:
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
				SearchFood();
			} else if (o == bDelete) {
				SearchFood();
			} else if (o == b_resetDate) {
				SearchFood();
			} else if (o == tfFood) {
				SearchFood();
			} else if (o == bSearch) {
				SearchFood();
			}
		}
	}	
}

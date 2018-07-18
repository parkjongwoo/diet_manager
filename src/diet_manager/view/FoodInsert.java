package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

public class FoodInsert extends JFrame {
	BFView breakfast;
	LCView lunch;
	DNView dinner;
	
	JTextField tfFood, tfKcal;
	JButton bSearch, bInsert, bModify, bDelete;
	JTextArea ta;
	
	JTable tableRecentList;
	tableModel tbModel;
	
	JTabbedPane pane;
	
	public FoodInsert() {
		breakfast = new BFView();
		lunch = new LCView();
		dinner = new DNView();
		
		tfFood= new JTextField();
		tfKcal = new JTextField();
		bSearch = new JButton("검색");
		bInsert = new JButton("등록");
		bModify = new JButton("수정");
		bDelete = new JButton("선택 삭제");
		ta = new JTextArea("그래프");
		
		tbModel = new tableModel();
		tableRecentList = new JTable(tbModel);
		
		pane = new JTabbedPane();
		pane.addTab("아침", breakfast);
		pane.addTab("점심", lunch);
		pane.addTab("저녁", dinner);
		pane.setSelectedIndex(2);
		
	}
	public void addLayout() {
		JPanel p_back = new JPanel();
		p_back.setLayout(new BorderLayout());
		
		JPanel p_east = new JPanel();
		p_east.setLayout(new BorderLayout());
		
		JPanel p_search = new JPanel();
		p_search.setBorder(new TitledBorder("음식 검색"));
		p_search.setLayout(new BorderLayout());
		
		JPanel p_search_food = new JPanel();
		p_search_food.setLayout(new GridLayout(1,3));
		p_search_food.add(new JLabel("음식명"));
		p_search_food.add(tfFood);
		p_search_food.add(bSearch);
		p_search.add(p_search_food, BorderLayout.NORTH);
		p_east.add(p_search, BorderLayout.NORTH);
		
		JPanel p_list = new JPanel();
		p_list.setLayout(new BorderLayout());
		p_list.add(new JScrollPane(tableRecentList),BorderLayout.CENTER);
		
		JPanel p_layout = new JPanel();
		p_layout.setLayout(new BorderLayout());
		p_layout.add(bInsert,BorderLayout.EAST);
		p_list.add(p_layout, BorderLayout.SOUTH);
		
		p_east.add(p_list);
			
		JPanel p_west = new JPanel();
		p_west.setLayout(new BorderLayout());
		
		JPanel p_ingred = new JPanel();
		p_ingred.setLayout(new BorderLayout());
		p_ingred.add(pane,BorderLayout.NORTH);
		
		JPanel p_graph = new JPanel();
		p_graph.setBorder(new TitledBorder("섭취한 영양소"));
		p_graph.add(ta,BorderLayout.CENTER);
		p_ingred.add(p_graph);
		
		
		JPanel p_button = new JPanel();
		p_button.setLayout(new GridLayout(1,2));
		p_button.add(bModify);
		p_button.add(bDelete);
		
		JPanel p_kcal = new JPanel();
		p_kcal.setLayout(new GridLayout(1,2));
		p_kcal.add(new JLabel("총 섭취량 (Kcal) : "));
		p_kcal.add(tfKcal);
		
		JPanel p_layout2 = new JPanel();
		p_layout2.setLayout(new BorderLayout());
		p_layout2.add(p_kcal, BorderLayout.EAST);
		p_layout2.add(p_button, BorderLayout.SOUTH);
		
		p_ingred.add(p_layout2,BorderLayout.SOUTH);	
		
		p_west.add(p_ingred,BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		p_back.add(p_west,BorderLayout.CENTER);
		p_back.add(p_east,BorderLayout.EAST);
		add(p_back);
		

		setSize(1200,800);
		setVisible( true );

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	class tableModel extends AbstractTableModel { 
		  
		ArrayList data = new ArrayList();
		String [] columnNames = {"선택","음식명","먹은양(g)"};

		    public int getColumnCount() { 
		        return columnNames.length; 
		    } 
		     
		    public int getRowCount() { 
		        return data.size(); 
		    } 

		    public Object getValueAt(int row, int col) { 
				ArrayList temp = (ArrayList)data.get( row );
		        return temp.get( col ); 
		    }
		    
		    public String getColumnName(int col){
		    	return columnNames[col];
		    }
	}
	
	public void eventProc() {
		ButtonEventHandler btnHandler = new ButtonEventHandler();
		bSearch.addActionListener(btnHandler);
		
	}
	
	class ButtonEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Object o = ev.getSource();
			
			if(o==bSearch) {
				SearchFood();
			}
		}
	}
	
	public void SearchFood() {
		
	}
}


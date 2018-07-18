package foodmanagement;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import foodmanagement.View.EvtHdlr;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailView extends JFrame {
	JTextArea taGraph;
	JComboBox cbY, cbM, cbD;
	JButton btn, bDelete, bExit;
	JTable foodList;
	TableModel tbModel;
	
	DetailView self;
	
	public DetailView() {
		addLayout();
		eventProc();
		self = this;
	}
	public void eventProc() {
		EvtHdlr eh = new EvtHdlr();
				
		bExit.addActionListener(eh);
	}
	class EvtHdlr implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object evt = e.getSource();
			if(evt==btn) {
				
			}
			else if(evt==bDelete) {
				
			}
			else if(evt==bExit) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?","종료",JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.OK_OPTION) {
					self.dispose();
					
				}
				else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		}
		
	}
	public void addLayout() {
		Integer[] strM = new Integer[12];
		for(int i=0; i<strM.length; i++) {
			strM[i]=i+1;
		}
		Integer[] strD = new Integer[31];
		for(int i=0; i<strD.length; i++) {
			strD[i]=i+1;
		}
		
		Integer[] strY = new Integer[11];
		int i=0;
		int thisyear = 2018;
		for(int y=thisyear-5; i<strY.length; y++) {			
			strY[i]=y;
			i++;
		}		
		cbY = new JComboBox(strY);
		cbM = new JComboBox(strM);
		cbD = new JComboBox(strD);
		btn = new JButton("조회");
		btn.setFont(new Font("맑은고딕", Font.BOLD, 12));
		bDelete = new JButton("선택 삭제");
		bDelete.setFont(new Font("맑은고딕", Font.BOLD, 15));
		bExit = new JButton("종료");
		bExit.setFont(new Font("맑은고딕", Font.BOLD, 15));
		tbModel = new TableModel();
		foodList = new JTable(tbModel);
		taGraph = new JTextArea();
		
		Calendar c = Calendar.getInstance();	
		cbY.setSelectedItem(c.get(Calendar.YEAR));
		cbM.setSelectedItem(c.get(Calendar.MONTH)+1);
		cbD.setSelectedItem(c.get(Calendar.DATE));
		
		JPanel p_title = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		JLabel LTitle = new JLabel("상세 영양소 조회  ");
		LTitle.setFont(new Font("맑은고딕", Font.BOLD, 18));		
		p_title.add(LTitle);
		
		JPanel p_main = new JPanel(new BorderLayout());
		JPanel p_main_top = new JPanel();
		p_main_top.setBorder(new TitledBorder("날짜별 조회"));
		p_main_top.add(cbY); p_main_top.add(new JLabel("년"));		
		p_main_top.add(cbM); p_main_top.add(new JLabel("월"));
		p_main_top.add(cbD); p_main_top.add(new JLabel("일"));	
		p_main_top.add(btn);
		p_main.add(p_main_top, BorderLayout.NORTH);
		
		JPanel p_main_table = new JPanel(new GridLayout(2,1));
		p_main_table.add(new JScrollPane(foodList));
		JPanel p_main_graph = new JPanel(new BorderLayout());
		p_main_graph.setBorder(new TitledBorder("영양소별 섭취량(그래프)"));
		p_main_graph.add(taGraph);
		p_main_table.add(p_main_graph);
		p_main.add(p_main_table, BorderLayout.CENTER);
		
		JPanel p_main_menu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_main_menu.add(bDelete);
		p_main_menu.add(bExit);
		
		setLayout(new BorderLayout());
		add(p_title, BorderLayout.NORTH);
		add(p_main, BorderLayout.CENTER);
		add(p_main_menu, BorderLayout.SOUTH);
		
		setSize(750,600);
		setVisible(true);		
	}
	class TableModel extends AbstractTableModel { 

		ArrayList data = new ArrayList();
		String [] columnNames = {"선택","음식명","열량","탄수화물", "단백질", "지방", "당류", "나트륨", "포화지방", "콜레스테롤", "트랜스지방"};

		//=============================================================
		// 1. 기본적인 TabelModel  만들기
		// 아래 세 함수는 TabelModel 인터페이스의 추상함수인데
		// AbstractTabelModel에서 구현되지 않았기에...
		// 반드시 사용자 구현 필수!!!!

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
}

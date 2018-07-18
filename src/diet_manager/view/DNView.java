package diet_manager.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


public class DNView extends JPanel {
	JTable tableRecentList;
	tableModel tbModel;
	
	public DNView() {
		
		addLayout();
	}
	
	public void addLayout() {
		tbModel = new tableModel();
		tableRecentList = new JTable(tbModel);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(tableRecentList),BorderLayout.CENTER);
		
		
//		setSize(600,500);
//		setVisible(true);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class tableModel extends AbstractTableModel {
		ArrayList data = new ArrayList();
		String [] columnNames = {"선택","식단명","열량","탄수화물","단백질","지방","당류","나트륨","포화지방","콜레스테롤","트랜스지방"};
		
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

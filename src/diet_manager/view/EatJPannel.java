package diet_manager.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import diet_manager.model.vo.EatVO;
import diet_manager.util.Util;

public class EatJPannel extends JPanel {

	public JTable tableRecentList;
	public TableModel tbModel;
	public ArrayList<EatVO> list;
	
	private String eatTime;
	
	public EatJPannel(String eatTime) {
		this.eatTime = eatTime;
		addLayout();
	}
	
	public void addLayout() {
		tbModel = new TableModel();
		tableRecentList = new JTable(tbModel);
		JCheckBox checkBox = new JCheckBox();
		
		tableRecentList.getColumn("선택").setCellRenderer(dtcr);
		tableRecentList.getColumn("선택").setCellEditor(new DefaultCellEditor(checkBox));
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(tableRecentList), BorderLayout.CENTER);

	}

	public void changeData(ArrayList<EatVO> list) {
		if(this.list!=null) this.list.clear();
		else
			this.list = new ArrayList<EatVO>();
		
		for(int i=0;i<list.size();i++) {
			EatVO vo = list.get(i);
//			System.out.println("eatTime:"+eatTime+" vo.getEtime():"+vo.getEtime());
			if(vo.getEtime().equals(eatTime))
				this.list.add(vo);
		}
		tbModel.data = this.list;
		tbModel.fireTableDataChanged();
	}
	
	public double getTotalCal() {
		double result=0.0;
		
		for(int i=0;i<tbModel.getRowCount();i++) {
			result+=(double)tbModel.getValueAt(i, 2);
		}
		return result;
	}
	
	class TableModel extends AbstractTableModel {
		
		ArrayList<EatVO> data = new ArrayList<EatVO>();
		String[] columnNames = { "선택", "식단명", "열량", "탄수화물", "단백질", "지방",
				"당류", "나트륨", "포화지방", "콜레스테롤", "트랜스지방" ,"1회제공량","먹은비율"};
		
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
				result = Util.formating(temp.getFcal() * temp.geteIntake(),2);
				break;
			case 3:
				result = Util.formating(temp.getFco() * temp.geteIntake(),2);
				break;
			case 4:
				result = Util.formating(temp.getFpro() * temp.geteIntake(),2);
				break;
			case 5:
				result = Util.formating(temp.getFfat() * temp.geteIntake(),2);
				break;
			case 6:
				result = Util.formating(temp.getFsu() * temp.geteIntake(),2);
				break;
			case 7:
				result = Util.formating(temp.getFna() * temp.geteIntake(),2);
				break;
			case 8:
				result = Util.formating(temp.getFsat() * temp.geteIntake(),2);
				break;
			case 9:
				result = Util.formating(temp.getFcho() * temp.geteIntake(),2);
				break;
			case 10:
				result = Util.formating(temp.getFtran() * temp.geteIntake(),2);
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
			return columnIndex==12||columnIndex==0;
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch(columnIndex) {
			case 0:
				data.get(rowIndex).setChecked((boolean)aValue);
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
		public Component getTableCellRendererComponent
		(JTable table,Object value,boolean isSelected,boolean hasFocus, int row, int column) {
			
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(((Boolean)value).booleanValue());
			checkBox.setHorizontalAlignment(JLabel.CENTER);
			
			return checkBox;
		}
		
	};
}

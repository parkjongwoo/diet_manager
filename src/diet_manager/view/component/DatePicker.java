package diet_manager.view.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class DatePicker extends JPanel implements ActionListener{
	
	Calendar cal;
	
	int yearRange;
	
	JComboBox<Integer> yearPick;
	JComboBox<Integer> monthPick;
	JComboBox<Integer> datePick;
	
	public DatePicker() {
		this(Calendar.getInstance());
	}	

	public DatePicker(Calendar c) {
		this(c, 100);
	}
	
	public DatePicker(Calendar c, int yearRange) {
		cal = c;
		this.yearRange = yearRange;
		init();
	}
	
	private void init() {
		initUI();
	}	
	
	private void initUI() {
		yearPick = new JComboBox<Integer>();
		monthPick = new JComboBox<Integer>();
		datePick = new JComboBox<Integer>();
		
		add(yearPick);
		add(monthPick);
		add(datePick);
		
		initPickers();
		eventProc();
	}
	
	private void initPickers() {
		int firstYear = cal.get(Calendar.YEAR)-yearRange;
		int lastYear = cal.get(Calendar.YEAR);
		int lastDateOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				
		for(int i=lastYear;i>=firstYear;i--) {
			yearPick.addItem(i);
		}		
		
		for(int i=1;i<=12;i++) {
			monthPick.addItem(i);
		}
		
		for(int i=1;i<=lastDateOfMonth;i++) {
			datePick.addItem(i);
		}
		
		yearPick.setSelectedItem(cal.get(Calendar.YEAR));
		monthPick.setSelectedItem(cal.get(Calendar.MONTH)+1);
		datePick.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
	}
	
	private void eventProc() {
		yearPick.addActionListener(this);
		monthPick.addActionListener(this);
		datePick.addActionListener(this);
	}
	
	private void updateDatePicker() {
		
		int lastDateOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int datePickerItemCount = datePick.getItemCount();
		
		if(lastDateOfMonth > datePickerItemCount) {
			for(int i=datePickerItemCount+1;i<=lastDateOfMonth;i++) {
				datePick.addItem(i);
			}
		}else if(lastDateOfMonth < datePickerItemCount) {			
			for(int i=datePickerItemCount;i>lastDateOfMonth;i--) {
				datePick.removeItem(i);
			}
		}
	}
	
	private void updateData() {
		int year = (Integer)yearPick.getSelectedItem();
		int month = (Integer)monthPick.getSelectedItem()-1;
		Integer datepick = (Integer)datePick.getSelectedItem();
		
		cal.set(Calendar.DATE, Math.min(getLastDate(year,month),datepick));
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);			
	}
	
	private int getLastDate(int year, int month) {
		Calendar test = Calendar.getInstance();
		test.set(Calendar.DATE, 1);
		test.set(Calendar.YEAR, year);
		test.set(Calendar.MONTH, month);
		return test.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();		
		updateData();		
		if(o==yearPick||o==monthPick) {
			updateDatePicker();
		}		
	}

	public Date getDate() {
		return cal.getTime();
	}
}

package crawling.view;

import java.awt.Window;

import javax.swing.JDialog;

import crawling.model.CrawlingModel;;

public class PerspectiveView extends JDialog {
	
	CrawlingModel db;
		
	public PerspectiveView(Window owner) {
		super(owner);
		addLayout();
		eventProc();
	}
	
	private void addLayout() {
		// TODO Auto-generated method stub
		
	}
	
	private void eventProc() {
		// TODO Auto-generated method stub
		
	}
	public void setModel(CrawlingModel db) {
		this.db = db;
	}
}

package jfreechartdemo;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import jfreechartdemo.view.ChartDemo;

public class JFreeChartDemoMain extends JFrame {
	
	public JFreeChartDemoMain() {
		
		ChartDemo cd = new ChartDemo();
		JScrollPane sp = new JScrollPane(cd);
		
		add(sp);
		
		setSize(800, 600);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new JFreeChartDemoMain();
	}

}

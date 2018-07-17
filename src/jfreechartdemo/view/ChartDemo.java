package jfreechartdemo.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.TableOrder;

import jfreechartdemo.model.ChartDemoModel;

public class ChartDemo extends JPanel {

	ChartDemoModel db;

	public ChartDemo() {
		connectDB();
		addLayout();
		eventProc();
	}

	private void connectDB() {
		try {
			db = new ChartDemoModel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void eventProc() {

	}

	private void addLayout() {
		try {
			this.setLayout(new GridLayout(0, 2));
//			ChartPanel cp = new ChartPanel(getBarChart(),
//					300,//가로사이즈
//					400,//세로사이즈
//					300,//최소가로사이즈
//					400,//최소세로사이즈
//					300,//최대가로사이즈
//					400,//최소가로사이즈
//					false,false,false,false,false,false);
			ChartPanel cp = new ChartPanel(getBarChart());
			add(cp);
			
			ChartPanel cp2 = new ChartPanel(getAreaChart());
			add(cp2);
			
			ChartPanel cp3 = new ChartPanel(getPieChart());
			add(cp3);
			
			ChartPanel cp4 = new ChartPanel(getLineChart());
			add(cp4);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JFreeChart getBarChart() throws Exception {

		JFreeChart chart = ChartFactory.createBarChart("Bar Chart", // title
				"JOB", // categoryAxisLabel
				"PERSONS", // valueAxisLabel
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
		return chart;
	}

	public JFreeChart getAreaChart() throws Exception {

		JFreeChart chart = ChartFactory.createAreaChart("Area Chart", // title
				"JOB", // categoryAxisLabel
				"PERSONS", // valueAxisLabel
				getDataSet(), // dataset
				PlotOrientation.VERTICAL, // orientation
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
		return chart;
	}
	
	public JFreeChart getLineChart() throws Exception {

		JFreeChart chart = ChartFactory.createLineChart("Line Chart", // title
				"JOB", // categoryAxisLabel
				"PERSONS", // valueAxisLabel
				getDataSet(), // dataset
				PlotOrientation.VERTICAL, // orientation
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
		return chart;
	}
	
	public JFreeChart getPieChart() throws Exception {

		JFreeChart chart = ChartFactory.createPieChart("pie chart", // title
				getPieDataSet(), // dataset
				true, // legend
				true, // tooltips
				false); // url
		PiePlot p = (PiePlot)chart.getPlot();
		// 차트의 배경색 설정입니다.
		p.setBackgroundPaint(Color.white);
//		// 라벨 설정입니다. 
		p.setLabelFont(new Font("돋움", Font.BOLD, 15));

		return chart;
	}

	private DefaultCategoryDataset getDataSet() throws Exception {
		ArrayList<ArrayList<String>> list = db.searchJobList();

		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		String category = "job";

		// addValue() 메서드를 이용해서 값을 추가함
		for (int i = 0; i < list.size(); i++) {
			int cnt = Integer.parseInt(list.get(i).get(0));
			String jobName = list.get(i).get(1);
			dataSet.addValue(cnt, category, jobName);

		}

		return dataSet;
	}
	private CategoryToPieDataset getPieDataSet() throws Exception {
		CategoryToPieDataset dataSet = new CategoryToPieDataset(
				getDataSet(),//변환해서 사용할 CategoryDataset
				TableOrder.BY_ROW,
				0);
		
		return dataSet;
	}
}

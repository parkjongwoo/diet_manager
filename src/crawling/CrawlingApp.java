package crawling;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import crawling.model.CrawlingModel;
import crawling.view.JobView;
import crawling.view.PerspectiveView;
import diet_manager.util.Util;

public class CrawlingApp extends JFrame {

	JobView jobView;
	PerspectiveView perView;

	CrawlingModel db;
	
	protected static Logger logger = Logger.getLogger(CrawlingApp.class.getName());

	public CrawlingApp() {
		try {
			System.out.println(Util.encrypt("1"));
			jobView = new JobView();
			perView = new PerspectiveView(this);

			db = new CrawlingModel();

			jobView.setModel(db);
			perView.setModel(db);
			
			add("Center", jobView );
			
			setSize(800,600);
			setVisible( true );

			setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		} catch (Exception e) {
			CrawlingApp.Logg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void Logg(String log) {
		logger.info(log);
	}
	public static void main(String[] args) {
		new CrawlingApp();
	}

}

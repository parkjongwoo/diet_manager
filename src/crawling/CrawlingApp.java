package crawling;

import javax.swing.JFrame;

import crawling.model.CrawlingModel;
import crawling.view.JobView;
import crawling.view.PerspectiveView;

public class CrawlingApp extends JFrame {

	JobView jobView;
	PerspectiveView perView;

	CrawlingModel db;

	public CrawlingApp() {
		try {

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
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new CrawlingApp();
	}

}

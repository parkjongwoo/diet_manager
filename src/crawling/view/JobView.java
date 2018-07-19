package crawling.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawling.CrawlingApp;
import crawling.model.CrawlingModel;
import diet_manager.model.vo.FoodVO;

public class JobView extends JPanel implements ActionListener, Runnable {

	CrawlingModel db;

	JButton jb_start, jb_stop, jb_viewPer;
	JLabel jl_cate, jl_start, jl_end, jl_now;
	JTextField jtf_cate, jtf_start, jtf_end, jtf_now;
	JTextArea jta_log;

	boolean working = false;

	public JobView() {
		super();
		connectDB();
		addLayout();
		eventProc();
	}

	private void connectDB() {
		try {
			db = new CrawlingModel();
		} catch (Exception e) {
			CrawlingApp.Logg(e.getMessage());
			e.printStackTrace();
		}
	}

	private void addLayout() {
		// TODO Auto-generated method stub
		setLayout(new BorderLayout());

		JPanel jp_c = new JPanel();
		jb_start = new JButton("작업시작");
		jb_stop = new JButton("작업종료");
		jb_viewPer = new JButton("현황보기");
		jp_c.add(jb_start);
		jp_c.add(jb_stop);
		jp_c.add(jb_viewPer);
		add(jp_c, BorderLayout.NORTH);

		JPanel jp_l = new JPanel(new GridLayout(4, 4));
		jl_cate = new JLabel("카테고리:");
		jl_start = new JLabel("시작페이지:");
		jl_end = new JLabel("종료페이지:");
		jl_now = new JLabel("현재작업중페이지:");

		jtf_cate = new JTextField(10);
		jtf_start = new JTextField(10);
		jtf_end = new JTextField(10);
		jtf_now = new JTextField(10);
		jtf_now.setEditable(false);

		jp_l.add(jl_cate);
		jp_l.add(jtf_cate);
		jp_l.add(jl_start);
		jp_l.add(jtf_start);
		jp_l.add(jl_end);
		jp_l.add(jtf_end);
		jp_l.add(jl_now);
		jp_l.add(jtf_now);

		add(jp_l, BorderLayout.CENTER);

		jta_log = new JTextArea(20, 10);

		add(new JScrollPane(jta_log), BorderLayout.SOUTH);

	}

	private void eventProc() {
		jb_start.addActionListener(this);
		jb_stop.addActionListener(this);
		jb_viewPer.addActionListener(this);
	}

	public void setModel(CrawlingModel db) {
		this.db = db;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == jb_start) {
			if (working)
				return;
			working = true;
			start();
		} else if (o == jb_stop) {
			working = false;
		} else if (o == jb_viewPer) {
			openPopup();
		}
	}

	private void openPopup() {

	}

	private void start() {
		Thread job = new Thread(this);
		job.start();
	}

	private String getCurrentTime() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	/**
	 * 카테고리, 페이지 인덱스값으로 웹페이지 호출하여 크롤링 작업.
	 * 
	 * @param categoryIndex
	 *            1:식품 2: 3:가공식품
	 * @param pageIndex
	 *            각 카테고리별 페이지 번호
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private void crawlingData(int categoryIndex, int pageIndex) throws Exception {

		CrawlingApp.Logg(" Start Date : " + getCurrentTime() + " 크롤링 [시작] 카테고리:" + categoryIndex + " 페이지:" + pageIndex);

		Document doc = Jsoup.parse(getResponseString(categoryIndex, pageIndex));
		Elements es = doc.select("#tab1 .nutrition-result tbody tr");
		
		Iterator<Element> ei = es.iterator();
		FoodVO dao = null;
		ArrayList<FoodVO> list = new ArrayList<FoodVO>();

		for (int i = 0; ei.hasNext(); i++) {
			Element e = ei.next();

			if (i % 2 == 0) {
				// sb.append('[').append(j).append("]:");
				// sb.append("id:").append(e.child(0).text()).append(",
				// 1회제공량:").append(e.child(2).text().trim())
				// .append(", 열량:").append(e.child(3).text().trim()).append(", 탄수화물:")
				// .append(e.child(4).text().trim()).append(",
				// 단백질:").append(e.child(5).text().trim())
				// .append(", 지방:").append(e.child(6).text().trim());
				dao = new FoodVO();
				
				dao.setFper(getDoubleFromValue(e.child(2).text().trim()));// 1회제공량
				dao.setFcal(getDoubleFromValue(e.child(3).text().trim()));// 칼로리
				dao.setFco(getDoubleFromValue(e.child(4).text().trim()));// 탄수화물
				dao.setFpro(getDoubleFromValue(e.child(5).text().trim()));// 단백질
				dao.setFfat(getDoubleFromValue(e.child(6).text().trim()));// 지방
			} else {
				// sb.append(", 이름:").append(e.child(0).child(0).text().trim()).append(", 당류:")
				// .append(e.child(1).text().trim()).append(",
				// 나트륨:").append(e.child(2).text().trim())
				// .append(", 콜레:").append(e.child(3).text().trim()).append(", 포화지방:")
				// .append(e.child(4).text().trim()).append(",
				// 트랜스지방:").append(e.child(5).text().trim())
				// .append('\n');
				//
				dao.setFname(e.child(0).child(0).text().trim());// 음식이름
				dao.setFsu(getDoubleFromValue(e.child(1).text().trim()));// 당류
				dao.setFna(getDoubleFromValue(e.child(2).text().trim()));// 나트륨
				dao.setFcho(getDoubleFromValue(e.child(3).text().trim()));// 콜레스테롤
				dao.setFsat(getDoubleFromValue(e.child(4).text().trim()));// 포화지방
				dao.setFtran(getDoubleFromValue(e.child(5).text().trim()));// 트랜스지방
				list.add(dao);
			}

		}
		db.insertFoodInfo(list);
		CrawlingApp.Logg(" End Date : " + getCurrentTime() + " 크롤링 [종료] 카테고리:" + categoryIndex + " 페이지:" + pageIndex);
	}

	/**
	 * 웹페이지 요청해서 String으로 변환.
	 * 
	 * @param categoryIndex
	 *            분류번호
	 * @param pageIndex
	 *            페이지번호
	 * @return 요청한 페이지를 문자열로 변경한 값.
	 * @throws Exception
	 */
	private String getResponseString(int categoryIndex, int pageIndex) throws Exception {
		HttpPost http = new HttpPost(getCrawlURLByCateAndPage(categoryIndex, pageIndex));
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response = httpClient.execute(http);
		HttpEntity entity = response.getEntity();
		ContentType contentType = ContentType.getOrDefault(entity);
		Charset charset = contentType.getCharset();

		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
		StringBuffer sb = new StringBuffer();

		String line = "";

		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		return sb.toString();
	}

	private double getDoubleFromValue(String value) {
		try {
			if("N/A".equals(value)||"trace".equals(value)) {
				value = "0";
			}else if (value.indexOf(",") >= 0) {
				value = value.replaceAll(",", "");
			}
			
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			System.out.println(value+ "값을 숫자로 변경 불가능. 0으로 대체****");
			return 0;
		}
	}

	@Override
	public void run() {
		int cate = Integer.parseInt(jtf_cate.getText());
		int page_start = Integer.parseInt(jtf_start.getText());
		int page_end = Integer.parseInt(jtf_end.getText());
		int page_now = page_start;

		while (working) {
			final Integer pnow = new Integer(page_now);

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					jtf_now.setText(String.valueOf(pnow));
				}
			});

			try {
				crawlingData(cate, page_now);
				
			} catch (NumberFormatException e) {
				CrawlingApp.Logg(String.format("NumberFormatException: cate:%d page_start:%d page_now:%d", cate,
						page_start, page_now));
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				CrawlingApp.Logg(String.format("ClientProtocolException: cate:%d page_start:%d page_now:%d", cate,
						page_start, page_now));
				e.printStackTrace();
			} catch (IOException e) {
				CrawlingApp.Logg(
						String.format("IOException: cate:%d page_start:%d page_now:%d", cate, page_start, page_now));
				e.printStackTrace();
			} catch (Exception e) {
				CrawlingApp.Logg(e.getMessage());
				e.printStackTrace();
			}finally {
				page_now++;
				if (page_now > page_end) {
					working = false;
				}
			}
		}
	}

	public String getCrawlURLByCateAndPage(int category, int page) {
		return "https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/"
				+ "foodnutrient/simpleSearch.do?menu_no=2805&menu_grp=" + "MENU_NEW03&code4=" + category + "&&page="
				+ page;
	}
}

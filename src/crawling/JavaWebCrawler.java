package crawling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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


public class JavaWebCrawler {

	public static String getCurrentDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		return sdf.format(new Date());

	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		System.out.println(Double.parseDouble("-0.09"));
//		// 1. 가져오기전 시간 찍기
//		System.out.println(" Start Date : " + getCurrentDate());
//
//		// 2. 가져올 HTTP 주소 세팅
//		HttpPost http = new HttpPost(getCrawlURLByCateAndPage(1,1));
//
//		// 3. 가져오기를 실행할 클라이언트 객체 생성
//		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//
//		// 4. 실행 및 실행 데이터를 Response 객체에 담음
//		HttpResponse response = httpClient.execute(http);
//
//		// 5. Response 받은 데이터 중, DOM 데이터를 가져와 Entity에 담음
//		HttpEntity entity = response.getEntity();
//
//		// 6. Charset을 알아내기 위해 DOM의 컨텐트 타입을 가져와 담고 Charset을 가져옴
//		ContentType contentType = ContentType.getOrDefault(entity);
//		Charset charset = contentType.getCharset();
//		
//		// 7. DOM 데이터를 한 줄씩 읽기 위해 Reader에 담음 (InputStream / Buffered 중 선택은 개인취향)
//		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
//
//		// 8. 가져온 DOM 데이터를 담기위한 그릇
//		StringBuffer sb = new StringBuffer();
//
//		// 9. DOM 데이터 가져오기
//		String line = "";
//		while ((line = br.readLine()) != null) {
//			sb.append(line + "\n");
//		}
//
//		// 10. 가져온 아름다운 DOM을 보자
//		//System.out.println(sb.toString());
//
//		// 11. Jsoup으로 파싱해보자.
//		Document doc = Jsoup.parse(sb.toString());
//		Elements es = doc.select(".nutrition-result tbody tr");
////		System.out.println("nutrition-result''s-------first Element\n"+es.toString());
//		
//		sb.setLength(0);
//		Iterator<Element> ei = es.iterator();
//		for(int i=0,j=1;ei.hasNext();i++) {
//			Element e = ei.next();
//			
//			if(i%2==0) {
//				sb.append('[').append(j).append("]:");
//				sb.append("id:").append(e.child(0).text()).
//				append(", 1회제공량:").append(e.child(2).text().trim()).
//				append(", 열량:").append(e.child(3).text().trim()).
//				append(", 탄수화물:").append(e.child(4).text().trim()).
//				append(", 단백질:").append(e.child(5).text().trim()).
//				append(", 지방:").append(e.child(6).text().trim());
//			}else {
//				sb.append(", 이름:").append(e.child(0).child(0).text().trim()).
//				append(", 당류:").append(e.child(1).text().trim()).
//				append(", 나트륨:").append(e.child(2).text().trim()).
//				append(", 콜레:").append(e.child(3).text().trim()).
//				append(", 포화지방:").append(e.child(4).text().trim()).
//				append(", 트랜스지방:").append(e.child(5).text().trim()).append('\n');
//				j++;
//			}
//			
//		}
//		System.out.println(sb);
//		
//		// 참고 - Jsoup에서 제공하는 Connect 처리
////		Document doc2 = Jsoup.connect("https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/foodnutrient/simpleSearch.do?menu_grp=MENU_NEW03&menu_no=2805").get();
////		 System.out.println(doc2.data());
//		
//		// 12. 얼마나 걸렸나 찍어보자
//		System.out.println(" End Date : " + getCurrentDate());

	}
	
	public static String getCrawlURLByCateAndPage(int category, int page) {
		return "https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/foodnutrient/simpleSearch.do?menu_no=2805&menu_grp=MENU_NEW03&code4="+category+"&&page="+page;
	}

}

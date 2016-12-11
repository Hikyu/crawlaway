package space.kyu.crawlaway;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.processor.Processor;

class AVSOProcessor implements Processor {
	public static final String domain = "https://avso.pw";
	public static final String startUrl = "https://avso.pw/cn";
	private  Map<String, AVItem> detailPage;
	private  Map<String, AVItem> downloadPage;
	
	{
		detailPage = new ConcurrentHashMap<>();
		downloadPage = new ConcurrentHashMap<>();
	}

	public void processPage(Page page) {
		AVItem item = new AVItem();
		String content = page.getContent();
		Document document = Jsoup.parse(content);
		Elements waterfall = document.select("#waterfall");
		Elements moveBox = waterfall.select("a.movie-box");
		Iterator<Element> moveIterator = moveBox.iterator();
		while (moveIterator.hasNext()) {
			Element i = (Element) moveIterator.next();
			String url = i.attr("href");
			Elements img = i.select("div.photo-frame").select("img");
			Element image = img.first();
			String imgUrl = image.attr("src");
			String title = image.attr("title");
			Elements info = i.select("div.photo-info").select("date");
			Element infomation = info.first();
			String designation = infomation.text();
			
			item.setDesignation(designation);
			item.setImgURL(imgUrl);
			item.setName(title);
			detailPage.put(url, item);
//			System.out.println("TITLE: " + title);
//			System.out.println("DESIGNATION: " + designation);
//			System.out.println("URL: " + url);
//			System.out.println("IMAGEURL: " + imgUrl);
//			System.out.println("********************************");
		}
		Elements pagination = document.select("ul.pagination");
		Elements number = pagination.select("li").select("a");
		Iterator<Element> pageIterator = number.iterator();
		while (pageIterator.hasNext()) {
			Element p = (Element) pageIterator.next();
			String href = p.attr("href");
			String targetUrl = domain + href;
			page.addTargetRequest(targetUrl);
		}

	}
	
	public Map<String, AVItem> getDetailPage(){
		return detailPage;
	}
}
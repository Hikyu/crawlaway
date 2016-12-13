package space.kyu.crawlaway;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.Request;
import space.kyu.crawlaway.processor.Processor;

class AVSOProcessor implements Processor {
	public static final String domain = "https://avso.pw";
	public static final String startUrl = "https://avso.pw/cn";
	private  Map<String, AVItem> detailPage;
	
	{
		detailPage = new ConcurrentHashMap<>();
	}

	public void processPage(Page page) {
		String oriUrl = page.getRequest().getUrl();
		if (oriUrl.endsWith("avso.pw/cn") || oriUrl.contains("avso.pw/cn/page")) {
			handleIndexPage(page);
		} else if(oriUrl.contains("avso.pw/cn/movie")){
			handleDetailPage(page);
		} else {
			System.out.println(oriUrl);
		}
//		} else if (oriUrl.contains("search")) {
//			handleDownloadePage(page);
//		} else if (oriUrl.contains("btso.pw/magnet/detail")) {
//			handleMagnetPage(page);
//		}
	}
	
	private void handleMagnetPage(Page page) {
		// TODO Auto-generated method stub
		
	}

	private void handleDownloadePage(Page page) {
		String content = page.getContent();
		AVItem item = (AVItem) page.getExtraInfo();
		Document document = Jsoup.parse(content);
		Elements rows = document.select("div.data-list").select("row");
		Iterator<Element> iterator = rows.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			Elements a = element.select("a");
			if (a.size() != 0) {
				String url = a.first().attr("href");
				System.out.println(url);
				Request request = new Request(url);
				request.setExtraInfo(item);
			}
		}
	}

	private void handleDetailPage(Page page) {
		AVItem item = (AVItem) page.getExtraInfo();
		String content = page.getContent();
		String actorName=null,duration=null,desi=null,date=null,series=null,imgUrl = null;
		StringBuilder builder = new StringBuilder();
		Document document = Jsoup.parse(content);
		try {
			Elements bigImage = document.select("a.bigImage");
			imgUrl = bigImage.first().attr("href");
			
			Elements actor = document.select("a.avatar-box");
			actorName = actor.first().select("span").text();
			
			Elements info = document.select("div.col-md-3");
			Elements subinfo = info.select("p");
			desi = subinfo.get(0).select("span").get(1).text();
			date = subinfo.get(1).text();
			date = date.substring(date.indexOf(":")+1).trim();
			duration = subinfo.get(2).text();
			duration = duration.substring(duration.indexOf(":")+1).trim();
			series = subinfo.get(6).select("a").text();
			Elements cates = subinfo.get(8).select("span");
			Iterator<Element> iterator = cates.iterator();
			builder = new StringBuilder();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				String cate = element.select("a").text();
				builder.append(cate).append("\t");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		item.setActor(actorName);
		item.setCategory(builder.toString());
		item.setDesignation(desi);
		item.setDuration(duration);
		item.setDate(date);
		item.setSeries(series);
		item.setImgURL(imgUrl);
		
		String target = document.select("div.ptb-10").get(2).select("a").attr("href");
		Request request = new Request(target);
		request.setExtraInfo(item);
		page.addTargetRequest(request);
	}

	private void handleIndexPage(Page page) {
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
			String title = image.attr("title");
			item.setTitle(title);
			Request request = new Request(url);
			request.setExtraInfo(item);
			page.addTargetRequest(request);
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

}
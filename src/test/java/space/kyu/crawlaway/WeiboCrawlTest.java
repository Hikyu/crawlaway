package space.kyu.crawlaway;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import space.kyu.crawlaway.entity.Config;
import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.processor.Processor;

public class WeiboCrawlTest {
	public static void main(String[] args) {
		Crawler crawler = Crawler.create(new WeiboPageProcessor());
		Config config = new Config();
		config.setReTryTime(3).setSleepTime(1000).setThreadNum(4).setTimeOut(3000);
		config.addUrl("http://weibo.com/");
		config.setExitEmptyQueueTime(10);
		crawler.setCrawlConfig(config);
		crawler.run();
	}

}

class WeiboPageProcessor implements Processor {
	public void processPage(Page page) {
		String content = page.getContent();
		Document document = Jsoup.parse(content);
		Elements select = document.select("div.pl_unlogin_home_topic");
		System.out.println(page.getStatusCode());
		System.out.println(page.getRequest().getUrl());
		System.out.println(content);
	}

}

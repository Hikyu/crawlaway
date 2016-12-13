package space.kyu.crawlaway;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.accessibility.internal.resources.accessibility;

import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.Request;
import space.kyu.crawlaway.processor.Processor;

public class DetailPageProcessor implements Processor{
	private  Map<String, AVItem> downloadPage;
	{
		downloadPage = new ConcurrentHashMap<>();
	}
	@Override
	public void processPage(Page page) {
		
	}

}

package space.kyu.crawlaway;

import space.kyu.crawlaway.entity.Config;

public class AVSOTest {

	public static void main(String[] args) {
		Crawler crawler = Crawler.create(new AVSOProcessor());
		Config config = new Config();
		config.setReTryTime(3)
			  .setSleepTime(5000)//ms
			  .setThreadNum(4)
			  .setTimeOut(3000)//ms
			  .setExitEmptyQueueTime(60)//s
			  .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36")
			  .addHeader("Host", "avso.pw")
		      .addUrl(AVSOProcessor.startUrl);
		crawler.setCrawlConfig(config);
//		crawler.addPipeline(new ConsolePipeline());
		crawler.run();
	}
}



package space.kyu.crawlaway;
import space.kyu.crawlaway.downloader.Downloader;
import space.kyu.crawlaway.pipeline.Pipeline;
import space.kyu.crawlaway.processor.Processor;
import space.kyu.crawlaway.scheduler.Scheduler;

/**
 * 爬虫调度类
 * @author yukai
 * @date 2016年12月8日
 */
public class Crawler {
	private Downloader downloader;
	private Pipeline pipeline;
	private Processor processor;
	private Scheduler scheduler;
}

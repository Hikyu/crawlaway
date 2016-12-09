package space.kyu.crawlaway;
import java.util.concurrent.Callable;

import space.kyu.crawlaway.downloader.Downloader;
import space.kyu.crawlaway.entity.Config;
import space.kyu.crawlaway.entity.CountableExectors;
import space.kyu.crawlaway.entity.CrawlReport;
import space.kyu.crawlaway.pipeline.Pipeline;
import space.kyu.crawlaway.processor.Processor;
import space.kyu.crawlaway.scheduler.Scheduler;

/**
 * 爬虫调度类
 * @author yukai
 * @date 2016年12月8日
 */
public class Crawler implements Runnable{
	private Downloader downloader;
	private Pipeline pipeline;
	private Processor processor;
	private Scheduler scheduler;
	private Config crawlConfig;
	private CountableExectors exectors;
	
	private void initComponent() {
		
	}
	
	public void run() {
		initComponent();
	}
	
	/**
	 * 异步方式执行
	 * 2016年12月9日
	 */
	public void startAsync() {
		Thread thread = new Thread(this);
		thread.setDaemon(false);
		thread.start();
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public Crawler setDownloader(Downloader downloader) {
		this.downloader = downloader;
		return this;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public Crawler setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
		return this;
	}

	public Processor getProcessor() {
		return processor;
	}

	public Crawler setProcessor(Processor processor) {
		this.processor = processor;
		return this;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public Crawler setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		return this;
	}

	public Config getCrawlConfig() {
		return crawlConfig;
	}

	public Crawler setCrawlConfig(Config crawlConfig) {
		this.crawlConfig = crawlConfig;
		return this;
	}

	
}

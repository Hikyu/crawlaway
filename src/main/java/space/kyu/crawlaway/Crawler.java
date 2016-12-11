package space.kyu.crawlaway;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.kyu.crawlaway.downloader.Downloader;
import space.kyu.crawlaway.downloader.HttpClientDownloader;
import space.kyu.crawlaway.entity.Config;
import space.kyu.crawlaway.entity.CountableExectors;
import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.Request;
import space.kyu.crawlaway.pipeline.Pipeline;
import space.kyu.crawlaway.processor.Processor;
import space.kyu.crawlaway.scheduler.BlockQueueScheduler;
import space.kyu.crawlaway.scheduler.DuplicateRemovedScheduler;
import space.kyu.crawlaway.scheduler.Scheduler;

/**
 * 爬虫调度类 非线程安全
 * 
 * @author yukai
 * @date 2016年12月8日
 */
public class Crawler implements Runnable {
	private Downloader downloader;
	private List<Pipeline> pipelines;
	private Processor pageProcessor;
	private Scheduler scheduler;
	private Config crawlConfig;
	private CountableExectors exectors;
	private ExecutorService executorService;

	private volatile int currentState = STATE_INIT;
	private static final int STATE_INIT = 0;
	private static final int STATE_STOP = -1;
	private static final int STATE_RUNNING = 1;
	private final AtomicLong totalPageCount = new AtomicLong(0);
	private final AtomicLong errorPageCount = new AtomicLong(0);
	private final Logger logger = LoggerFactory.getLogger(Crawler.class);
	private String crawlID;

	static {
		org.apache.log4j.BasicConfigurator.configure();
	}
	{
		pipelines = new ArrayList<Pipeline>();
	}

	public static Crawler create(Processor pageProcessor) {
		return new Crawler(pageProcessor);
	}

	private Crawler(Processor pageProcessor) {
		this.pageProcessor = pageProcessor;
	}

	private void init() {
		if (crawlConfig == null) {
			crawlConfig = new Config();
		}

		if (downloader == null) {
			downloader = new HttpClientDownloader();
		}

		if (scheduler == null) {
			scheduler = new BlockQueueScheduler();
			((DuplicateRemovedScheduler) scheduler).setOpen(crawlConfig.ifRemoveDupRequest());
		}

		if (executorService == null) {
			exectors = new CountableExectors(crawlConfig.getThreadNum());
		} else {
			exectors = new CountableExectors(crawlConfig.getThreadNum(), executorService);
		}

		List<Request> startRequests = crawlConfig.getStartRequests();
		addRequests(startRequests);
	}

	private void addRequests(List<Request> requests) {
		for (Request request : requests) {
			scheduler.push(request);
		}
	}

	private void checkIfRunning() {
		if (currentState == STATE_RUNNING) {
			throw new IllegalStateException("Crawler 已经在运行...");
		}
	}

	public void run() {
		checkRunningState();
		logger.info("Crawler:{} 开始运行...", getCrawlID());
		init();
		while (!Thread.currentThread().isInterrupted() && currentState == STATE_RUNNING) {
			Request request = scheduler.poll(crawlConfig.getExitEmptyQueueTime(), TimeUnit.SECONDS);
			if (request == null) {
				if (exectors.getAlive() == 0) {
					logger.info("Crawler:{} 任务结束", getCrawlID());
					logger.info("抓取url总数:{}, 其中抓取失败数量:{}", totalPageCount.get(), errorPageCount.get());
					break;
				}
			}
			final Request requestFinal = request;
			exectors.execute(new Runnable() {

				public void run() {
					try {
						processRequest(requestFinal);
					} catch (Exception e) {
						logger.error("处理  {} 时遇到错误...", requestFinal.toString());
						errorPageCount.incrementAndGet();
					} finally {
						totalPageCount.incrementAndGet();
					}
				}
			});
		}
		stopCrawl();

	}

	private void processRequest(Request request) {
		Page page = downloader.download(request, crawlConfig);
		if (page == null) {
			throw new RuntimeException("页面下载失败...");
		}
		pageProcessor.processPage(page);
		addTargetRequest(page);
		if (!page.getProcessResult().isSkip()) {
			for (Pipeline pipeline : pipelines) {
				pipeline.pipeline(page.getProcessResult());
			}
		}
		sleep(crawlConfig.getSleepTime());
	}

	private void addTargetRequest(Page page) {
		List<Request> targetRequests = page.getTargetRequests();
		addRequests(targetRequests);
	}

	private void sleep(int sleepTime) {
		if (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkRunningState() {
		checkIfRunning();
		currentState = STATE_RUNNING;
	}

	public String getCrawlID() {
		if (crawlConfig.getDomain() != null) {
			return crawlConfig.getDomain();
		}
		crawlID = UUID.randomUUID().toString();
		return crawlID;
	}

	/**
	 * 异步方式执行 2016年12月9日
	 */
	public void startAsync() {
		checkIfRunning();
		Thread thread = new Thread(this);
		thread.setDaemon(false);
		thread.start();
	}

	public void start() {
		checkIfRunning();
		run();
	}

	public Crawler setDownloader(Downloader downloader) {
		checkIfRunning();
		this.downloader = downloader;
		return this;
	}

	public Crawler addPipeline(Pipeline pipeline) {
		checkIfRunning();
		pipelines.add(pipeline);
		return this;
	}

	public Crawler setScheduler(Scheduler scheduler) {
		checkIfRunning();
		this.scheduler = scheduler;
		return this;
	}

	public Crawler setCrawlConfig(Config crawlConfig) {
		checkIfRunning();
		this.crawlConfig = crawlConfig;
		return this;
	}

	public void stopCrawl() {
		if (currentState != STATE_RUNNING) {
			return;
		}
		currentState = STATE_STOP;
		close();
	}

	public void close() {
		destroyEach(downloader);
		destroyEach(pageProcessor);
		destroyEach(scheduler);
		for (Pipeline pipeline : pipelines) {
			destroyEach(pipeline);
		}
		exectors.shutdown();
	}

	private void destroyEach(Object object) {
		if (object instanceof Closeable) {
			try {
				((Closeable) object).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}

package space.kyu.crawlaway.entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CountableExectors {

	private int threadNum;
	private ExecutorService executorService;
	private AtomicInteger threadAlive = new AtomicInteger(0);
	private ReentrantLock reentrantLock = new ReentrantLock();
	private Condition condition = reentrantLock.newCondition();

	public int getAlive() {
		return threadAlive.get();
	}

	public CountableExectors(int threadNum) {
		this.threadNum = threadNum;
		this.executorService = Executors.newFixedThreadPool(threadNum);
	}

	public CountableExectors(int threadNum, ExecutorService executorService) {
		this.threadNum = threadNum;
		this.executorService = executorService;
	}

	public void execute(final Runnable runnable) {
		if (threadAlive.get() >= threadNum) {
			try {
				reentrantLock.lock();
				while (threadAlive.get() >= threadNum) {
					try {
						condition.await();
					} catch (InterruptedException e) {
					}
				}
			} finally {
				reentrantLock.unlock();
			}
		}
		threadAlive.incrementAndGet();
		executorService.execute(new Runnable() {
			public void run() {
				try {
					runnable.run();
				} finally {
					try {
						reentrantLock.lock();
						threadAlive.decrementAndGet();
						condition.signal();
					} finally {
						reentrantLock.unlock();
					}
				}
			}
		});
	}

	public void shutdown() {
		executorService.shutdown();
	}
}

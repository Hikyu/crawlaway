package space.kyu.crawlaway.scheduler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.kyu.crawlaway.entity.Request;

public class BlockQueueScheduler extends DuplicateRemovedScheduler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private LinkedBlockingQueue<Request> blockQueue;

	{
		blockQueue = new LinkedBlockingQueue<Request>();
	}

	@Override
	protected void pushWhenNoDuplicate(Request request) {
		blockQueue.add(request);
	}

	public Request poll(long timeout, TimeUnit unit) {
		try {
			return blockQueue.poll(timeout, unit);
		} catch (InterruptedException e) {
			logger.error("poll error! "+ e.getMessage());
		}
		return null;
	}

}

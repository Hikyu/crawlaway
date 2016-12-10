package space.kyu.crawlaway.scheduler;

import java.util.concurrent.TimeUnit;

import space.kyu.crawlaway.entity.Request;

/**
 * 下载队列接口 实现类需保证线程安全
 * 
 * @author yukai
 * @date 2016年12月9日
 */
public interface Scheduler {
	public void push(Request request);

	public Request poll(long timeout, TimeUnit unit);
}

package space.kyu.crawlaway.scheduler;

import space.kyu.crawlaway.entity.Request;

/**
 * 下载队列接口
 * @author yukai
 * @date 2016年12月9日
 */
public interface Scheduler {
	public void push(Request request);
	
	public Request poll();
}

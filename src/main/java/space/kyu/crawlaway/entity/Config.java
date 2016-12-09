package space.kyu.crawlaway.entity;

/**
 * 下载页面时的一些配置
 * 
 * @author yukai
 * @date 2016年12月9日
 */
public class Config {
	private int sleepTime;
	private int reTryTime;

	public int getSleepTime() {
		return sleepTime;
	}

	public Config setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}

	public int getReTryTime() {
		return reTryTime;
	}

	public Config setReTryTime(int reTryTime) {
		this.reTryTime = reTryTime;
		return this;
	}

}

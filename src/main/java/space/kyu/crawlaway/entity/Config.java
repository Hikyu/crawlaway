package space.kyu.crawlaway.entity;

import java.util.List;
import java.util.Map;

/**
 * 爬取页面的配置
 * 
 * @author yukai
 * @date 2016年12月9日
 */
public class Config {
	private int sleepTime;
	private int reTryTime;
	private String domain;
	private Map<String, String> cookies;
	private List<Request> startRequests;
	private int threadNum = 1;

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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Map<String, String> getAllCookies() {
		return cookies;
	}

	public Config setCookies(String cookies) {
		//TODO 解析多个cookies拼接的字符串
		return this;
	}
	
	public Config addCookie(String key, String value) {
		cookies.put(key, value);
		return this;
	}

	public List<Request> getStartRequests() {
		return startRequests;
	}

	public Config setStartRequests(List<Request> startRequests) {
		this.startRequests = startRequests;
		return this;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public Config setThreadNum(int threadNum) {
		this.threadNum = threadNum;
		return this;
	}
	
	

}

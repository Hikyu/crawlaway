package space.kyu.crawlaway.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 爬取页面的配置
 * 
 * @author yukai
 * @date 2016年12月9日
 */
public class Config {
	// 抓取间隔时间
	private int sleepTime;
	// 抓取队列为空时程序等待时间
	private int exitEmptyQueueTime = 10 * 60 * 1000;
	private String domain;
	private Map<String, String> cookies;
	private List<Request> startRequests;
	private int threadNum = 1;
	private boolean removeDuplicateRequest = true;
	private int timeOut = 5000;
	private Map<String, String> headers;
	private int reTryTime = 0;
	private Set<Integer> acceptCodes;

	{
		cookies = new LinkedHashMap<String, String>();
		startRequests = new ArrayList<Request>();
		headers = new HashMap<String, String>();
		acceptCodes = new HashSet<>();
		acceptCodes.add(200);
	}

	
	public int getSleepTime() {
		return sleepTime;
	}

	public Config setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
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
		String[] cookiesArray = cookies.trim().split(";");
		for (String cookie : cookiesArray) {
			String[] keyValue = cookie.trim().split("=");
			this.cookies.put(keyValue[0], keyValue[1]);
		}
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

	public Config addUrl(String url) {
		Request request = new Request(url);
		startRequests.add(request);
		return this;
	}
	
	public Config addUrl(String url, Object info) {
		Request request = new Request(url);
		request.setExtraInfo(info);
		startRequests.add(request);
		return this;
	}
	
	public Config addRequest(Request request) {
		startRequests.add(request);
		return this;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public Config setThreadNum(int threadNum) {
		if (threadNum <= 0) {
			throw new IllegalArgumentException("线程数不得少于1!");
		}
		this.threadNum = threadNum;
		return this;
	}

	public int getExitEmptyQueueTime() {
		return exitEmptyQueueTime;
	}

	public Config setExitEmptyQueueTime(int seconds) {
		this.exitEmptyQueueTime = seconds;
		return this;
	}

	public boolean ifRemoveDupRequest() {
		return removeDuplicateRequest;
	}

	public Config setRemoveDupRequest(boolean removeDuplicateRequest) {
		this.removeDuplicateRequest = removeDuplicateRequest;
		return this;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public Config setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		return this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Config addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public int getReTryTime() {
		return reTryTime;
	}

	public Config setReTryTime(int reTryTime) {
		this.reTryTime = reTryTime;
		return this;
	}
	
	public void addAcceptCode(int code) {
		acceptCodes.add(code);
	}

	public Set<Integer> getAcceptCodes() {
		return acceptCodes;
	}

	public void setAcceptCodes(Set<Integer> acceptCodes) {
		this.acceptCodes = acceptCodes;
	}

	
}

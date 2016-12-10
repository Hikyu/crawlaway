package space.kyu.crawlaway.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载的页面
 * 
 * @author yukai
 * @date 2016年12月9日
 */
public class Page {
	private String content;
	private int statusCode;
	private Request request;
	private ResultItems processResult;
	private List<Request> targetRequests;
	
	{
		targetRequests = new ArrayList<Request>();
		processResult = new ResultItems();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public ResultItems getProcessResult() {
		return processResult;
	}

	public List<Request> getTargetRequests() {
		return targetRequests;
	}

	public void addTargetRequest(Request request) {
		targetRequests.add(request);
	}
	
	public void addTargetRequest(String url) {
		addTargetRequest(new Request(url));
	}
	
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
		processResult.setRequest(request);
	}
	
	
}

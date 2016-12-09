package space.kyu.crawlaway.entity;
/**
 * 下载请求
 * @author yukai
 * @date 2016年12月9日
 */
public class Request {
	private String url;
	private String method;
	private String postData;
	private long priority;
	
	@Override
	public String toString() {
		return "Request.url: " + url;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || ! (obj instanceof Request)) {
			return false;
		}
		
		Request o = (Request)obj;
		if (url.equals(o.url)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return url.hashCode();
	}
	
	public String getUrl() {
		return url;
	}
	public Request setUrl(String url) {
		this.url = url;
		return this;
	}
	public String getMethod() {
		return method;
	}
	public Request setMethod(String method) {
		this.method = method;
		return this;
	}
	public String getPostData() {
		return postData;
	}
	public Request setPostData(String postData) {
		this.postData = postData;
		return this;
	}
	public long getPriority() {
		return priority;
	}
	public Request setPriority(long priority) {
		this.priority = priority;
		return this;
	}
	
	
}

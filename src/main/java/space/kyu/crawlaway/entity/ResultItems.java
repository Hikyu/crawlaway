package space.kyu.crawlaway.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 处理某个页面获得的结果
 * 
 * @author yukai
 * @date 2016年12月9日
 */
public class ResultItems {
	private Request request;
	private Map<String, Object> results;
	private boolean skip;
	
	{
		results = new LinkedHashMap<String, Object>();
	}

	public Request getRequest() {
		return request;
	}

	public ResultItems setRequest(Request request) {
		this.request = request;
		return this;
	}

	public Map<String, Object> getAllResults() {
		return results;
	}

	public ResultItems setAllResults(Map<String, Object> result) {
		this.results = result;
		return this;
	}

	public boolean isSkip() {
		return skip;
	}

	public ResultItems setSkip(boolean skip) {
		this.skip = skip;
		return this;
	}

	public String getUrl() {
		return request.getUrl();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		Object o = results.get(key);
		if (o == null) {
			return null;
		}
		return (T) results.get(key);
	}

	public <T> void putField(String key, T value) {
		results.put(key, value);
	}
}

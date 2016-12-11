package space.kyu.crawlaway.downloader;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.kyu.crawlaway.entity.Config;
import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.Request;
import space.kyu.crawlaway.utils.UrlUtil;

public class HttpClientDownloader implements Downloader {
	private PoolingHttpClientConnectionManager connectionManager;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public HttpClientDownloader() {
		Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		connectionManager = new PoolingHttpClientConnectionManager(reg);
		connectionManager.setDefaultMaxPerRoute(100);
	}

	public Page download(Request request, Config crawlConfig) {
		logger.info("下载页面:{}", request.getUrl());
		CloseableHttpResponse resp = null;
		try {
			HttpUriRequest httpUriRequest = getHttpUriRequest(request, crawlConfig);
			resp = getHttpClient(crawlConfig).execute(httpUriRequest);
			Page page = handleResponse(resp, request);
			if (crawlConfig.getAcceptCodes().contains(page.getStatusCode())) {
				logger.info("页面{} 下载完成", request.getUrl());
				return page;
			} else {
				logger.error("错误的status code:{}", page.getStatusCode());
				return null;
			}
		} catch (IOException e) {
			logger.error("下载页面:{} 时出错! errorMsg: {}", request.getUrl(), e.getMessage());
			return null;
		} finally {
			if (resp != null) {
				try {
					EntityUtils.consume(resp.getEntity());
				} catch (IOException e) {
					logger.warn("关闭http响应时出错: {}", e.getMessage());
				}
			}
		}
	}

	private Page handleResponse(CloseableHttpResponse resp, Request request) throws IOException {
		String content = getContentFromResp(resp);
		Page page = new Page();
		page.setContent(content);
		page.setRequest(request);
		page.setStatusCode(resp.getStatusLine().getStatusCode());
		return page;
	}

	private String getContentFromResp(CloseableHttpResponse resp) throws IOException {
		byte[] contentBytes = IOUtils.toByteArray(resp.getEntity().getContent());
		String htmlCharset = getHtmlCharset(resp, contentBytes);
		if (htmlCharset != null) {
			return new String(contentBytes, htmlCharset);
		} else {
			return new String(contentBytes);
		}
	}

	/**
	 * us.codecraft.webmagic.downloader.HttpClientDownloader 2016年12月10日
	 * 
	 * @param resp
	 * @param contentBytes
	 * @return
	 * @throws IOException
	 */
	private String getHtmlCharset(CloseableHttpResponse resp, byte[] contentBytes) throws IOException {
		String charset;
		String value = resp.getEntity().getContentType().getValue();
		charset = UrlUtil.getCharset(value);
		if (UrlUtil.isNotBlank(charset)) {
			logger.debug("Auto get charset: {}", charset);
			return charset;
		}
		Charset defaultCharset = Charset.defaultCharset();
		String content = new String(contentBytes, defaultCharset.name());
		if (UrlUtil.isNotEmpty(content)) {
			Document document = Jsoup.parse(content);
			Elements links = document.select("meta");
			for (Element link : links) {
				String metaContent = link.attr("content");
				String metaCharset = link.attr("charset");
				if (metaContent.indexOf("charset") != -1) {
					metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
					charset = metaContent.split("=")[1];
					break;
				} else if (UrlUtil.isNotEmpty(metaCharset)) {
					charset = metaCharset;
					break;
				}
			}
		}
		logger.debug("Auto get charset: {}", charset);
		return charset;
	}

	private CloseableHttpClient getHttpClient(Config crawlConfig) {
		SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
		CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : crawlConfig.getAllCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(crawlConfig.getDomain());
            cookieStore.addCookie(cookie);
        }
		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(crawlConfig.getReTryTime(), true))
				.setDefaultSocketConfig(socketConfig)
				.setDefaultCookieStore(cookieStore);
		return httpClientBuilder.build();
	}

	private HttpUriRequest getHttpUriRequest(Request request, Config crawlConfig) {
		RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
		if (crawlConfig.getHeaders() != null) {
			for (Map.Entry<String, String> headerEntry : crawlConfig.getHeaders().entrySet()) {
				requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
			}
		}
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
				.setConnectionRequestTimeout(crawlConfig.getTimeOut()).setSocketTimeout(crawlConfig.getTimeOut())
				.setConnectTimeout(crawlConfig.getTimeOut()).setCookieSpec(CookieSpecs.DEFAULT);
		requestBuilder.setConfig(requestConfigBuilder.build());
		return requestBuilder.build();
	}

	protected RequestBuilder selectRequestMethod(Request request) {
		String method = request.getMethod();
		if (method == null || method.equalsIgnoreCase("GET")) {
			// default get
			return RequestBuilder.get();
		} else if (method.equalsIgnoreCase("GET")) {
			RequestBuilder requestBuilder = RequestBuilder.post();
			NameValuePair[] nameValuePair = request.getPostData();
			if (nameValuePair != null && nameValuePair.length > 0) {
				requestBuilder.addParameters(nameValuePair);
			}
			return requestBuilder;
		} else if (method.equalsIgnoreCase("HEAD")) {
			return RequestBuilder.head();
		} else if (method.equalsIgnoreCase("PUT")) {
			return RequestBuilder.put();
		} else if (method.equalsIgnoreCase("DELETE")) {
			return RequestBuilder.delete();
		} else if (method.equalsIgnoreCase("TRACE")) {
			return RequestBuilder.trace();
		}
		throw new IllegalArgumentException("Illegal HTTP Method " + method);
	}

}

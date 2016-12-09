package space.kyu.crawlaway.downloader;

import space.kyu.crawlaway.entity.Config;
import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.Request;
/**
 * 页面下载执行者
 * @author yukai
 * @date 2016年12月9日
 */
public interface Downloader {
	public Page download(Request request);
	
	public Config getDownloadConfig();
}

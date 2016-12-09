package space.kyu.crawlaway.processor;

import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.ResultItem;

/**
 * 页面处理接口
 * @author yukai
 * @date 2016年12月9日
 */
public interface Processor {
	public ResultItem processPage(Page page);
}

package space.kyu.crawlaway.processor;

import java.util.List;

import space.kyu.crawlaway.entity.Page;
import space.kyu.crawlaway.entity.ResultItems;

/**
 * 页面处理接口
 * @author yukai
 * @date 2016年12月9日
 */
public interface Processor {
	public List<ResultItems> processPage(Page page);
}

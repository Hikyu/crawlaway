package space.kyu.crawlaway.pipeline;

import java.util.List;

import space.kyu.crawlaway.entity.ResultItem;

/**
 * 持久化策略
 * @author yukai
 * @date 2016年12月9日
 */
public interface Pipeline {
	public void pipeline(List<ResultItem> resultItems);
}

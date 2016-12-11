package space.kyu.crawlaway.pipeline;

import java.util.Map;

import space.kyu.crawlaway.entity.ResultItems;

public class ConsolePipeline implements Pipeline {

	public void pipeline(ResultItems resultItems) {
//		System.out.println("get page: " + resultItems.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAllResults().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
	}
}


package space.kyu.crawlaway.scheduler;

import java.util.Set;

import com.google.common.collect.Sets;

import space.kyu.crawlaway.entity.Request;

public abstract class DuplicateRemovedScheduler implements Scheduler {
	private Set<String> urls = Sets.newConcurrentHashSet();
	private boolean open = true;

	public void push(Request request) {
		if (open) {
			if (urls.add(request.getUrl())) {
				pushWhenNoDuplicate(request);
			}
		} else {
			pushWhenNoDuplicate(request);
		}
	}
	
	protected abstract void pushWhenNoDuplicate(Request request);

	public void setOpen(boolean open) {
		this.open = open;
	}

	
}

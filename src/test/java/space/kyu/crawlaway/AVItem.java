package space.kyu.crawlaway;

import java.util.ArrayList;
import java.util.List;

public class AVItem {
	// 标题
	private String title;
	// 演员
	private String actor;
	// 时长
	private String duration;
	// 系列
	private String series;
	// 番号
	private String designation;
	// 图片链接
	private String imgURL;
	// 资源
	private List<String> resources;
	// 发行日期
	private String date;
	// 类别
	private String category;

	{
		resources = new ArrayList<String>();
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append("Title:").append(title).append("|")
		       .append("Actor:").append(actor).append("|")
		       .append("Duration:").append(duration).append("|")
		       .append("Date:").append(date).append("|")
		       .append("Series:").append(series).append("|")
		       .append("Category").append(category).append("|")
		       .append("ImageUrl:").append(imgURL).append("|")
		       .append("Desigination:").append(designation).append("|")
		       .append("Resource:").append(resources.toString());
		return builder.toString();
	}
	
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}

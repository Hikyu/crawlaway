package space.kyu.crawlaway;

import java.util.List;

public class AVItem {
	private String title;
	private String designation;
	private String imgURL;
	private List<String> resources;
	public String getName() {
		return title;
	}
	public void setName(String name) {
		this.title = name;
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
	
	
}

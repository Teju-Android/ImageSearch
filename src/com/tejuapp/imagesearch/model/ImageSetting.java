package com.tejuapp.imagesearch.model;

import java.io.Serializable;

public class ImageSetting implements Serializable{
	
	static final long serialVersionUID = 7099889824922887632L;
	private String size;
	private String color;
	private String type;
	private String site;
	
	public ImageSetting(String size, String color, String type, String site) {
		this.size = size;
		this.color = color;
		this.type = type;
		this.site = site;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
}

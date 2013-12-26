/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.innovate.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class Speaker implements Serializable {
	/**
	 * 
	 * The speaker name.
	 * 
	 */
	private String name;

	/**
	 * 
	 * The speaker title.
	 * 
	 */
	private String title;
	/**
	 * 
	 * The speaker picture.
	 * 
	 */
	private String picture;
	/**
	 * 
	 * The speaker details.
	 * 
	 */
	private String details;
	/**
	 * 
	 * The speaker session identifiers.
	 * 
	 */
	private List<String> sessionIds;

	public String getPictureName() {
		StringBuffer tmp = new StringBuffer(
				picture.substring(("/static/i/speakers/").length()));

		for (int i = 0; i < tmp.length(); i++) {
			if (tmp.charAt(i) == '-') {
				tmp.setCharAt(i, '_');
			}
		}

		if (tmp.charAt(0) >= '0' && tmp.charAt(0) <= '9') {
			tmp.insert(0, 'x');
		}
		tmp.delete(tmp.indexOf(".png"), tmp.length());

		return tmp.toString().toLowerCase(Locale.US);
	}

	/**
	 * 
	 * Simple getter for a namesake field.
	 * 
	 * 
	 * @return value of a namesake field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * Simple setter for a namesake field.
	 * 
	 * 
	 * @param name
	 *            - new value for a namesake field.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * Simple getter for a namesake field.
	 * 
	 * 
	 * @return value of a namesake field.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * Simple setter for a namesake field.
	 * 
	 * 
	 * @param name
	 *            - new value for a namesake field.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * Simple getter for a namesake field.
	 * 
	 * 
	 * @return value of a namesake field.
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * 
	 * Simple setter for a namesake field.
	 * 
	 * 
	 * @param picture
	 *            - new value for a namesake field.
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * 
	 * Simple getter for a namesake field.
	 * 
	 * 
	 * @return value of a namesake field.
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * 
	 * Simple setter for a namesake field.
	 * 
	 * 
	 * @param details
	 *            - new value for a namesake field.
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * Simple getter for a namesake field.
	 * 
	 * @return value of a namesake field.
	 */
	public List<String> getSessionIds() {
		return sessionIds;
	}

	/**
	 * Simple setter for a namesake field.
	 * 
	 * @param sessionIds
	 *            - new value for a namesake field.
	 */
	public void setSessionIds(List<String> sessionIds) {
		this.sessionIds = sessionIds;
	}
}
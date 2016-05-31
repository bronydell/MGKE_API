package com.bronydell.arch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day {
	private String date;
	private ArrayList<Group> groups;

	/**
	 * Getter for date
	 * 
	 * @return Date of recording
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Setter for date
	 * 
	 * @param Date
	 *            of recording
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Getter for groups
	 * 
	 * @return Group array
	 */
	public ArrayList<Group> getGroups() {
		return groups;
	}

	/**
	 * Setter for groups
	 * 
	 * @param Group
	 *            array
	 */
	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	/**
	 * Set Day date
	 * 
	 * @param date
	 */
	public void pickADate(String date) {
		if (date == null || date == "") {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date dat = new Date();
			date = dateFormat.format(dat);
		} else {
			Pattern pattern = Pattern.compile("(0[1-9]|1[0-9]|2[0-9]|3[01]).(0[1-9]|1[012]).[0-9]{4}");
			Matcher matcher = pattern.matcher(date);
			if (matcher.find())
				date = LocalDateTime.parse(matcher.group(0), DateTimeFormatter.ofPattern("dd.MM.yyyy"))
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		this.date = date;
	}
}

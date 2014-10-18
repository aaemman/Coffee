package com.percolate.coffee.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.percolate.coffee.util.date.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AlexanderEmmanuel on 2014-10-17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoffeeTypeDetailed extends CoffeeType {

	private String mUpdatedAt;

	/**
	 * Default constructor for CoffeeType.class.
	 * - This constructor is necessary (even if blank) in order for Jackson JSON Parsing to work properly.
	 */
	@JsonCreator
	public CoffeeTypeDetailed() {	}

	@JsonProperty("last_updated_at")
	public String getUpdatedAt() {
		return mUpdatedAt;
	}

	@JsonProperty("last_updated_at")
	public void setUpdatedAt(String updatedAt) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		try {
			date = format.parse(updatedAt.substring(0,19));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mUpdatedAt = "Updated " + DateUtils.getTimeAgo(date.getTime());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CoffeeTypeDetailed)) return false;
		if (!super.equals(o)) return false;

		CoffeeTypeDetailed that = (CoffeeTypeDetailed) o;

		if (!mUpdatedAt.equals(that.mUpdatedAt)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + mUpdatedAt.hashCode();
		return result;
	}
}

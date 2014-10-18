package com.percolate.coffee.util.date;

import java.util.Date;

/**
 * A generic utility class for all utilities relating to dates
 */
public class DateUtils {

	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS   = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS    = 24 * HOUR_MILLIS;


	/**
	 * A method which returns a String stating how long ago a given time was
	 * @param time the time in milliseconds
	 * @return a string representing how long ago the given time was
	 */
	public static String getTimeAgo(long time) {
		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = new Date().getTime();

		if (time > now || time <= 0) {
			return "This Is From The Future!";
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return "just now";
		} else if (diff < 2 * MINUTE_MILLIS) {
			return "a minute ago";
		} else if (diff < 50 * MINUTE_MILLIS) {
			return diff / MINUTE_MILLIS + " minutes ago";
		} else if (diff < 90 * MINUTE_MILLIS) {
			return "an hour ago";
		} else if (diff < 24 * HOUR_MILLIS) {
			return diff / HOUR_MILLIS + " hours ago";
		} else if (diff < 48 * HOUR_MILLIS) {
			return "yesterday";
		} else {
			return diff / DAY_MILLIS + " days ago";
		}
	}
}

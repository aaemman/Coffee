package com.percolate.coffee.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoffeeType extends SugarRecord<CoffeeType> implements Serializable {
	private String mDescription;
	private String mImageUrl;
	private String mCoffeeTypeId;
	private String mName;

	/**
	 * Default constructor for CoffeeType.class.
	 * - This constructor is necessary (even if blank) in order for Jackson JSON Parsing and SQL queries to work properly.
	 */
	@JsonCreator
	public CoffeeType() { }

	@JsonProperty("desc")
	public String getDescription() {
		return mDescription;
	}

	@JsonProperty("desc")
	public void setDescription(String description) {
		mDescription = description;
	}

	@JsonProperty("image_url")
	public String getImageUrl() {
		return mImageUrl;
	}

	@JsonProperty("image_url")
	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	@JsonProperty("id")
	public String getCoffeeTypeId() {
		return mCoffeeTypeId;
	}

	@JsonProperty("id")
	public void setmCoffeeTypeIdId(String id) {
		mCoffeeTypeId = id;
	}

	@JsonProperty("name")
	public String getName() {
		return mName;
	}

	@JsonProperty("name")
	public void setName(String name) {
		mName = name;
	}

	@Override
	public String toString() {
		return "CoffeeType{" +
				"mDescription='" + mDescription + '\'' +
				", mImageUrl='" + mImageUrl + '\'' +
				", mId='" + mCoffeeTypeId + '\'' +
				", mName='" + mName + '\'' +
				'}';
	}
}

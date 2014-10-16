package com.percolate.coffee.util.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoffeeType {
	private String mDescription;
	private String mImageUrl;
	private String mId;
	private String mName;

	@JsonCreator
	public CoffeeType() {
	}

	@JsonProperty("desc")
	public String getDescription() {
		return mDescription;
	}

	@JsonProperty("desc")
	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	@JsonProperty("image_url")
	public String getImageUrl() {
		return mImageUrl;
	}

	@JsonProperty("image_url")
	public void setImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}


	@JsonProperty("id")
	public String getId() {
		return mId;
	}

	@JsonProperty("id")
	public void setId(String mId) {
		this.mId = mId;
	}

	@JsonProperty("name")
	public String getName() {
		return mName;
	}

	@JsonProperty("name")
	public void setName(String mName) {
		this.mName = mName;
	}

	@Override
	public String toString() {
		return "CoffeeType{" +
				"mDescription='" + mDescription + '\'' +
				", mImageUrl='" + mImageUrl + '\'' +
				", mId='" + mId + '\'' +
				", mName='" + mName + '\'' +
				'}';
	}
}

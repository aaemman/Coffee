package com.percolate.coffee.util.api.request;

import android.content.res.Resources;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.percolate.coffee.R;
import com.percolate.coffee.util.api.pojo.CoffeeTypeDetailed;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * Created by AlexanderEmmanuel on 2014-10-17.
 */
public class CoffeeTypeShowRequest extends SpringAndroidSpiceRequest<CoffeeTypeDetailed> {

	Resources mResources;
	String    mId;

	public CoffeeTypeShowRequest(String id, Resources resources) {
		super(CoffeeTypeDetailed.class);
		mResources = resources;
		mId = id;

		if (id == null) {
			throw new NullPointerException("THE ID CANNOT BE NULL");
		}
	}

	@Override
	public CoffeeTypeDetailed loadDataFromNetwork() throws Exception {
		String url = mResources.getString(R.string.percolate_coffee_base_url) + mId + "/";

		HttpHeaders headers = new HttpHeaders() {
			{
				set("Authorization", mResources.getString(R.string.percolate_coffee_api_key));
			}
		};
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");

		CoffeeTypeDetailed coffeeType = getRestTemplate().exchange(url.toLowerCase(), HttpMethod.GET, new HttpEntity<Object>(headers), CoffeeTypeDetailed.class).getBody();

		return coffeeType;

	}

	public String getCacheKey(Resources resources) {
		return resources.getString(R.string.percolate_coffee_coffee_type_show_cache_key);
	}
}

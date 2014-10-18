package com.percolate.coffee.util.api.request;

import android.content.res.Resources;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.percolate.coffee.R;
import com.percolate.coffee.model.CoffeeTypeList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class CoffeeTypeIndexRequest extends SpringAndroidSpiceRequest<CoffeeTypeList> {

	Resources mResources;

	public CoffeeTypeIndexRequest(Resources resources) {
		super(CoffeeTypeList.class);
		mResources = resources;
	}

	@Override
	public CoffeeTypeList loadDataFromNetwork() throws Exception {
		String url = mResources.getString(R.string.percolate_coffee_base_url);
		HttpHeaders headers = new HttpHeaders() {
			{
				set("Authorization", mResources.getString(R.string.percolate_coffee_api_key));
			}
		};
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");


		CoffeeTypeList coffeeTypes = getRestTemplate().exchange(url.toLowerCase(), HttpMethod.GET, new HttpEntity<Object>(headers), CoffeeTypeList.class).getBody();
		return coffeeTypes;

	}

	public String getCacheKey(Resources resources) {
		return resources.getString(R.string.percolate_coffee_coffee_type_index_cache_key);
	}
}

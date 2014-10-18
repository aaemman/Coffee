package com.percolate.coffee.util.api.request;

import android.content.res.Resources;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.percolate.coffee.R;
import com.percolate.coffee.model.CoffeeTypeDetailed;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * Show Request; gets a single {@link com.percolate.coffee.model.CoffeeType} given a {@link com.percolate.coffee.model.CoffeeType} CoffeeTypeId
 */
public class CoffeeTypeShowRequest extends SpringAndroidSpiceRequest<CoffeeTypeDetailed> {

	Resources mResources;
	String    mId;

	/**
	 *Constructor
	 * @param coffeeTypeId {@link com.percolate.coffee.model.CoffeeType} mCoffeeTypeId
	 * @param resources applications resources
	 */
	public CoffeeTypeShowRequest(String coffeeTypeId, Resources resources) {
		super(CoffeeTypeDetailed.class);
		mResources = resources;
		mId = coffeeTypeId;

		if (coffeeTypeId == null) {
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

	/**
	 * @param resources
	 * @return a String which can be used as a standard key for caching the results of this request
	 */
	public String getCacheKey(Resources resources) {
		return resources.getString(R.string.percolate_coffee_coffee_type_show_cache_key);
	}
}

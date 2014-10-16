package com.percolate.coffee.util.api.requestlistener;

import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.percolate.coffee.util.api.pojo.CoffeeTypeList;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class CoffeeTypeIndexRequestListener implements RequestListener<CoffeeTypeList> {
	@Override
	public void onRequestFailure(SpiceException spiceException) {
		//update UI
		Log.i("CoffeeTypeIndexRequestListener", "REQUEST FAILED");
		Log.i("CoffeeTypeIndexRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());

	}

	@Override
	public void onRequestSuccess(CoffeeTypeList coffeeTypes) {
		//update UI
		Log.i("CoffeeTypeIndexRequestListener", "REQUEST SUCCESS!");

	}
}

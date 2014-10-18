package com.percolate.coffee.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.octo.android.robospice.request.okhttp.simple.OkHttpBitmapRequest;
import com.octo.android.robospice.request.simple.IBitmapRequest;
import com.octo.android.robospice.spicelist.SpiceListItemView;
import com.octo.android.robospice.spicelist.okhttp.OkHttpBitmapSpiceManager;
import com.octo.android.robospice.spicelist.okhttp.OkHttpSpiceArrayAdapter;
import com.percolate.coffee.model.CoffeeType;
import com.percolate.coffee.view.ui.CoffeeTypeSpiceListItem;

import java.io.File;
import java.util.List;

/**
 * SpiceArrayAdapter which manages the list specifically for the {@link com.percolate.coffee.activity.CoffeeTypeListViewActivity}
 *
 */
public class CoffeeTypeListSpiceAdapter extends OkHttpSpiceArrayAdapter<CoffeeType> {

	/**
	 * Used for testing only.
	 *
	 * @param context
	 * @param spiceManagerBinary
	 * @param objects
	 */
	public CoffeeTypeListSpiceAdapter(Context context, OkHttpBitmapSpiceManager spiceManagerBinary, List<CoffeeType> objects) {
		super(context, spiceManagerBinary, objects);
	}

	@Override
	public SpiceListItemView<CoffeeType> createView(Context context, ViewGroup parent) {
		return new CoffeeTypeSpiceListItem(getContext());
	}

	@Override
	public IBitmapRequest createRequest(CoffeeType coffeeType, int imageIndex, int requestImageWidth, int requestImageHeight) {
		File tempFile = new File(getContext().getCacheDir(), "COFFEE_TYPE_IMAGE_CACHE_KEY" + coffeeType.getName());

		if (coffeeType.getImageUrl() != null && !coffeeType.getImageUrl().trim().equals("")) {
			return new OkHttpBitmapRequest(coffeeType.getImageUrl(), requestImageWidth,
			                               requestImageHeight, tempFile);
		}else{
			return null;
		}
	}
}

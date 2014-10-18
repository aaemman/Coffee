package com.percolate.coffee.view.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.spicelist.SpiceListItemView;
import com.percolate.coffee.R;
import com.percolate.coffee.model.CoffeeType;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class CoffeeTypeSpiceListItem extends RelativeLayout implements SpiceListItemView<CoffeeType> {
	private CoffeeType mCoffeeType;
	private TextView   mName;
	private TextView   mDescription;
	private ImageView  mPicture;

	/**
	 * Default Constructor
	 */
	public CoffeeTypeSpiceListItem(Context context) {
		super(context);
		inflateView(context);
	}

	private void inflateView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.coffee_list_item, this);
		mName = (TextView) findViewById(R.id.coffee_list_item_name_text_view);
		mDescription = (TextView) findViewById(R.id.coffee_list_item_description_text_view);
		mPicture = (ImageView) findViewById(R.id.coffee_list_item_picture_image_view);
	}

	/**
	 * @return the object that is displayed by this list item.
	 */
	@Override
	public CoffeeType getData() {
		return mCoffeeType;
	}

	/**
	 * @param imageIndex the index of the image in case there are multiple bitmap used to represent every
	 *                   piece of data.
	 * @return the imageView that is displaying the drawable associated to this view's data. This
	 * ImageView will be update by the SpiceManager.
	 */
	@Override
	public ImageView getImageView(int imageIndex) {
		return  mPicture;
	}

	/**
	 * @return the number of image views used to represent each piece of data. Usually 1.
	 */
	@Override
	public int getImageViewCount() {
		return 1;
	}

	/**
	 * Updates the view with given data. Overrides of this method should not deal with images. Only
	 * update other fields here.
	 *
	 * @param coffeeType
	 */
	@Override
	public void update(CoffeeType coffeeType) {
		mCoffeeType = coffeeType;
		mName.setText(mCoffeeType.getName());
		mDescription.setText(mCoffeeType.getDescription());
	}
}

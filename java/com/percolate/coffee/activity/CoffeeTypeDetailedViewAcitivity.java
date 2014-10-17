package com.percolate.coffee.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.BitmapRequest;
import com.percolate.coffee.R;
import com.percolate.coffee.util.animation.FadeInAnimationFactory;
import com.percolate.coffee.util.api.pojo.CoffeeTypeDetailed;
import com.percolate.coffee.util.api.request.CoffeeTypeShowRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import roboguice.util.temp.Ln;

public class CoffeeTypeDetailedViewAcitivity extends JacksonSpringAndroidSpicedActivity {


	private LinearLayout   mDetailedViewLayout;
	private TextView       mNameTextView;
	private TextView       mDescriptionTextView;
	private ImageView      mPictureImageView;
	private TextView       mUpdatedAtTextView;
	private ActionBar      mActionBar;
	private LayoutInflater mInflater;
	private View           mCustomActionBarView;

	private CoffeeTypeDetailed mCoffeeTypeDetailed;
	private String             mId;
	private String             mImageUrl;
	private String             mLastRequestCacheKey;

	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_type_detailed_view_acitivity);
		initActionBar();


		Intent intent = getIntent();

		mId = intent.getStringExtra("id");
		if (mId == null) {
			throw new NullPointerException(
					"COFFEETYPE NAME WAS NULL; MAKE SURE THAT YOU ARE ADDING THE " +
							"COFFEETYPES NAME AS AN INTENT EXTRA UNDER NAME \'id\'");
		}

		mImageUrl = intent.getStringExtra("image_url");

		mDetailedViewLayout = (LinearLayout) findViewById(R.id.coffee_type_detailed_view_layout);
		mNameTextView = (TextView) findViewById(R.id.coffee_detailed_item_name_text_view);
		mDescriptionTextView = (TextView) findViewById(R.id.coffee_detailed_item_description_text_view);
		mPictureImageView = (ImageView) findViewById(R.id.coffee_detailed_item_picture_image_view);
		mUpdatedAtTextView = (TextView) findViewById(R.id.coffee_detailed_item_updated_at_text_view);
	}

	@Override
	protected void onStart() {
		super.onStart();
		performShowRequest();
	}


	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mInflater = LayoutInflater.from(this);
		mCustomActionBarView = mInflater.inflate(R.layout.percolate_action_bar, null);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		mActionBar.setCustomView(mCustomActionBarView, lp);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	private void updateTextViewContent(CoffeeTypeDetailed coffeeTypeDetailed) {
		mNameTextView.setText(coffeeTypeDetailed.getName());
		mDescriptionTextView.setText(coffeeTypeDetailed.getDescription());
		mUpdatedAtTextView.setText(coffeeTypeDetailed.getUpdatedAt());

		new FadeInAnimationFactory(mDetailedViewLayout)
				.duration(6000)
				.animate();
	}

	private void updateImageViewContents(Bitmap bitmap) {
		mPictureImageView.setImageBitmap(bitmap);
		new FadeInAnimationFactory(mPictureImageView)
				.duration(6000)
				.animate();
	}


	private void performShowRequest() {
		CoffeeTypeShowRequest request = new CoffeeTypeShowRequest(mId, getResources());
		mLastRequestCacheKey = request.getCacheKey(getResources());
		spiceManager.execute(request, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new CoffeeTypeShowRequestListener());

		if (mImageUrl != null) {

			File cacheFile = null;
			String filename;
			try {
				filename = URLEncoder.encode(mImageUrl, "UTF-8");
				cacheFile = new File(getCacheDir(), filename);
			} catch (UnsupportedEncodingException e) {
				Ln.e(e);
			}

			BitmapRequest bitmapRequest = new BitmapRequest(mImageUrl, 800, 800, cacheFile);
			//the standard Bitmap spiceRequest does not have a cache key so I am using its class name as a replacement.
			mLastRequestCacheKey = request.getClass().getSimpleName();
			spiceManager.execute(bitmapRequest, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new GetImageFromUrlRequestListener());
		}
	}


	// ------ INNER CLASSES ------

	private class CoffeeTypeShowRequestListener implements RequestListener<CoffeeTypeDetailed> {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Log.i("CoffeeTypeShowRequestListener", "COFFEE SHOW REQUEST FAILED");
			Log.i("CoffeeTypeShowRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());
		}

		@Override
		public void onRequestSuccess(CoffeeTypeDetailed coffeeTypeDetailed) {
			Log.i("CoffeeTypeShowRequestListener", "COFFEE SHOW REQUEST SUCCESS!");
			mCoffeeTypeDetailed = coffeeTypeDetailed;
			updateTextViewContent(coffeeTypeDetailed);


		}
	}

	private class GetImageFromUrlRequestListener implements RequestListener<Bitmap> {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Log.i("GetImageFromUrlRequestListener", "GET IMAGE FROM URL REQUEST FAILED");
			Log.i("GetImageFromUrlRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());
		}

		@Override
		public void onRequestSuccess(Bitmap bitmap) {
			Log.i("GetImageFromUrlRequestListener", "GET IMAGE FROM URL REQUEST SUCCESS!");
			updateImageViewContents(bitmap);

		}
	}

}

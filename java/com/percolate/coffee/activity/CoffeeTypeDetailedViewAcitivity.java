package com.percolate.coffee.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.BitmapRequest;
import com.orm.SugarRecord;
import com.percolate.coffee.R;
import com.percolate.coffee.model.CoffeeType;
import com.percolate.coffee.model.CoffeeTypeDetailed;
import com.percolate.coffee.util.animation.FadeInAnimationFactory;
import com.percolate.coffee.util.api.request.CoffeeTypeShowRequest;
import com.percolate.coffee.util.view.ImageUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import roboguice.util.temp.Ln;

/**
 * Activity class for the details page of a {@link com.percolate.coffee.model.CoffeeTypeDetailed }
 */
public class CoffeeTypeDetailedViewAcitivity extends JacksonSpringAndroidSpicedActivity {


	private LinearLayout       mDetailedViewLayout;
	private LinearLayout       mImageLoadLayout;
	private TextView           mNameTextView;
	private TextView           mDescriptionTextView;
	private ImageView          mPictureImageView;
	private TextView           mUpdatedAtTextView;
	private ProgressBar        mImageLoadingProgressBar;
	private ActionBar          mActionBar;
	private LayoutInflater     mInflater;
	private View               mCustomActionBarView;
	private CoffeeType         mCoffeeType;
	private CoffeeTypeDetailed mCoffeeTypeDetailed;
	private String             mId;
	private String             mImageUrl;
	private String             mLastRequestCacheKey;
	private MenuItem           mShareActionItem;
	private View               mShareActionView;
	private View               mErrorPromptView;

	private Bitmap mPictureBitmap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_type_detailed_view_acitivity);
		initActionBar();

		Intent intent = getIntent();

		mCoffeeType = (CoffeeType) intent.getSerializableExtra("coffeeType");
		mId = mCoffeeType.getCoffeeTypeId();
		if (mId == null) {
			throw new NullPointerException(
					"COFFEETYPE ID WAS NULL; MAKE SURE THAT YOU ARE ADDING A " +
							"COFFEETYPE AS AN INTENT EXTRA UNDER NAME \'coffeeType\'");
		}

		mImageUrl = mCoffeeType.getImageUrl();

		mImageLoadingProgressBar = (ProgressBar) findViewById(R.id.coffee_detailed_item_image_loading_progress_bar);
		mImageLoadLayout = (LinearLayout) findViewById(R.id.coffee_detailed_item_image_load_layout);
		mDetailedViewLayout = (LinearLayout) findViewById(R.id.coffee_type_detailed_view_layout);
		mNameTextView = (TextView) findViewById(R.id.coffee_detailed_item_name_text_view);
		mDescriptionTextView = (TextView) findViewById(R.id.coffee_detailed_item_description_text_view);
		mPictureImageView = (ImageView) findViewById(R.id.coffee_detailed_item_picture_image_view);
		mUpdatedAtTextView = (TextView) findViewById(R.id.coffee_detailed_item_updated_at_text_view);
		mErrorPromptView = mInflater.inflate(R.layout.an_error_has_occured, null);

		if (mImageUrl == null || mImageUrl.isEmpty()) {
			mImageLoadingProgressBar.setVisibility(View.GONE);
		}

		List<CoffeeTypeDetailed> coffeeTypeDetailedList = SugarRecord.find(CoffeeTypeDetailed.class, "m_coffee_type_id = ?", mId);

		if (coffeeTypeDetailedList != null && coffeeTypeDetailedList.size() > 0) {
			if (coffeeTypeDetailedList.get(0) != null) {
				updateTextViewContent(coffeeTypeDetailedList.get(0));
			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		performShowRequest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.coffee_type_detailed_view_acitivity, menu);
		View actionView = getLayoutInflater().inflate(R.layout.share_action_item, null);
		mShareActionItem = menu.findItem(R.id.action_share);
		mShareActionItem.setActionView(actionView);

		mShareActionView = mShareActionItem.getActionView().findViewById(R.id.share_action_view);
		mShareActionView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(Intent.createChooser(
						getDefaultShareIntent(),
						"Select an application to share with..."));

			}
		});

		mShareActionView.setEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;

			default:
				break;
		}
		return true;
	}

	private Intent getDefaultShareIntent() {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, mCoffeeTypeDetailed.getName());
		intent.putExtra(Intent.EXTRA_TEXT, mCoffeeTypeDetailed.getDescription());

		if (!(mImageUrl == null || mImageUrl.isEmpty())) {
			Uri uri = ImageUtils.getImageUri(getBaseContext(), mPictureBitmap, "coffeeLatestSharedImage.jpg");
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		}

		return intent;
	}

	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mInflater = LayoutInflater.from(this);
		mCustomActionBarView = mInflater.inflate(R.layout.percolate_action_bar, null);

		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);

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

		if (!(mImageUrl == null || mImageUrl.isEmpty())) {

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

			mImageLoadingProgressBar.setVisibility(View.VISIBLE);
		}
	}


	// ------ INNER CLASSES ------

	private class CoffeeTypeShowRequestListener implements RequestListener<CoffeeTypeDetailed> {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Log.i("CoffeeTypeShowRequestListener", "COFFEE SHOW REQUEST FAILED");
			Log.i("CoffeeTypeShowRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());

			mImageLoadingProgressBar = (ProgressBar) findViewById(R.id.coffee_detailed_item_image_loading_progress_bar);
			mImageLoadingProgressBar.setVisibility(View.GONE);

			if (mErrorPromptView != null && mErrorPromptView.getParent() == null) {
				((TextView) mErrorPromptView.findViewById(R.id.error_occured_prompt_text)).setText(spiceException.getLocalizedMessage());
				mImageLoadLayout.addView(mErrorPromptView, 0);
			}
		}

		@Override
		public void onRequestSuccess(CoffeeTypeDetailed coffeeTypeDetailed) {
			Log.i("CoffeeTypeShowRequestListener", "COFFEE SHOW REQUEST SUCCESS!");
			mCoffeeTypeDetailed = coffeeTypeDetailed;

			List<CoffeeTypeDetailed> coffeeTypeDetailedList = SugarRecord.find(CoffeeTypeDetailed.class, "m_coffee_type_id = ?", mCoffeeTypeDetailed.getCoffeeTypeId());
			if (coffeeTypeDetailedList.size() != 0) {
				if (!(coffeeTypeDetailedList.get(0).equals(mCoffeeTypeDetailed))) {
					mCoffeeTypeDetailed.save();
				}
			} else {
				mCoffeeTypeDetailed.save();
			}

			mImageLoadingProgressBar.setVisibility(View.GONE);

			updateTextViewContent(coffeeTypeDetailed);

			if (mErrorPromptView != null && mErrorPromptView.getParent() == mImageLoadLayout) {
				mImageLoadLayout.removeView(mErrorPromptView);
			}

			if (mImageUrl == null || mImageUrl.isEmpty()) {
				mShareActionView.setEnabled(true);
			}
		}
	}

	private class GetImageFromUrlRequestListener implements RequestListener<Bitmap> {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Log.i("GetImageFromUrlRequestListener", "GET IMAGE FROM URL REQUEST FAILED");
			Log.i("GetImageFromUrlRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());

			mImageLoadingProgressBar = (ProgressBar) findViewById(R.id.coffee_detailed_item_image_loading_progress_bar);
			mImageLoadingProgressBar.setVisibility(View.GONE);

			if (mErrorPromptView != null && mErrorPromptView.getParent() == null) {
				((TextView) mErrorPromptView.findViewById(R.id.error_occured_prompt_text)).setText(spiceException.getLocalizedMessage());
				mImageLoadLayout.addView(mErrorPromptView, 0);
			}
		}

		@Override
		public void onRequestSuccess(Bitmap bitmap) {
			Log.i("GetImageFromUrlRequestListener", "GET IMAGE FROM URL REQUEST SUCCESS!");
			mPictureBitmap = bitmap;
			mShareActionView.setEnabled(true);
			updateImageViewContents(bitmap);

			mImageLoadingProgressBar.setVisibility(View.GONE);

			if (mErrorPromptView != null && mErrorPromptView.getParent() == mImageLoadLayout) {
				mImageLoadLayout.removeView(mErrorPromptView);
			}

		}
	}

}

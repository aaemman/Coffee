package com.percolate.coffee.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.spicelist.okhttp.OkHttpBitmapSpiceManager;
import com.percolate.coffee.R;
import com.percolate.coffee.adapter.CoffeeTypeListSpiceAdapter;
import com.percolate.coffee.util.animation.ListProgressBarAnimationBuilder;
import com.percolate.coffee.util.api.pojo.CoffeeTypeList;
import com.percolate.coffee.util.api.request.CoffeeTypeIndexRequest;


public class CoffeeListActivity extends JacksonSpringAndroidSpicedActivity {
	private String mLastRequestCacheKey;
	private OkHttpBitmapSpiceManager spiceManagerBinary = new OkHttpBitmapSpiceManager();

	private RelativeLayout coffeeTypesListLayout;
	private ProgressBar    coffeeTypesProgressBar;
	private ListView       coffeeTypesListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_list);
		coffeeTypesListLayout = (RelativeLayout) findViewById(R.id.coffee_types_list_linear_layout);
		coffeeTypesProgressBar = (ProgressBar) findViewById(R.id.coffee_types_list_progress_bar);
		coffeeTypesListView = (ListView) findViewById(R.id.coffee_types_list_list_view);
	}

	@Override
	protected void onStart() {
		super.onStart();
		spiceManagerBinary.start(this);
		performIndexRequest();
	}

	@Override
	protected void onStop() {
		super.onStop();
		spiceManagerBinary.shouldStop();
	}

	private void updateListViewContent(CoffeeTypeList coffeeTypes) {
		CoffeeTypeListSpiceAdapter coffeeTypeListSpiceAdapter = new CoffeeTypeListSpiceAdapter(this, spiceManagerBinary, coffeeTypes);
		coffeeTypesListView.setAdapter(coffeeTypeListSpiceAdapter);
	}

	private void performIndexRequest() {
		CoffeeListActivity.this.setProgressBarIndeterminateVisibility(true);

		CoffeeTypeIndexRequest request = new CoffeeTypeIndexRequest(getResources());
		mLastRequestCacheKey = request.getCacheKey(getResources());

		spiceManager.execute(request, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new CoffeeTypeIndexRequestListener());
		animateProgressBar(ProgressBarAnimation.ENTER);
	}

	private void animateProgressBar(final ProgressBarAnimation progressBarAnimation) {

		new ListProgressBarAnimationBuilder(coffeeTypesListLayout)
				.shown(progressBarAnimation.mShown)
				.onStartAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coffeeTypesListLayout.getLayoutParams();
						lp.bottomMargin = ListProgressBarAnimationBuilder.PROGRESS_BAR_END_POSITION;
						coffeeTypesListLayout.setLayoutParams(lp);
					}
				})
				.onEndAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						if (progressBarAnimation.mShown) {
							coffeeTypesProgressBar.setVisibility(View.GONE);
							RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coffeeTypesListLayout.getLayoutParams();
							lp.topMargin = -ListProgressBarAnimationBuilder.PROGRESS_BAR_END_POSITION;
							coffeeTypesListLayout.setLayoutParams(lp);
						}
					}
				})
				.animate();
	}

	// ------ HELPERS ------

	private enum ProgressBarAnimation {
		ENTER(false),
		EXIT(true);

		private boolean mShown;

		ProgressBarAnimation(boolean shown) {
			mShown = shown;
		}
	}

	// ------ INNER CLASSES ------

	private class CoffeeTypeIndexRequestListener implements RequestListener<CoffeeTypeList> {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Log.i("CoffeeTypeIndexRequestListener", "COFFEE INDEX REQUEST FAILED");
			Log.i("CoffeeTypeIndexRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());
			animateProgressBar(ProgressBarAnimation.EXIT);
		}

		@Override
		public void onRequestSuccess(CoffeeTypeList coffeeTypes) {
			Log.i("CoffeeTypeIndexRequestListener", "COFFEE INDEX REQUEST SUCCESS!");
			animateProgressBar(ProgressBarAnimation.EXIT);
			updateListViewContent(coffeeTypes);

		}
	}

}

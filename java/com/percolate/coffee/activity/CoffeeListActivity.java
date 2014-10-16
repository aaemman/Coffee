package com.percolate.coffee.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.percolate.coffee.R;
import com.percolate.coffee.arrayadapter.CoffeeTypeListArrayAdapter;
import com.percolate.coffee.util.animation.ListProgressBarAnimationBuilder;
import com.percolate.coffee.util.api.pojo.CoffeeTypeList;
import com.percolate.coffee.util.api.request.CoffeeTypeIndexRequest;


public class CoffeeListActivity extends JacksonSpringAndroidSpicedActivity {
	private String mLastRequestCacheKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_list);
	}

	@Override
	protected void onStart() {
		super.onStart();
		performIndexRequest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coffee_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void performIndexRequest() {
		CoffeeListActivity.this.setProgressBarIndeterminateVisibility(true);

		CoffeeTypeIndexRequest request = new CoffeeTypeIndexRequest(getResources());
		mLastRequestCacheKey = request.getCacheKey(getResources());

		spiceManager.execute(request, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new CoffeeTypeIndexRequestListener());
		animateProgressBar(ProgressBarAnimation.ENTER);
	}

	private void animateProgressBar(final ProgressBarAnimation progressBarAnimation) {
		final RelativeLayout coffeeTypesList = (RelativeLayout) findViewById(R.id.coffee_types_list_linear_layout);
		final ProgressBar coffeeTypesProgressBar = (ProgressBar) findViewById(R.id.coffee_types_list_progress_bar);

		new ListProgressBarAnimationBuilder(coffeeTypesList)
				.shown(progressBarAnimation.mShown)
				.onStartAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coffeeTypesList.getLayoutParams();
						lp.bottomMargin = ListProgressBarAnimationBuilder.PROGRESS_BAR_END_POSITION;
						coffeeTypesList.setLayoutParams(lp);
					}
				})
				.onEndAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						if (progressBarAnimation.mShown) {
							coffeeTypesProgressBar.setVisibility(View.GONE);
							RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coffeeTypesList.getLayoutParams();
							lp.topMargin = -ListProgressBarAnimationBuilder.PROGRESS_BAR_END_POSITION;
							coffeeTypesList.setLayoutParams(lp);
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

		}

		@Override
		public void onRequestSuccess(CoffeeTypeList coffeeTypes) {
			Log.i("CoffeeTypeIndexRequestListener", "COFFEE INDEX REQUEST SUCCESS!");

			animateProgressBar(ProgressBarAnimation.EXIT);

			ListView coffeeTypesListView = (ListView) findViewById(R.id.coffee_types_list_list_view);
			coffeeTypesListView.setAdapter(new CoffeeTypeListArrayAdapter(getBaseContext(), coffeeTypes));

		}
	}

}

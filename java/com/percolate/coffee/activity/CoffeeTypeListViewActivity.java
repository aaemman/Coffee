package com.percolate.coffee.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.spicelist.okhttp.OkHttpBitmapSpiceManager;
import com.percolate.coffee.R;
import com.percolate.coffee.adapter.CoffeeTypeListSpiceAdapter;
import com.percolate.coffee.util.animation.ListProgressBarAnimationFactory;
import com.percolate.coffee.util.api.pojo.CoffeeType;
import com.percolate.coffee.util.api.pojo.CoffeeTypeList;
import com.percolate.coffee.util.api.request.CoffeeTypeIndexRequest;


public class CoffeeTypeListViewActivity extends JacksonSpringAndroidSpicedActivity {
	private String mLastRequestCacheKey;
	private OkHttpBitmapSpiceManager spiceManagerBinary = new OkHttpBitmapSpiceManager();

	private RelativeLayout coffeeTypesListLayout;
	private ProgressBar    coffeeTypesProgressBar;
	private ListView       coffeeTypesListView;

	private ActionBar      mActionBar;
	private LayoutInflater mInflater;
	private View           mCustomActionBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_list);

		initActionBar();

		coffeeTypesListLayout = (RelativeLayout) findViewById(R.id.coffee_types_list_linear_layout);
		coffeeTypesProgressBar = (ProgressBar) findViewById(R.id.coffee_types_list_progress_bar);
		coffeeTypesListView = (ListView) findViewById(R.id.coffee_types_list_list_view);

		coffeeTypesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				CoffeeType clickedCoffeeType = (CoffeeType) adapterView.getItemAtPosition(i);
				Intent showCoffeeTypeDetailedViewIntent = new Intent(CoffeeTypeListViewActivity.this, CoffeeTypeDetailedViewAcitivity.class);
				showCoffeeTypeDetailedViewIntent.putExtra("coffeeType", clickedCoffeeType);

				startActivity(showCoffeeTypeDetailedViewIntent);


			}
		});
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
		if (coffeeTypesListView.getAdapter() != null) {
			coffeeTypesListView.deferNotifyDataSetChanged();
		} else {
			CoffeeTypeListSpiceAdapter coffeeTypeListSpiceAdapter = new CoffeeTypeListSpiceAdapter(this, spiceManagerBinary, coffeeTypes);
			coffeeTypesListView.setAdapter(coffeeTypeListSpiceAdapter);
		}
	}

	private void performIndexRequest() {
		CoffeeTypeListViewActivity.this.setProgressBarIndeterminateVisibility(true);

		CoffeeTypeIndexRequest request = new CoffeeTypeIndexRequest(getResources());
		mLastRequestCacheKey = request.getCacheKey(getResources());

		spiceManager.execute(request, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new CoffeeTypeIndexRequestListener());
		animateProgressBar(ProgressBarAnimation.ENTER);
	}

	private void animateProgressBar(final ProgressBarAnimation progressBarAnimation) {

		new ListProgressBarAnimationFactory(coffeeTypesListLayout)
				.shown(progressBarAnimation.mShown)
				.onStartAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coffeeTypesListLayout.getLayoutParams();
						lp.bottomMargin = ListProgressBarAnimationFactory.PROGRESS_BAR_END_POSITION;
						coffeeTypesListLayout.setLayoutParams(lp);
					}
				})
				.onEndAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						if (progressBarAnimation.mShown) {
							coffeeTypesProgressBar.setVisibility(View.GONE);
							RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coffeeTypesListLayout.getLayoutParams();
							lp.topMargin = -ListProgressBarAnimationFactory.PROGRESS_BAR_END_POSITION;
							coffeeTypesListLayout.setLayoutParams(lp);
						}
					}
				})
				.animate();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
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

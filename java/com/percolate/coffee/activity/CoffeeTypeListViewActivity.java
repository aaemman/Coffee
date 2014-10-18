package com.percolate.coffee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.spicelist.okhttp.OkHttpBitmapSpiceManager;
import com.orm.SugarRecord;
import com.percolate.coffee.R;
import com.percolate.coffee.adapter.CoffeeTypeListSpiceAdapter;
import com.percolate.coffee.model.CoffeeType;
import com.percolate.coffee.model.CoffeeTypeList;
import com.percolate.coffee.util.animation.ListProgressBarAnimationFactory;
import com.percolate.coffee.util.api.request.CoffeeTypeIndexRequest;

import java.util.List;

/**
 * Activity class for the details page of a {@link com.percolate.coffee.model.CoffeeType }
 */
public class CoffeeTypeListViewActivity extends JacksonSpringAndroidSpicedActivity {
	private String mLastRequestCacheKey;
	private OkHttpBitmapSpiceManager spiceManagerBinary = new OkHttpBitmapSpiceManager();

	private RelativeLayout mCoffeeTypesListLayout;
	private RelativeLayout mCoffeeTypesListInnerProgressLayout;
	private ProgressBar    mCoffeeTypesProgressBar;
	private ListView       mCoffeeTypesListView;
	private ActionBar      mActionBar;
	private LayoutInflater mInflater;
	private View           mCustomActionBarView;
	private View           mErrorPromptView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coffee_list);

		initActionBar();

		mCoffeeTypesListLayout = (RelativeLayout) findViewById(R.id.coffee_types_list_relative_layout);
		mCoffeeTypesListInnerProgressLayout = (RelativeLayout) findViewById(R.id.coffee_types_list_inner_progress_layout);
		mCoffeeTypesProgressBar = (ProgressBar) findViewById(R.id.coffee_types_list_progress_bar);
		mCoffeeTypesListView = (ListView) findViewById(R.id.coffee_types_list_list_view);

		mCoffeeTypesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				CoffeeType clickedCoffeeType = (CoffeeType) adapterView.getItemAtPosition(i);
				Intent showCoffeeTypeDetailedViewIntent = new Intent(CoffeeTypeListViewActivity.this, CoffeeTypeDetailedViewAcitivity.class);
				showCoffeeTypeDetailedViewIntent.putExtra("coffeeType", clickedCoffeeType);

				startActivity(showCoffeeTypeDetailedViewIntent);


			}
		});

		List<CoffeeType> coffeeTypes = SugarRecord.listAll(CoffeeType.class);

		if (coffeeTypes != null && coffeeTypes.size() > 0) {
			CoffeeTypeListSpiceAdapter coffeeTypeListSpiceAdapter = new CoffeeTypeListSpiceAdapter(this, spiceManagerBinary, coffeeTypes);
			mCoffeeTypesListView.setAdapter(coffeeTypeListSpiceAdapter);
		}

	}

	/**
	 * initialize the actionbar
	 */
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


	/**
	 *
	 * @param coffeeTypes the {@link com.percolate.coffee.model.CoffeeTypeList} which is desired to be the adapter of mCoffeeTypesListView, if and adapter already exists, it is smply notified of new data
	 */
	private void updateListViewContent(CoffeeTypeList coffeeTypes) {
		if (mCoffeeTypesListView.getAdapter() != null) {
			mCoffeeTypesListView.deferNotifyDataSetChanged();
		} else {
			CoffeeTypeListSpiceAdapter coffeeTypeListSpiceAdapter = new CoffeeTypeListSpiceAdapter(this, spiceManagerBinary, coffeeTypes);
			mCoffeeTypesListView.setAdapter(coffeeTypeListSpiceAdapter);
		}
	}

	/**
	 * Perform a CoffeeType index request from the coffeeapi server
	 */
	private void performIndexRequest() {
		CoffeeTypeListViewActivity.this.setProgressBarIndeterminateVisibility(true);

		CoffeeTypeIndexRequest request = new CoffeeTypeIndexRequest(getResources());
		mLastRequestCacheKey = request.getCacheKey(getResources());

		spiceManager.execute(request, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new CoffeeTypeIndexRequestListener());
		animateProgressBar(ProgressBarAnimation.ENTER);
	}

	/**
	 * Initialize the animation of the progress bar (either in or out)
	 * @param progressBarAnimation an enum ({@link com.percolate.coffee.activity.CoffeeTypeListViewActivity.ProgressBarAnimation}) used to determin whether to animate the progress bar as an entrance or an exit
	 */
	private void animateProgressBar(final ProgressBarAnimation progressBarAnimation) {

		new ListProgressBarAnimationFactory(mCoffeeTypesListLayout)
				.shown(progressBarAnimation.mShown)
				.onStartAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mCoffeeTypesListLayout.getLayoutParams();
						lp.bottomMargin = ListProgressBarAnimationFactory.PROGRESS_BAR_END_POSITION;
						mCoffeeTypesListLayout.setLayoutParams(lp);
					}
				})
				.onEndAnimationRunnable(new Runnable() {
					@Override
					public void run() {
						if (progressBarAnimation.mShown) {
							mCoffeeTypesProgressBar.setVisibility(View.GONE);
							RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mCoffeeTypesListLayout.getLayoutParams();
							lp.topMargin = -ListProgressBarAnimationFactory.PROGRESS_BAR_END_POSITION;
							mCoffeeTypesListLayout.setLayoutParams(lp);
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

	/**
	 * an enum used to determin whether to animate the progress bar as an entrance or an exit
	 */
	private enum ProgressBarAnimation {
		ENTER(false),
		EXIT(true);

		private boolean mShown;

		ProgressBarAnimation(boolean shown) {
			mShown = shown;
		}
	}

	// ------ INNER CLASSES ------

	/**
	 * A request listener which is notified when a request (index requests) whether it fails or succeeds
	 */
	private class CoffeeTypeIndexRequestListener implements RequestListener<CoffeeTypeList> {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Log.i("CoffeeTypeIndexRequestListener", "COFFEE INDEX REQUEST FAILED");
			Log.i("CoffeeTypeIndexRequestListener", "MESSAGE -> " + spiceException.getLocalizedMessage());
			animateProgressBar(ProgressBarAnimation.EXIT);

			mErrorPromptView = mInflater.inflate(R.layout.an_error_has_occured, null);
			((TextView) mErrorPromptView.findViewById(R.id.error_occured_prompt_text)).setText(spiceException.getLocalizedMessage());

			if (mCoffeeTypesListView.getHeaderViewsCount() == 0) {
				mCoffeeTypesListView.addHeaderView(mErrorPromptView);
			}
		}

		@Override
		public void onRequestSuccess(CoffeeTypeList coffeeTypes) {
			Log.i("CoffeeTypeIndexRequestListener", "COFFEE INDEX REQUEST SUCCESS!");
			animateProgressBar(ProgressBarAnimation.EXIT);
			updateListViewContent(coffeeTypes);
			coffeeTypes.saveAll();


			if (mCoffeeTypesListView.getHeaderViewsCount() > 0 && mErrorPromptView != null) {
				mCoffeeTypesListView.removeHeaderView(mErrorPromptView);
			}
		}
	}

}

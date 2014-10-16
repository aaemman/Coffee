package com.percolate.coffee.activity;

import android.app.Activity;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

public class JacksonSpringAndroidSpicedActivity extends Activity {

	protected SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);

	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}
}

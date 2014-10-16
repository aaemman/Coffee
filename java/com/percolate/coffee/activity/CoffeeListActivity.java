package com.percolate.coffee.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.percolate.coffee.R;
import com.percolate.coffee.util.api.request.CoffeeTypeIndexRequest;
import com.percolate.coffee.util.api.requestlistener.CoffeeTypeIndexRequestListener;


public class CoffeeListActivity extends JacksonSpringAndroidSpicedActivity {
	private String mLastRequestCacheKey;

	Button testRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_list);

	    testRequest = (Button) findViewById(R.id.testRequestButton);
	    testRequest.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    performRequest();
		    }
	    });

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

	private void performRequest() {
		CoffeeListActivity.this.setProgressBarIndeterminateVisibility(true);

		CoffeeTypeIndexRequest request = new CoffeeTypeIndexRequest(getResources());
		mLastRequestCacheKey = request.getCacheKey(getResources());

		spiceManager.execute(request, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new CoffeeTypeIndexRequestListener());
	}
}

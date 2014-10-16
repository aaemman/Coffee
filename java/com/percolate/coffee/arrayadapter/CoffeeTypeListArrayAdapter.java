package com.percolate.coffee.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.percolate.coffee.R;
import com.percolate.coffee.util.api.pojo.CoffeeType;
import com.percolate.coffee.util.api.pojo.CoffeeTypeList;
import com.percolate.coffee.util.view.ImageUtils;

import java.util.List;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class CoffeeTypeListArrayAdapter extends ArrayAdapter<CoffeeType> {
	final Context mContext;

	public CoffeeTypeListArrayAdapter(Context context, CoffeeTypeList objects) {
		super(context, R.layout.coffee_list_item, objects);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.coffee_list_item, parent, false);

			holder = new ViewHolder();

			holder.name = (TextView) convertView.findViewById(R.id.coffee_list_item_name_text_view);
			holder.description = (TextView) convertView.findViewById(R.id.coffee_list_item_description_text_view);
			holder.picture = (ImageView) convertView.findViewById(R.id.coffee_list_item_picture_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CoffeeType coffeeTypeItem = getItem(position);

		if (coffeeTypeItem != null) {
			holder.name.setText(coffeeTypeItem.getName());
			holder.description.setText(coffeeTypeItem.getDescription());
			holder.picture.setTag(coffeeTypeItem.getImageUrl());
			holder.picture.setImageDrawable(ImageUtils.loadImageFromWeb(coffeeTypeItem.getImageUrl()));
		}

		return convertView;
	}

	// ------ INNER CLASSES ------

	static class ViewHolder {
		TextView name;
		TextView description;
		ImageView picture;
	}
}

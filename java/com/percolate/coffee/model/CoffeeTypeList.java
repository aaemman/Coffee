package com.percolate.coffee.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom ArrayList made specifically to handle lists of {@link com.percolate.coffee.model.CoffeeType}. This class is required in order for {@link com.octo.android.robospice.spicelist.simple.SpiceArrayAdapter} to work with CoffeeType; ex. {@link com.percolate.coffee.adapter.CoffeeTypeListSpiceAdapter}
 */
public class CoffeeTypeList extends ArrayList<CoffeeType> {

	/**
	 * Saves all values in this list given that they are unique
	 */
	public void saveAll() {
		List<CoffeeType> coffeeTypeList;
		for (CoffeeType coffeeType : this) {
			coffeeTypeList = SugarRecord.find(CoffeeType.class, "m_coffee_type_id = ?", coffeeType.getCoffeeTypeId());
			if (coffeeTypeList.size() != 0) {
				if (!(coffeeTypeList.get(0).equals(coffeeType))) {
					coffeeTypeList.get(0).delete();
					coffeeType.save();
				}
			} else {
				coffeeType.save();
			}
		}
	}
}


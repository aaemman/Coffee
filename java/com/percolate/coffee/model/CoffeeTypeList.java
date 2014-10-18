package com.percolate.coffee.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class CoffeeTypeList extends ArrayList<CoffeeType> {

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


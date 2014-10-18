package com.percolate.coffee.model;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class CoffeeTypeList extends ArrayList<CoffeeType> {

	public void saveAll() {
		for (CoffeeType coffeeType : this) {
			if(SugarRecord.find(CoffeeType.class, "m_coffee_type_id = ?", coffeeType.getCoffeeTypeId()).size() == 0) {
				coffeeType.save();
			}
		}
	}
}


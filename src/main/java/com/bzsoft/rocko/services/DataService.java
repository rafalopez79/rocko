package com.bzsoft.rocko.services;

import com.bzsoft.rocko.model.DataResult;

public interface DataService {

	public DataResult find(String sku, int location);
}

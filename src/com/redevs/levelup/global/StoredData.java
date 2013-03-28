package com.redevs.levelup.global;

import java.util.ArrayList;

public class StoredData {
	
	private static ArrayList<DataObject> list = new ArrayList<DataObject>();
	
	private StoredData() {}
	
	public static void store(DataObject d) {
		list.add(d);
		
	}
	
	public static DataObject get(int i) {
		return list.get(i);
	}

	
	

}

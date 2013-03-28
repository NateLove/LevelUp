package com.redevs.levelup.tabs;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AddFriendActivity extends ListActivity {
	
	private ArrayList<String> friends;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		friends = i.getStringArrayListExtra("asdf");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, friends);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(AddFriendActivity.this, TabsActivity.class);
        i.putExtra("selected", position);
        startActivity(i);
	}
}
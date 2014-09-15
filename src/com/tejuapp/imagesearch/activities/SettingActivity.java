package com.tejuapp.imagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tejuapp.imagesearch.R;
import com.tejuapp.imagesearch.R.id;
import com.tejuapp.imagesearch.R.layout;
import com.tejuapp.imagesearch.R.menu;
import com.tejuapp.imagesearch.model.ImageSetting;

public class SettingActivity extends Activity {

	EditText etSize;
	EditText etColor;
	EditText etType;
	EditText etSite;
	Button btSubmit;
	
	ImageSetting settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setupActivity();
		settings = (ImageSetting) getIntent().getSerializableExtra("settings");
		etSize.setText(settings.getSize());
		etColor.setText(settings.getColor());
		etType.setText(settings.getType());
		etSite.setText(settings.getSite());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
	
	private void setupActivity(){
		etSize = (EditText) findViewById(R.id.etSize);
		etColor = (EditText) findViewById(R.id.etColor);
		etType = (EditText) findViewById(R.id.etType);
		etSite = (EditText) findViewById(R.id.etSite);
	}
	
	public void onSubmit(View v){
		String size = etSize.getText().toString();
		String color = etColor.getText().toString();
		String type = etType.getText().toString();
		String site = etSite.getText().toString();
		
		settings.setSize(size);
		settings.setColor(color);
		settings.setType(type);
		settings.setSite(site);
		
		Intent i = new Intent();
		i.putExtra("settings", settings);
		setResult(RESULT_OK,i);
		finish();
	}

}

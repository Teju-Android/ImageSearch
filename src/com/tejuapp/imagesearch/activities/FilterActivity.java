package com.tejuapp.imagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tejuapp.imagesearch.R;
import com.tejuapp.imagesearch.model.ImageSetting;

public class FilterActivity extends Activity {

	Spinner spSize;
	Spinner spColor;
	Spinner spType;
	EditText etSite;
	ImageSetting settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		settings = (ImageSetting) getIntent().getSerializableExtra("settings");
		setupActivity();
		setInputs();
	}

	private void setupActivity() {
		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spColor);
		spType = (Spinner) findViewById(R.id.spType);
		etSite = (EditText) findViewById(R.id.etImageSite);
	}
	
	private void setInputs(){
		if(settings.getSize()!=null){
			spSize.setSelection(getSpinnerIndex(spSize, settings.getSize()));
		}
		
		if(settings.getColor()!=null){
			spColor.setSelection(getSpinnerIndex(spColor, settings.getColor()));
		}
		
		if(settings.getType()!=null){
			spType.setSelection(getSpinnerIndex(spType, settings.getSize()));
		}
		if(settings.getSite()!=null){
			etSite.setText(settings.getSite());
		}
		
	}
	
	private int getSpinnerIndex(Spinner sp, String value){
		int index = 0;
		for (int i=0;i<sp.getCount();i++){
			if (sp.getItemAtPosition(i).equals(value)){
				index = i;
			}
		}
		return index;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

	public void onSubmit(View v){
		if(!spSize.getSelectedItem().toString().equals("Size Not Selected"))
			settings.setSize(spSize.getSelectedItem().toString());
		if(!spColor.getSelectedItem().toString().equals("Color Not Selected"))
			settings.setColor(spColor.getSelectedItem().toString());
		if(!spType.getSelectedItem().toString().equals("Type Not Selected"))
			settings.setType(spType.getSelectedItem().toString());
		if(!etSite.getText().toString().equals(""))
			settings.setSite(etSite.getText().toString());
		
		Intent i = new Intent();
		i.putExtra("settings", settings);
		setResult(RESULT_OK,i);
		finish();
	}
}

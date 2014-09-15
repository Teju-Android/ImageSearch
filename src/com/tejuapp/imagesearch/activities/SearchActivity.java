package com.tejuapp.imagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.imagesearch.R;
import com.tejuapp.imagesearch.adapters.ImageResultsAdapter;
import com.tejuapp.imagesearch.listener.EndlessScrollListener;
import com.tejuapp.imagesearch.model.ImageResult;
import com.tejuapp.imagesearch.model.ImageSetting;

public class SearchActivity extends Activity {

    private EditText etQuery;
    private Button btSearch;
    private GridView gvImageResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private int imageNumber;
    private ImageSetting settings;
    final String DEFAULT_SITE = "google.com";
    final String DEFAULT_SIZE = "medium";
    final String DEFAULT_COLOR = "white";
    final String DEFAULT_TYPE = "photo";
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupView();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvImageResults.setAdapter(aImageResults);
        settings = new ImageSetting(DEFAULT_SIZE, DEFAULT_COLOR, DEFAULT_TYPE, DEFAULT_SITE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    
    private void setupView(){
    	etQuery = (EditText) findViewById(R.id.etSearchText);
    	btSearch = (Button) findViewById(R.id.btSubmitSearch);
    	gvImageResults = (GridView) findViewById(R.id.gvImageResults);
    	gvImageResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
	    		Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
	    		ImageResult result = imageResults.get(position);
	    		i.putExtra("url", result.getFullUrl());
	    		startActivity(i);
	    	}
    	});
    	
    	gvImageResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadMoreDataFromApi(page);
			}
		});
    	
    }
    
    public void onImageSearch(View v){
    	startSearch();
    }
    
    private void startSearch(){
    	imageNumber = 0;
    	imageResults.clear();
    	populateData();
    }
    
    private void populateData(){
    	String query = etQuery.getText().toString();
    	String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q="+query+"&v=1.0&rsz=8&start="+imageNumber;
    	searchUrl += setUrlWithImageSettings();
    	Log.d("DEBUG","SEARCH IRL IS "+searchUrl);
    	imageNumber += 8;
    	Toast.makeText(this, "Searching for \""+query+"\"", Toast.LENGTH_SHORT).show();
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.get(searchUrl, new JsonHttpResponseHandler(){
    		@Override
    		public void onSuccess(int statusCode, Header[] headers, JSONObject response){
    			Log.d("DEBUG", response.toString());
    			JSONArray imageResultsJSON = null;
    			try {
					imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
					aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
    			Log.d("imageArray", imageResults.toString());
    		}
    	});	
    }
    
    private String setUrlWithImageSettings(){
    	String url = "&imgsz="+settings.getSize();
    	url += "&imgcolor="+settings.getColor();
    	url += "&imgtype="+settings.getType();
    	url += "&as_sitesearch="+settings.getSite();
    	return url;
    }
    
    private void customLoadMoreDataFromApi(int offset){
    	populateData();
    }
    
    public void onClickSettings(MenuItem mi){
    	Toast.makeText(this, "Added Item", Toast.LENGTH_SHORT).show();
    	Intent i=new Intent(this, SettingActivity.class);
    	i.putExtra("settings", settings);
    	startActivityForResult(i, 5);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if(requestCode ==5){
    		if(resultCode == RESULT_OK){
    			settings = (ImageSetting) data.getSerializableExtra("settings");
    			Log.d("HELP-SITE",settings.getSite()+" -----"+settings.getSize());
    			startSearch();
    			//Toast.makeText(this, settings.getSize(), Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
}

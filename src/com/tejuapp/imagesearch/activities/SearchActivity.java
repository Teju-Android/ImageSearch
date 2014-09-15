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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pavantej.imagesearch.R;
import com.tejuapp.imagesearch.adapters.ImageResultsAdapter;
import com.tejuapp.imagesearch.listener.EndlessScrollListener;
import com.tejuapp.imagesearch.model.ImageResult;

public class SearchActivity extends Activity {

    private EditText etQuery;
    private Button btSearch;
    private GridView gvImageResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private int imageNumber;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupView();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvImageResults.setAdapter(aImageResults);
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
    	imageNumber = 0;
    	imageResults.clear();
    	populateData();
    }
    
    private void populateData(){
    	String query = etQuery.getText().toString();
    	String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q="+query+"&v=1.0&rsz=8&start="+imageNumber;
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
    private void customLoadMoreDataFromApi(int offset){
    	populateData();
    }
    
}

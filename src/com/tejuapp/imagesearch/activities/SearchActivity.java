package com.tejuapp.imagesearch.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.imagesearch.R;
import com.tejuapp.imagesearch.adapters.ImageResultsAdapter;
import com.tejuapp.imagesearch.listener.EndlessScrollListener;
import com.tejuapp.imagesearch.model.ImageResult;
import com.tejuapp.imagesearch.model.ImageSetting;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {

    private GridView gvImageResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private int imageNumber;
    private ImageSetting settings;
    private SearchView mSearchView;
    private String query;
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
        settings = new ImageSetting();
     // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          //doMySearch(query);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }
    
    private void setupSearchView(MenuItem searchItem) {
    	mSearchView.setIconifiedByDefault(false);
    	searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
       
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(this);
    }

    
    private void setupView(){
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
    	//String query = etQuery.getText().toString();
    	String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q="+query+"&v=1.0&rsz=8&start="+imageNumber;
    	searchUrl += setUrlWithImageSettings();
    	Log.d("DEBUG","SEARCH IRL IS "+searchUrl);
    	imageNumber += 8;
    	//Toast.makeText(this, "Searching for \""+query+"\"", Toast.LENGTH_SHORT).show();
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
    	String url="";
    	if(settings.getSize()!=null)
    		url +=  "&imgsz="+settings.getSize();
    	if(settings.getColor()!=null)
    		url += "&imgcolor="+settings.getColor();
    	if(settings.getType()!=null)
    		url += "&imgtype="+settings.getType();
    	if(settings.getSite()!=null)
    		url += "&as_sitesearch="+settings.getSite();
    	return url;
    }
    
    private void customLoadMoreDataFromApi(int offset){
    	populateData();
    }
    
    public void onClickSettings(MenuItem mi){
    	//Toast.makeText(this, "Added Item", Toast.LENGTH_SHORT).show();
    	Intent i=new Intent(this, FilterActivity.class);
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


	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		this.query = query;
		startSearch();
		return false;
	}
    
}

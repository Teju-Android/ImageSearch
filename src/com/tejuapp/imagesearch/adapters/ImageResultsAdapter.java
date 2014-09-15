package com.tejuapp.imagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pavantej.imagesearch.R;
import com.squareup.picasso.Picasso;
import com.tejuapp.imagesearch.model.ImageResult;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsAdapter(Context context, List<ImageResult> images) {
		super(context, android.R.layout.simple_list_item_1, images);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
		ImageResult imageInfo = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent,false);
		}
		
		ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		ivImage.setImageResource(0);
		tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
		Picasso.with(getContext()).load(imageInfo.getThumbUrl()).into(ivImage);
		return convertView;
	}

	

}

package com.tshirt_os_server;
 
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ImageLoader imLoader;
 
    // Keep all Images in array
 
    // Constructor
    public ImageAdapter(Context contxt){
        mContext = contxt;
        imLoader = new ImageLoader(mContext);
    }
 
    @Override
    public int getCount() {
        return DataPref.url.length;
    }
 
    @Override
    public Object getItem(int position) {
        return DataPref.url[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	
        ImageView imageView = new ImageView(mContext);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(185, 211));
        imLoader.displayImage(DataPref.url[position], imageView);
       
        
        return imageView;
    }
 
}
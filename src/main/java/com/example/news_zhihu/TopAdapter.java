package com.example.news_zhihu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHC on 2017/2/13.
 */

public class TopAdapter extends PagerAdapter {

    private List<Bitmap> bitmapList;
    private Context context;
    private List<Integer> toppictureid;

    public TopAdapter(List<Bitmap> bitmapList, Context context,List<Integer> toppictureid) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.toppictureid=toppictureid;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView=new ImageView(context);
        imageView.setImageBitmap(bitmapList.get(position));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailednew.class);
                String url = "http://daily.zhihu.com/story/";
                String urlpath = url + toppictureid.get(position);
                intent.putExtra("Path", urlpath);
                context.startActivity(intent);
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

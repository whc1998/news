package com.example.news_zhihu;

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

    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private List<ImageView> imageViewList = new ArrayList<>();

    public TopAdapter(List<Bitmap> bitmapList, List<String> stringList, List<ImageView> imageViewList) {
        this.bitmapList = bitmapList;
        this.stringList = stringList;
        this.imageViewList = imageViewList;

    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        return imageViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }
}

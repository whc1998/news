package com.example.news_zhihu;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WHC on 2017/2/13.
 */

public class MyAdapter extends BaseAdapter {

    private List<String> list;
    private List<Bitmap> bitmapList;
    private int layoutresouse, tv_id, iv_id;
    private LayoutInflater inflater;
    private Context context;
    private TextView textView;
    private ImageView imageView;

    public MyAdapter(Context context, List<String> list, List<Bitmap> bitmapList, int layoutresouse, int tv_id, int iv_id) {
        this.context = context;
        this.list = list;
        this.bitmapList = bitmapList;
        this.layoutresouse = layoutresouse;
        this.tv_id = tv_id;
        this.iv_id = iv_id;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(layoutresouse, null);
        textView = (TextView) view.findViewById(tv_id);
        imageView = (ImageView) view.findViewById(iv_id);
        textView.setText(list.get(i));
        imageView.setImageBitmap(bitmapList.get(i));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }

}

package com.example.news_zhihu;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WHC on 2017/3/5.
 */

class MainAdapter extends RecyclerView.Adapter<MyViewholder> {

    private LayoutInflater inflater;
    private Context context;
    private final static int VIEW_HEADER = 1, VIEW_ITEM = 0;
    private List<String> mdatas;
    private List<Bitmap> bitmapList,Top_bitmap;
    private List<Integer> toppictureid;
    private List<String> top_title;
    private OnItemClickListener onItemClickListener;

    public MainAdapter(Context context,List<String> mdatas,List<Bitmap> bitmapList,List<Bitmap> Top_bitmap,
                       List<Integer> toppictureid,List<String> top_title){
        this.context=context;
        this.mdatas=mdatas;
        this.bitmapList=bitmapList;
        this.Top_bitmap=Top_bitmap;
        this.toppictureid=toppictureid;
        this.top_title=top_title;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==VIEW_HEADER){
            view =inflater.inflate(R.layout.recycleview_header,parent,false);
        }else {
            view = inflater.inflate(R.layout.maincontent_list_style, null);
        }
        MyViewholder viewholder=new MyViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final MyViewholder holder, final int position) {
        if (getItemViewType(position)==VIEW_HEADER){
            TopAdapter adapter=new TopAdapter(Top_bitmap,context,toppictureid);
            holder.toptv.setText(top_title.get(0));
            holder.viewPager.setAdapter(adapter);
            holder.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                @Override
                public void onPageSelected(int position1) {
                    holder.toptv.setText(top_title.get(position1));
                }
            });
            return;
        }else{
            holder.textView.setText(mdatas.get(position-1));
            holder.imageView.setImageBitmap(bitmapList.get(position-1));
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_HEADER;
        } else {
            return VIEW_ITEM;
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
class MyViewholder extends RecyclerView.ViewHolder{

    TextView textView;
    ImageView imageView;
    ViewPager viewPager;
    TextView toptv;

    public MyViewholder(View itemView) {
        super(itemView);
        textView= (TextView) itemView.findViewById(R.id.tv_title);
        imageView= (ImageView) itemView.findViewById(R.id.iv_image);
        viewPager= (ViewPager) itemView.findViewById(R.id.viewpager);
        toptv = (TextView) itemView.findViewById(R.id.vp_text);
    }
}
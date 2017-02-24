package com.example.news_zhihu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    private List<Map<String, Object>> list;
    private ListView drawer_listView, maincontent_listview;
    private int[] images = {android.R.drawable.ic_input_add};
    private String[] text = {"日常心理学", "用户推荐日报", "电影日报", "不许无聊", "设计日报"
            , "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报"};
    private List<String> list_title = new ArrayList<>();
    private List<String> top_title = new ArrayList<>();
    private List<Bitmap> picture = new ArrayList<>();
    private List<Bitmap> top_picture = new ArrayList<>();
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<Integer> TitleId = new ArrayList<>();
    private List<Integer> toppictureid = new ArrayList<>();
    private String path = "http://news-at.zhihu.com/api/4/news/latest";
    private ViewPager viewPager;
    private TextView toptv;
    private Toolbar toolbar;
    private Handler handler = new Handler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        drawer_listView = (ListView) findViewById(R.id.drawer_list);
        maincontent_listview = (ListView) findViewById(R.id.maincontent_list);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        toptv = (TextView) findViewById(R.id.vp_text);
        //toolbar
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,0, 0);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nilgt:

                        Toast.makeText(MainActivity.this, "夜间模式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        Toast.makeText(MainActivity.this, "设置选项", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        donw();
        list = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", images[0]);
            map.put("text", text[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.drawer_list_style,
                new String[]{"img", "text"}, new int[]{R.id.ig_style, R.id.tv_style});
        drawer_listView.setAdapter(adapter);
        //viewpager轮播
        viewPager.setCurrentItem(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (viewPager.getCurrentItem() == imageViewList.size() - 1) {
                                viewPager.setCurrentItem(0);
                            } else {
                                //这里是设置当前页的下一页
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            }
                        }
                    });
                }
            }
        }).start();

    }

    private void donw() {
        new Httpcontion(path, list_title, picture, TitleId, handler, top_title, top_picture, toppictureid).start();
    }

    @Override
    public boolean handleMessage(Message message) {
        MyAdapter adapter = new MyAdapter(MainActivity.this, list_title, picture,
                R.layout.maincontent_list_style, R.id.tv_title, R.id.iv_image);
        maincontent_listview.setAdapter(adapter);

        maincontent_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Detailednew.class);
                String url = "http://daily.zhihu.com/story/";
                String urlpath = url + TitleId.get(i);
                intent.putExtra("Path", urlpath);
                startActivity(intent);
            }
        });

        for (int i = 0; i < top_picture.size(); i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageBitmap(top_picture.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);

            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Detailednew.class);
                    String url = "http://daily.zhihu.com/story/";
                    String urlpath = url + toppictureid.get(finalI);
                    intent.putExtra("Path", urlpath);
                    startActivity(intent);
                }
            });
        }
        viewPager.setAdapter(new TopAdapter(top_picture, top_title, imageViewList));
        toptv.setText(top_title.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                toptv.setText(top_title.get(position));
            }
        });
        return false;
    }

}

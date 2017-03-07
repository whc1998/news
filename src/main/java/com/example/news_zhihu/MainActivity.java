package com.example.news_zhihu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private ListView drawer_listView;
    private int[] images = {android.R.drawable.ic_input_add};
    private String[] text = {"日常心理学", "用户推荐日报", "电影日报", "不许无聊", "设计日报"
            , "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报"};
    private List<String> list_title = new ArrayList<>();
    private List<String> top_title = new ArrayList<>();
    private List<Bitmap> picture = new ArrayList<>();
    private List<Bitmap> top_picture = new ArrayList<>();
    private List<Integer> TitleId = new ArrayList<>();
    private List<Integer> toppictureid = new ArrayList<>();
    private String path = "http://news-at.zhihu.com/api/4/news/latest";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private int position=0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        donw();
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        drawer_listView = (ListView) findViewById(R.id.drawer_list);
        viewPager= (ViewPager) findViewById(R.id.viewpager);

//        drawer_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(MainActivity.this,intrdoce.class);
//                intent.putExtra("key",text[i]);
//                startActivity(intent);
//            }
//        });

        recyclerView= (RecyclerView) findViewById(R.id.recyle_view1);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.sr);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//刷新完毕
                        Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                },3000);
            }
        });

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

        list = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", images[0]);
            map.put("text", text[i]);
            list.add(map);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,text);
        drawer_listView.setAdapter(adapter);

    /**
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
                            if (viewPager.getCurrentItem() == top_picture.size() - 1) {
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
    */
    }

    private void donw() {
        new Httpcontion(path, list_title, picture, TitleId, handler, top_title, top_picture, toppictureid).start();
    }

    @Override
    public boolean handleMessage(Message message) {

        //recycleview
        MainAdapter mainAdapter=new MainAdapter(MainActivity.this,list_title,picture,top_picture,toppictureid,top_title);
        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, Detailednew.class);
                String url = "http://daily.zhihu.com/story/";
                String urlpath = url + TitleId.get(position-1);
                intent.putExtra("Path", urlpath);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mainAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL
        ,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //notification
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this);
        long[] vibrate={0,200,100,200};
        builder.setVibrate(vibrate);
        builder.setSmallIcon(R.drawable.picture2);
        builder.setContentTitle("装逼日报");
        builder.setContentText(top_title.get(0));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture2);
        builder.setLargeIcon(bitmap);
        builder.setAutoCancel(false);
        Intent intent=new Intent(MainActivity.this,Detailednew.class);
        String url1 = "http://daily.zhihu.com/story/";
        String urlpath = url1 + toppictureid.get(0);
        intent.putExtra("Path",urlpath);
        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,1,intent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        Notification notification=builder.build();
        manager.notify(1,notification);
        return false;
    }

}

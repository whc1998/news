package com.example.news_zhihu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHC on 2017/2/13.
 */

public class Httpcontion extends Thread {

    private String address;
    private List<String> list_title, top_title = new ArrayList<>();
    private List<Bitmap> image, top_picture = new ArrayList<>();
    private List<Integer> TitleId,id_toppicture=new ArrayList<>();
    private Handler handler;

    public Httpcontion(String address, List<String> list_title, List<Bitmap> image,List<Integer> TitleId,
                       Handler handler, List<String> top_title, List<Bitmap> top_picture,List<Integer> id_toppicture) {
        this.address = address;
        this.list_title = list_title;
        this.TitleId=TitleId;
        this.image = image;
        this.handler = handler;
        this.top_title = top_title;
        this.top_picture = top_picture;
        this.id_toppicture=id_toppicture;
    }

    @Override
    public void run() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");
                resolver(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resolver(String jsonresouse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonresouse);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String picture = object.optJSONArray("images").getString(0);
                String title = object.getString("title");
                int titleid=object.getInt("id");
                TitleId.add(titleid);
                list_title.add(title);
                donwpicture(picture, image);
            }
            JSONArray jsonArray_top = jsonObject.getJSONArray("top_stories");
            for (int i = 0; i < jsonArray_top.length(); i++) {
                JSONObject object = jsonArray_top.getJSONObject(i);
                String toppicture = object.getString("image");
                String toptitle = object.getString("title");
                int toppictureid=object.getInt("id");
                id_toppicture.add(toppictureid);
                top_title.add(toptitle);
                donwpicture(toppicture, top_picture);
            }
            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void donwpicture(String donwpath, List<Bitmap> top_picture) {
        try {
            URL url = new URL(donwpath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                top_picture.add(bitmap);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

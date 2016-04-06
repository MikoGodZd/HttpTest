package com.miko.zd.httptest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    String text;
    StringBuffer sb;
    TextView textView;

    Button btHandler;
    Button btAsy;
    Button btRunOn;

    Bitmap bm;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    private void initEvents() {
        btHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(Get).start();
            }
        });

        btAsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsynacGet asy = new AsynacGet("http://img02.tooopen.com/images/20160216/tooopen_sy_156324542564.jpg");
                asy.execute();
            }
        });

        btRunOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(RunOn).start();
            }
        });
    }

    private void initView() {
        btHandler = (Button) findViewById(R.id.bt_cb_handler);
        btAsy = (Button) findViewById(R.id.bt_cb_asy);
        btRunOn = (Button) findViewById(R.id.bt_cb_runon);

        iv = (ImageView) findViewById(R.id.iv_change);
    }

    public String InputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i = -1;
        while (i != -1) {
            i = is.read();
            out.write(i);
        }
        return out.toString();
    }

    public InputStream streampost(String remote_addr) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(remote_addr);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inStream;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.setImageBitmap(bm);
        }
    };

    Runnable GetText = new Runnable() {
        @Override
        public void run() {
            InputStream inputStream = streampost("www.baidu.com");
            try {
                sb = new StringBuffer();
                sb.append(InputStream2String(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            MainActivity.this.handler.sendMessage(msg);
        }
    };
    Runnable Get = new Runnable() {
        @Override
        public void run() {
            GetPicture("http://www.pp3.cn/uploads/allimg/111111/10142UL5-3.jpg");
            Message msg = new Message();
            MainActivity.this.handler.sendMessage(msg);
        }
    };

    public void GetPicture(String url) {
        URL aURL = null;
        try {
            aURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn = null;
        try {
            conn = aURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        bm = BitmapFactory.decodeStream(bis);
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class AsynacGet extends AsyncTask {

        String url;
        AsynacGet(String url){
            this.url=url;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetPicture(url);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            iv.setImageBitmap(bm);
        }
    }
    Runnable RunOn =new Runnable() {
        @Override
        public void run() {
            GetPicture("http://img5.imgtn.bdimg.com/it/u=876917683,706781518&fm=11&gp=0.jpg");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bm);
                }
            });
        }
    };
}

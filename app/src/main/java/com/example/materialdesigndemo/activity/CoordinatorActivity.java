package com.example.materialdesigndemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsAdapter;
import com.example.materialdesigndemo.model.News;
import com.example.materialdesigndemo.utils.HttpsUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class CoordinatorActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // 常量：聚合数据的url网址和handle的消息
    private static final String NEWS_URL = "http://v.juhe.cn/toutiao/index";
    private static final int GET_NEWS = 1;
    private List<News> newsList;


    // 数据处理的对象
    private NewsHandler handler;
    private NewsAdapter adapter;

    // 控件对象
    private Toolbar toolbar;
    private RecyclerView rvNews;
    private CollapsingToolbarLayout collapsingToolbar;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_coordinator );
        handler = new NewsHandler( this);

        initView();
        initData();

    }
    private void initView() {
        // 1. 初始化Toolbar
        toolbar = findViewById(R.id.tool_bar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("新闻头条");
        setSupportActionBar(toolbar);


        //初始化RecyclverView列表和自动化刷新布局
        rvNews = findViewById( R.id.rv_news );
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        String appKey = "f00aad74d887eeb41d55be4ef23a0025";
        String url = NEWS_URL + "?key=" + appKey + "&type=top";
        Request request = new Request.Builder().url(url).build();
        HttpsUtil.getInstance().newCall(request).enqueue( new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("RecyclverViewActivity", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    JSONObject obj = JSON.parseObject(json);
                    JSONObject result = obj.getJSONObject("result");
                    if (result != null) {
                        JSONArray data = result.getJSONArray("data");
                        if(data != null && !data.isEmpty()) {
                            Message msg = handler.obtainMessage();
                            msg.what = GET_NEWS;
                            msg.obj = data.toJSONString();  //
                            handler.sendMessage(msg);
                        }
                    }
                } else {
                    Log.e("NewsListActivity", response.message());
                }
            }
        });






    }






    @Override
    public void onRefresh() {
        initData();
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 3000);


    }

    static class NewsHandler extends Handler {
        private WeakReference<Activity> ref;

        public NewsHandler(CoordinatorActivity activity) {
            this.ref = new WeakReference <Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CoordinatorActivity activity = (CoordinatorActivity) this.ref.get();
            if (msg.what == GET_NEWS) {
                // 获取数据
                String json = (String) msg.obj;
                activity.newsList = JSON.parseArray(json, News.class);

                // 设置RecyclerView的分割线和布局
//                DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
//                activity.rvNews.addItemDecoration(decoration);

                activity.rvNews.setLayoutManager(new LinearLayoutManager(activity));

                // 设置Adapter
                activity.adapter = new NewsAdapter(activity.newsList);
                activity.rvNews.setAdapter(activity.adapter);

                // 设置事件监听
                activity.adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(activity, NewsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("news", activity.newsList.get(position));
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                });
            }
        }
    }

    public  void sendEmail(View view){
        Snackbar.make( view,"正在发送邮件.......",Snackbar.LENGTH_LONG ).show();


    }
}

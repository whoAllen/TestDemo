package com.languo.testdemo;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * CollapsingToolbarLayout 的使用，实现标题栏悬等效果（仿豆瓣小组滑动动效）
 */
public class ScrollActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> stringList = new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;

    private static final String TAG = "ScrollActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        recyclerView = findViewById(R.id.scroll_recycler_view);
        appBarLayout = findViewById(R.id.app_bar_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        for(int i = 0; i < 20; i++) {
            stringList.add("1" + i);
        }
        ScrollAdapter scrollAdapter = new ScrollAdapter(this, stringList);
        recyclerView.setAdapter(scrollAdapter);


        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {

                    //展开状态
                    Log.i(TAG, "onStateChanged: " + "展开");

                }else if(state == State.COLLAPSED){

                    //折叠状态
                    Log.i(TAG, "onStateChanged: " + "折叠");

                }else {

                    //中间状态
                    Log.i(TAG, "onStateChanged: " + "中间");

                }
            }
        });

    }

    class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ScrollViewHolder> {

        private List<String> stringList;
        private Context mContext;

        public ScrollAdapter(Context context, List<String> stringList) {
            this.mContext = context;
            this.stringList = stringList;
        }


        @Override
        public ScrollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scroll, parent, false);
            return new ScrollViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ScrollViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        class ScrollViewHolder extends RecyclerView.ViewHolder {

            public ScrollViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}

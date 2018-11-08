package com.languo.testdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mainTvButton;
    private TextView mainTvNotification, mainTvCollapsingToolbar;
    private SeekBar mainSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initClick();

        //动态添加布局
        addViewToLinearLayout();
        //带进度的 SeekBar
        initSeekBar();
        //特殊的 TextView
        spannableTextView();

    }

    /**
     * TextView 单独字体有点击事件
     */
    private void spannableTextView() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("好友" + i + "，");
        }
        String likeUsers = sb.substring(0, sb.lastIndexOf("，")).toString();
        TextView tvSpan = findViewById(R.id.main_tv_span);
        tvSpan.setMovementMethod(LinkMovementMethod.getInstance());
        tvSpan.setText(addClickPart(likeUsers),
                TextView.BufferType.SPANNABLE);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mainTvNotification = findViewById(R.id.main_tv_notification);
        mainTvCollapsingToolbar = findViewById(R.id.main_tv_collapsing_toolbar);
    }

    /**
     * 点击事件
     */
    private void initClick() {
        //点击跳转到 CollapsingToolbar 界面
        mainTvCollapsingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSecond = new Intent(MainActivity.this, ScrollActivity.class);
                startActivity(toSecond);
            }
        });

        mainTvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationActivity = new Intent(MainActivity.this,
                        NotificationActivity.class);
                startActivity(notificationActivity);
            }
        });
    }

    /**
     * 带进度的 seekBar
     */
    private void initSeekBar() {
        mainSeekBar = findViewById(R.id.main_seek_bar);
        mainTvButton = findViewById(R.id.main_seek_bar_button);

        mainSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度是从-50~50的,但是seekbar.getmin()有限制,所以这里用0~100 -50;
                int text = progress - 50;
                //设置文本显示
                mainTvButton.setText(String.valueOf(text));

                //获取文本宽度
                float textWidth = mainTvButton.getWidth();

                //获取seekbar最左端的x位置
                float left = seekBar.getLeft();

                //进度条的刻度值
                float max = Math.abs(seekBar.getMax());

                //这不叫thumb的宽度,叫seekbar距左边宽度,实验了一下，seekbar 不是顶格的，两头都存在一定空间，所以xml 需要用paddingStart 和 paddingEnd 来确定具体空了多少值,我这里设置15dp;
                float thumb = dp2px(15);

                //每移动1个单位，text应该变化的距离 = (seekBar的宽度 - 两头空的空间) / 总的progress长度
                float average = (((float) seekBar.getWidth()) - 2 * thumb) / max;

                //int to float
                float currentProgress = progress;

                //textview 应该所处的位置 = seekbar最左端 + seekbar左端空的空间 + 当前progress应该加的长度 - textview宽度的一半(保持居中作用)
                float pox = left - textWidth / 2 + thumb + average * currentProgress;
                mainTvButton.setX(pox);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mainTvButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mainTvButton.setVisibility(View.GONE);
            }
        });
    }

    /**
     * LinearLayout 添加内容
     */
    private void addViewToLinearLayout() {
        LinearLayout mainLl = findViewById(R.id.main_ll);
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(i + " ");
            mainLl.addView(textView);
        }

    }

    //定义一个点击每个部分文字的处理方法
    private SpannableStringBuilder addClickPart(String str) {
        //赞的图标，这里没有素材，就找个笑脸代替下~
        ImageSpan imgspan = new ImageSpan(MainActivity.this, R.drawable.ic_launcher);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(imgspan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        //创建一个SpannableStringBuilder对象，连接多个字符串
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str);
        String[] likeUsers = str.split("，");
        if (likeUsers.length > 0) {
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(MainActivity.this, name,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色为蓝色
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);
            }
        }
        return ssb.append("等" + likeUsers.length + "个人觉得很赞");
    }

    /**
     * dp 转 px
     *
     * @param dpValue
     * @return
     */
    public int dp2px(final float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

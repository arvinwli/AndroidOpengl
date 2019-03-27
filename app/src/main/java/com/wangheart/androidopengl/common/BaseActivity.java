package com.wangheart.androidopengl.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wangheart.androidopengl.R;

/**
 * @author arvin
 * @description: 基类
 * @date 2019/3/26
 */
abstract public class BaseActivity extends AppCompatActivity {
    private QMUITopBar qmuiTopBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initTopBar(){
        qmuiTopBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        QMUIAlphaImageButton btnBack= qmuiTopBar.addLeftBackImageButton();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btnBack.setLayoutParams(layoutParams);
        String activityTitle=getIntent().getStringExtra(Constants.REQUEST.KEY_ACTIVITY_TITLE);
        if(!TextUtils.isEmpty(activityTitle)){
            qmuiTopBar.setTitle(activityTitle);
        }else {
            qmuiTopBar.setTitle(getTitle().toString());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        qmuiTopBar.setTitle(title.toString());
    }

    protected void disableTopLeftView(){
        qmuiTopBar.removeAllLeftViews();
    }

    protected QMUITopBar getTopBar(){
        return qmuiTopBar;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(initContentView(layoutResID));
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(initContentView(view), params);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(initContentView(view));
    }

    protected View initContentView(int layoutResID) {
        return initContentView(getLayoutInflater().inflate(layoutResID, null));
    }
    protected View initContentView(View contentView) {
        View mainLayout = View.inflate(this, R.layout.activity_base, null);
        LinearLayout contentLayout = (LinearLayout) mainLayout.findViewById(R.id.ll_container);
        contentLayout.addView(contentView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        qmuiTopBar=mainLayout.findViewById(R.id.qmuiTopBar);
        initTopBar();
        return mainLayout;
    }

    protected BaseActivity getThis(){
        return this;
    }
}

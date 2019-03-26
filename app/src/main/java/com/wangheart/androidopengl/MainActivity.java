package com.wangheart.androidopengl;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        LearnCenter.init();
        findViewById(R.id.btn_learn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearnCenter.launchLearnList(getThis(),LearnCenter.TYPE_ROOT);
            }
        });
    }
}

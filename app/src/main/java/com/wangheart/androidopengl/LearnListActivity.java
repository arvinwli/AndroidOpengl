package com.wangheart.androidopengl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.common.Constants;
import com.wangheart.androidopengl.ui.WebActivity;
import com.wangheart.androidopengl.utils.CollectionsUtils;
import com.wangheart.androidopengl.utils.LogUtils;
import com.wangheart.androidopengl.utils.UIUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author arvin
 * @description:
 * @date 2019/3/26
 */
public class LearnListActivity extends BaseActivity {
    private List<LearnCenter.LearnItem> mListData;
    private LearnCenter.LearnItem mLearnItem;
    private RecyclerView rv;
    private LearnAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        initView();
    }

    private void initView() {
        mLearnItem = (LearnCenter.LearnItem) getIntent().getSerializableExtra(Constants.REQUEST.KEY_LEARN_ITEM);
        if (mLearnItem == null) {
            mListData = LearnCenter.getRootList();
        } else {
            mListData = mLearnItem.getChildItem();
        }
        setTitle(mLearnItem == null ? "全部" : mLearnItem.getName());
        rv = findViewById(R.id.rv);
        mAdapter = new LearnAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getThis()));
        rv.setAdapter(mAdapter);
    }

    protected class LearnAdapter extends RecyclerView.Adapter<LearnItemHolder> {

        @NonNull
        @Override
        public LearnItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new LearnItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_learn, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull LearnItemHolder learnItemHolder, final int i) {
            final boolean isEnable = !TextUtils.isEmpty(mListData.get(i).getActivityName()) ||
                    !TextUtils.isEmpty(mListData.get(i).getUrl()) ||
                    !CollectionsUtils.isEmpty(mListData.get(i).getChildItem());
            learnItemHolder.itemView.setEnabled(isEnable);
            learnItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEnable) {
                        return;
                    }
                    LearnCenter.LearnItem item = mListData.get(i);
                    if (item.getChildItem() != null && item.getChildItem().size() > 0) {
                        LearnCenter.launchLearnList(getThis(), mListData.get(i));
                    } else {
                        switch (item.getType()) {
                            case LearnCenter.TYPE_ACTIVITY:
                                LearnCenter.launchDetail(getThis(), item);
                                break;
                            case LearnCenter.TYPE_WEB:
                                if (TextUtils.isEmpty(item.getUrl())) {
                                    LogUtils.w("item url is empty " + item);
                                } else {
                                    WebActivity.launch(getThis(), item.getUrl(), item.getName());
                                }
                                break;
                        }
                    }
                }
            });
            learnItemHolder.tvName.setText(mListData.get(i).getName());
            learnItemHolder.tvName.setTextColor(isEnable?UIUtils.getColor(R.color.textDefault):UIUtils.getColor(R.color.textGray));
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }
    }


    protected class LearnItemHolder extends ViewHolder {
        TextView tvName;
        FrameLayout flContainer;

        public LearnItemHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            flContainer = itemView.findViewById(R.id.fl_container);
        }
    }
}

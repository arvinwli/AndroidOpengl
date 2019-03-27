package com.wangheart.androidopengl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wangheart.androidopengl.common.BaseActivity;

import java.util.List;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/26
 */
public class LearnListActivity extends BaseActivity {
    private List<LearnCenter.LearnItem> mListData;
    private RecyclerView rv;
    private LearnAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        initView();
    }

    private void initView() {
        mListData = LearnCenter.getSecondPageData(getIntent());
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

            learnItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LearnCenter.LearnItem item=mListData.get(i);
                    if(item.getChildItem()!=null&&item.getChildItem().size()>0) {
                        LearnCenter.launchLearnList(getThis(), mListData.get(i).getType());
                    }else{
                        LearnCenter.launchDetail(getThis(), item);
                    }
                }
            });
            learnItemHolder.tvName.setText(mListData.get(i).getName());
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
            flContainer=itemView.findViewById(R.id.fl_container);
        }
    }
}

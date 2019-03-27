package com.wangheart.androidopengl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.apache.commons.io.IOUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/26
 */
public class LearnCenter {
    private static List<LearnItem> learnItemList;
    private static SparseArray<LearnItem> learnItemIndex;
    public static final int TYPE_ROOT = 0;

    public static interface REQUEST {
        String KEY_TYPE = "key_type";
    }

    public static void init() {
        Observable.just("config.json")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return IOUtils.toString(OpenglApplication.getInstance().getAssets().open(s));
                    }
                }).map(new Function<String, List<LearnItem>>() {
            @Override
            public List<LearnItem> apply(String s) throws Exception {
                Logger.json(s);
                return new Gson().fromJson(s, new TypeToken<List<LearnItem>>() {
                }.getType());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LearnItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<LearnItem> learnCenters) {
                        Logger.d(learnCenters);
                        learnItemList = learnCenters;
                        learnItemIndex = new SparseArray<>();
                        for (LearnItem learnItem : learnItemList) {
                            learnItemIndex.put(learnItem.id, learnItem);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void launchLearnList(Activity activity, int type) {
        Intent intent = new Intent(activity, LearnListActivity.class);
        intent.putExtra(REQUEST.KEY_TYPE, type);
        activity.startActivity(intent);
    }


    public static void launchDetail(Activity activity, LearnItem learnItem) {
        if(learnItem==null|| TextUtils.isEmpty(learnItem.getActivityName())){
            Logger.w("learnItem is invalid "+learnItem);
            return;
        }
        Intent intent=new Intent();
        String className=activity.getPackageName()+".ui."+learnItem.getActivityName();
        Logger.d("className:"+className);
        intent.setComponent(new ComponentName(activity,className));
        activity.startActivity(intent);
    }

    public static List<LearnItem> getSecondPageData(Intent intent) {
        int mainType = intent.getIntExtra(REQUEST.KEY_TYPE, TYPE_ROOT);
        LearnItem learnItem = learnItemIndex.get(mainType);
        if (learnItem == null) {
            return learnItemList;
        } else {
            return learnItem.getChildItem();
        }
    }


    public static class LearnItem {
        private String name;
        private int id;
        private LearnItem parentItem;
        private List<LearnItem> childItem;
        private String activityName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }



        public LearnItem getParentItem() {
            return parentItem;
        }

        public void setParentItem(LearnItem parentItem) {
            this.parentItem = parentItem;
        }

        public List<LearnItem> getChildItem() {
            return childItem;
        }

        public void setChildItem(List<LearnItem> childItem) {
            this.childItem = childItem;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        @Override
        public String toString() {
            return "LearnItem{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", parentItem=" + parentItem +
                    ", childItem=" + childItem +
                    ", activityName='" + activityName + '\'' +
                    '}';
        }
    }


}

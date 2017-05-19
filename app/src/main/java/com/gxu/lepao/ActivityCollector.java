package com.gxu.lepao;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljy on 2017-05-19.
 * 活动管理器
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    //新建活动
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    //结束活动
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    //结束所有活动，退出程序
    public static void finishAll(){
        for(Activity activity : activities){
            activity.finish();
        }
    }

}

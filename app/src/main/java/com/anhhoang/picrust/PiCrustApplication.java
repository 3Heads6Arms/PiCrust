package com.anhhoang.picrust;

import android.app.Application;

import com.anhhoang.picrust.data.DaoMaster;
import com.anhhoang.picrust.data.DaoSession;

/**
 * Created by Anh.Hoang on 10/2/2017.
 */

public class PiCrustApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        daoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this, "picrust.db").getWritableDb()).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

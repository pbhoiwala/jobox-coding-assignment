package com.jobox.coding.assignment;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;


public class CacheController {

    private static CacheController cacheController;

    private Realm realm;

    private CacheController(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    /**
     * Singleton
     * @return
     */
    public static CacheController getInstance(Context  context) {
        if (cacheController == null) {
            return new CacheController(context);
        }
        return cacheController;
    }

    public void cacheNews(final ArrayList<News> newsList) {
        clearAllCache();
        realm.beginTransaction();
        realm.insert(newsList);
        realm.commitTransaction();
    }

    public ArrayList<News> loadCachedNews() {
        RealmResults<News> news = realm.where(News.class).findAll();
        return new ArrayList<>(realm.copyFromRealm(news));

    }

    private void clearAllCache() {
        RealmResults<News> newsList = realm.where(News.class).findAll();
        realm.beginTransaction();
        newsList.deleteAllFromRealm();
        realm.commitTransaction();
    }



}

package com.jobox.coding.assignment.controller;

import android.content.Context;

import com.jobox.coding.assignment.type.News;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class CacheController {

    private static CacheController cacheController;

    private Realm realm;



    public void cacheNews(final ArrayList<News> newsList) {
        clearAllCache();
        realm.beginTransaction();
        realm.insert(newsList);
        realm.commitTransaction();
    }

    public void cacheMoreNews(final ArrayList<News> newsArrayList) {
        realm.beginTransaction();
        realm.insert(newsArrayList);
        realm.commitTransaction();
    }

    public ArrayList<News> loadCachedNews() {
        RealmResults<News> news = realm.where(News.class).findAll();
        return new ArrayList<>(realm.copyFromRealm(news));

    }

    public void clearAllCache() {
        RealmResults<News> newsList = realm.where(News.class).findAll();
        realm.beginTransaction();
        newsList.deleteAllFromRealm();
        realm.commitTransaction();
    }

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
            cacheController = new CacheController(context);
        }
        return cacheController;
    }



}

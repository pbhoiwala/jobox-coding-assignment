package com.jobox.coding.assignment.controller;

import android.content.Context;

import com.jobox.coding.assignment.type.News;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class CacheController {

    private static CacheController cacheController;

    private Realm realm;

    /**
     * Clears old cache and caches new data
     * @param newsList
     */
    public void cacheNews(final ArrayList<News> newsList) {
        clearAllCache();
        realm.beginTransaction();
        realm.insert(newsList);
        realm.commitTransaction();
    }

    /**
     * Caches more news articles (without clearing old cache)
     * @param newsArrayList
     */
    public void cacheMoreNews(final ArrayList<News> newsArrayList) {
        realm.beginTransaction();
        realm.insert(newsArrayList);
        realm.commitTransaction();
    }

    /**
     * Loads all cached news
     * @return
     */
    public ArrayList<News> loadCachedNews() {
        RealmResults<News> news = realm.where(News.class).findAll();
        return new ArrayList<>(realm.copyFromRealm(news));
    }

    /**
     * Removes old cached data from database
     */
    private void clearAllCache() {
        RealmResults<News> newsList = realm.where(News.class).findAll();
        realm.beginTransaction();
        newsList.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * Private cacheController constructor that initializes
     * Realm instance
     * @param context
     */
    private CacheController(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    /**
     * Singleton method to get the static instance of this class
     * that is used instead of the constructor
     */
    public static CacheController getInstance(Context  context) {
        if (cacheController == null) {
            cacheController = new CacheController(context);
        }
        return cacheController;
    }



}

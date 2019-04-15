package com.jobox.coding.assignment.async;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.jobox.coding.assignment.callback.OnCompressBitmapCallback;
import com.jobox.coding.assignment.callback.OnNewsFetchCallback;
import com.jobox.coding.assignment.type.News;
import com.jobox.coding.assignment.util.NewsFetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AsyncAction {

    public static class FetchNews extends AsyncTask<Object, Object[], Object[]> {

        @Override
        protected Object[] doInBackground(Object... params) {
            Context context = (Context) params[0];
            String query = (String) params[1];

            NewsFetcher newsFetcher = new NewsFetcher(context);
            params[2] = newsFetcher.fetchNews(query);

            return params;
        }

        @Override
        protected void onPostExecute(Object[] params) {
            ArrayList<News> newsArrayList = new ArrayList<>((ArrayList) params[2]);
            OnNewsFetchCallback callback = (OnNewsFetchCallback) params[3];
            callback.onSuccess(newsArrayList);
        }
    }

    public static class CompressBitmap extends AsyncTask<Object, Object[], Object[]> {

        @Override
        protected Object[] doInBackground(Object... params) {
            Context context = (Context) params[0];
            Bitmap resource = (Bitmap) params[1];

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resource.compress(Bitmap.CompressFormat.JPEG, 20, out);
            params[1] = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            return params;
        }

        @Override
        protected void onPostExecute(Object[] params) {
            Bitmap decode = (Bitmap) params[1];
            OnCompressBitmapCallback callback = (OnCompressBitmapCallback) params[2];
            callback.onSuccess(decode);
        }
    }
}

package br.com.allin.mobile.pushnotification.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lucasrodrigues on 28/11/16.
 */

public class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
    private final String imageUrl;
    private final OnDownloadCompleted onDownloadCompleted;

    public DownloadImage(String imageUrl, OnDownloadCompleted onDownloadCompleted) {
        this.imageUrl = imageUrl;
        this.onDownloadCompleted = onDownloadCompleted;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL url = new URL(this.imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();

            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap == null) {
            this.onDownloadCompleted.onError();
        } else {
            this.onDownloadCompleted.onCompleted(bitmap);
        }
    }

    public interface OnDownloadCompleted {
        void onCompleted(Bitmap bitmap);

        void onError();
    }
}

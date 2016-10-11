package br.com.allin.mobile.pushnotification.service;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.entity.CacheEntity;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.http.HttpManager;

/**
 * Service class for cache
 */
public class CacheService {
    private CacheDAO cacheDAO;
    private Context context;

    public CacheService(Context context) {
        this.cacheDAO = new CacheDAO(context);
        this.context = context;
    }

    public void sync() {
        List<CacheEntity> cacheList = cacheDAO.getAll();

        if (cacheList != null) {
            for (CacheEntity cacheEntity : cacheList) {
                sync(cacheEntity);
            }
        }
    }

    private void sync(final CacheEntity cacheEntity) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                try {
                    return HttpManager.makeRequestURL(context, cacheEntity.getUrl(),
                            RequestType.POST, new JSONObject(cacheEntity.getJson()), false);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseEntity) {
                    cacheDAO.delete(cacheEntity.getId());
                }
            }
        }.execute();
    }

    public void insert(String url, String json) {
        this.cacheDAO.insert(url, json);
    }
}

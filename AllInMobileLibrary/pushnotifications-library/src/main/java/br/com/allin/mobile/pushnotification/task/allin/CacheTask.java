package br.com.allin.mobile.pushnotification.task.allin;

import android.os.AsyncTask;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.entity.allin.CacheEntity;
import br.com.allin.mobile.pushnotification.entity.allin.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.http.HttpManager;

/**
 * Created by lucasrodrigues on 18/01/18.
 */

public class CacheTask extends AsyncTask<Void, Void, Object> {
    private CacheEntity cache;
    private CacheDAO cacheDAO;

    public CacheTask(CacheEntity cache, CacheDAO cacheDAO) {
        this.cache = cache;
        this.cacheDAO = cacheDAO;
    }

    protected Object doInBackground(Void... params) {
        try {
            return HttpManager.makeRequestURL(cache.getUrl(),
                    RequestType.POST, new JSONObject(cache.getJson()), false);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);

        if (object instanceof ResponseEntity) {
            cacheDAO.delete(cache.getId());
        }
    }
}

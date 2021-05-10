package br.com.allin.mobile.pushnotification.task.allin;

import android.os.AsyncTask;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.http.RequestType;

/**
 * Created by lucasrodrigues on 18/01/18.
 */

public class CacheTask extends AsyncTask<Void, Void, Object> {
    private final AICache cache;
    private final CacheDAO cacheDAO;

    public CacheTask(AICache cache, CacheDAO cacheDAO) {
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

        if (object instanceof AIResponse) {
            cacheDAO.deleteById(cache.getId());
        }
    }
}

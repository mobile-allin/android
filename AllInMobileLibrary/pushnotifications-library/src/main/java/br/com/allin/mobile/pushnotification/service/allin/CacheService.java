package br.com.allin.mobile.pushnotification.service.allin;

import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.task.allin.CacheTask;

/**
 * Service class for cache
 */
public class CacheService {
    private CacheDAO cacheDAO;

    public CacheService() {
        this.cacheDAO = AlliNDatabase.get().cacheTable();
    }

    void sync() {
        List<AICache> cacheList = cacheDAO.get();

        if (cacheList != null) {
            for (AICache cache : cacheList) {
                sync(cache);
            }
        }
    }

    private void sync(final AICache cache) {
        new CacheTask(cache, cacheDAO).execute();
    }

    public void insert(String url, String json) {
        cacheDAO.insert(new AICache(url, json));
    }
}

package br.com.allin.mobile.pushnotification.service.allin;

import java.util.List;

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

    public void sync() {
        List<AICache> cacheList = this.cacheDAO.get();

        if (cacheList != null) {
            for (AICache cache : cacheList) {
                this.sync(cache);
            }
        }
    }

    private void sync(AICache cache) {
        new CacheTask(cache, this.cacheDAO).execute();
    }

    public void insert(String url, String json) {
        this.cacheDAO.insert(new AICache(url, json));
    }
}

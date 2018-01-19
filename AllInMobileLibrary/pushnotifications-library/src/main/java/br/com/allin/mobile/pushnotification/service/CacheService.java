package br.com.allin.mobile.pushnotification.service;

import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.entity.CacheEntity;
import br.com.allin.mobile.pushnotification.task.CacheTask;

/**
 * Service class for cache
 */
public class CacheService {
    private CacheDAO cacheDAO;

    public CacheService() {
        this.cacheDAO = new CacheDAO(AlliNPush.getInstance().getContext());
    }

    void sync() {
        List<CacheEntity> cacheList = cacheDAO.getAll();

        if (cacheList != null) {
            for (CacheEntity cacheEntity : cacheList) {
                sync(cacheEntity);
            }
        }
    }

    private void sync(final CacheEntity cache) {
        new CacheTask(cache, cacheDAO).execute();
    }

    public void insert(String url, String json) {
        cacheDAO.insert(url, json);
    }
}

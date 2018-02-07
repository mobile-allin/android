package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.SearchEntity;
import br.com.allin.mobile.pushnotification.entity.btg.TransactionEntity;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class SearchService extends TrackingBaseService<SearchEntity> {
    public SearchService(String account, List<SearchEntity> searchs) {
        super(account, "search", searchs);
    }

    @Override
    JSONArray transform(List<SearchEntity> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (SearchEntity search : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("keyword", search.getKeyword());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}

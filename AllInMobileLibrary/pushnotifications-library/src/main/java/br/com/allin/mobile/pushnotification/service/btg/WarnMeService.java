package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.WarnMeEntity;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class WarnMeService extends TrackingBaseService<WarnMeEntity> {
    public WarnMeService(String account, List<WarnMeEntity> warns) {
        super(account, "warnme", warns);
    }

    @Override
    JSONArray transform(List<WarnMeEntity> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (WarnMeEntity warn : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("productId", warn.getProductId());
                productJSONObject.put("active", warn.isActive());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}

package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.AIClient;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class ClientService extends TrackingBaseService<AIClient> {
    public ClientService(String account, List<AIClient> carts) {
        super(account, "client", carts);
    }

    @Override
    JSONArray transform(List<AIClient> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (AIClient client : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("email", client.getEmail());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}

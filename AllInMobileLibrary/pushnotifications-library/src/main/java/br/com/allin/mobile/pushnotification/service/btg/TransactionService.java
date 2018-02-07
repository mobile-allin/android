package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.TransactionEntity;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class TransactionService extends TrackingBaseService<TransactionEntity> {
    public TransactionService(String account, List<TransactionEntity> transactions) {
        super(account, "transaction", transactions);
    }

    @Override
    JSONArray transform(List<TransactionEntity> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (TransactionEntity transaction : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("productId", transaction.getProductId());
                productJSONObject.put("transactionId", transaction.getTransactionId());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}

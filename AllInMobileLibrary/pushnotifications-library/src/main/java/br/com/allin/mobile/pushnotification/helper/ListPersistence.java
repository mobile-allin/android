package br.com.allin.mobile.pushnotification.helper;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.allin.AIValues;

public class ListPersistence {
    public static String getMD5(String nameList, List<AIValues> columnsAndValues) {
        String json = columnsAndValues.toString();
        String complete = "List: " + nameList + " Json: " + json;

        return Util.md5(complete);
    }
}

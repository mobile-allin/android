package br.com.allin.mobile.pushnotification.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.allin.mobile.pushnotification.entity.allin.AIValues;

public class ListPersistence {
    public static String getMD5(String nameList, List<AIValues> columnsAndValues) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String json = columnsAndValues.toString();
        String complete = "List: " + nameList + " Json: " + json + " Date: " + date;

        return Util.md5(complete);
    }
}

package Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utils {
    public static ArrayList<ArrayList<String>> getKeysValuesFromMap(HashMap<String, String> map) {
        ArrayList keysValuesList = new ArrayList <String>(2);
        Set<String> keySet = map.keySet();

        // Creating an ArrayList of keys
        // by passing the keySet
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);

        // Getting Collection of values from HashMap
        Collection<String> values = map.values();

        // Creating an ArrayList of values
        ArrayList<String> listOfValues = new ArrayList<>(values);

        // Adding key, value list to new array list
        keysValuesList.add(listOfKeys);
        keysValuesList.add(listOfValues);

        return keysValuesList;
    }
    public static long getDiffBetweenDates(Date date1, Date date2) {
        long diff;
        LocalDate localDate1 = new Date(date1.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = new Date(date2.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        diff = ChronoUnit.DAYS.between(localDate1, localDate2);
        return diff;
    }
    public static String getDateStringWithFormat(String pattern, Date date) {
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);
        return dateAsString;
    }
    public static HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}

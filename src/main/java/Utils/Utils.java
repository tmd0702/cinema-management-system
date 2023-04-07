package Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

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

}

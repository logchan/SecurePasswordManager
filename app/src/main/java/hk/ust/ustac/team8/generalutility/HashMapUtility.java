package hk.ust.ustac.team8.generalutility;

import java.util.HashMap;

/**
 * Some static methods that are related to Java HashMap
 */
public class HashMapUtility {

    /**
     * A method that performs the following tasks:
     * 1. if key is null or map is null, do nothing
     * 2. if value is non-null, add or update
     * 3. otherwise, remove key/value pair from the map if exists
     *
     * @param map the map to perform on
     * @param key the key
     * @param value the value
     * @param <K> the type of key
     * @param <V> the type of value
     */
    public static <K, V> void conditionalNonNullUpdate(HashMap<K, V> map, K key, V value) {
        if (key == null || map == null) {
            return;
        }

        if (value != null) {
            map.put(key, value);
        }
        else {
            if (map.containsKey(key)) {
                map.remove(key);
            }
        }
    }
}

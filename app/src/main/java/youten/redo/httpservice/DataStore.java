package youten.redo.httpservice;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DataStore {
    public static final String KEY_DEFAULT = "default";
    private static DataStore mDataStore = new DataStore();

    private Map<String, String> mMap = new HashMap<>();

    public static String get(String key) {
        if (StringUtils.isBlank(key)) {
            return getJson(); // whole
        }
        return mDataStore.mMap.get(key);
    }

    public static String set(String key, String value) {
        if ((key == null) || (value == null)) {
            throw new IllegalArgumentException("key or value is null");
        }
        return mDataStore.mMap.put(key, value);
    }

    public static Map<String, String> getMap() {
        return mDataStore.mMap;
    }

    public static String getJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, String> entry : mDataStore.mMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ((key != null) && (value != null)) {
                sb.append("\"").append(key).append("\":");
                sb.append(value).append(",");
            }
        }
        sb.append("\"_now\":\"").append(getNowISO8601()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private static String getNowISO8601() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new java.util.Date());
    }

    /**
     * Constructor
     */
    private DataStore() {
        mMap.put(KEY_DEFAULT, "{\"_dummy\":\"123\"}");
        // singleton
    }
}

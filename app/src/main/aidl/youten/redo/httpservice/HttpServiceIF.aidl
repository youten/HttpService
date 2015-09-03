package youten.redo.httpservice;

import youten.redo.httpservice.HttpServiceListener;

interface HttpServiceIF {
    oneway void registerListener(in HttpServiceListener listener);
    oneway void unregisterListener(in HttpServiceListener listener);
    String getValue(in String key);
    oneway void setValue(in String key, in String value);
}

package youten.redo.httpservice.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * HttpService Event
 */
public class HEvent implements Parcelable {

    /** Event Method */
    private String mMethod;
    /** target Key Name */
    private String mKey;
    /** target Value JSON String */
    private String mValueJson;

    /**
     * Constructor
     *
     * @param method    Event Method
     * @param key       key
     * @param valueJson value JSON String
     */
    public HEvent(String method, String key, String valueJson) {
        mMethod = method;
        mKey = key;
        mValueJson = valueJson;
    }

    public HEvent(Parcel in) {
        mMethod = in.readString();
        mKey = in.readString();
        mValueJson = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMethod);
        dest.writeString(mKey);
        dest.writeString(mValueJson);
    }

    public static final Parcelable.Creator<HEvent> CREATOR = new Parcelable.Creator<HEvent>() {
        public HEvent createFromParcel(Parcel in) {
            return new HEvent(in);
        }

        public HEvent[] newArray(int size) {
            return new HEvent[size];
        }
    };

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getValueJson() {
        return mValueJson;
    }

    public void setValueJson(String valueJson) {
        mValueJson = valueJson;
    }
}


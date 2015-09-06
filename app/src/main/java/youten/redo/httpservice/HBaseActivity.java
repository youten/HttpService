package youten.redo.httpservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import youten.redo.httpservice.event.HEvent;

public abstract class HBaseActivity extends AppCompatActivity {
    private static final String TAG = HBaseActivity.class.getSimpleName();
    private static final ComponentName BSERVICE_COMPONENT_NAME = new ComponentName(HttpService.class
            .getPackage().getName(), HttpService.class.getName());

    /** HttpService IF instance */
    private HttpServiceIF mHttpServiceIF;

    /** Service Conn (for Background Service) */
    private final ServiceConnection mBServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected(name=" + name);

            if (BSERVICE_COMPONENT_NAME.equals(name)) {
                mHttpServiceIF = HttpServiceIF.Stub.asInterface(service);
                registerBServiceListener();
                onHttpServiceConnected();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    /** Background Service Event Listener */
    private final HttpServiceListener mHttpServiceListener = new HttpServiceListener.Stub() {
        @Override
        public boolean onEvent(HEvent event) {
            return onHttpServiceEvent(event);
        }
    };

    /** bind and register to BService */
    private void registerBServiceListener() {
        if (mHttpServiceIF == null) {
            Intent intent = new Intent(HttpServiceIF.class.getName());
            intent.setPackage(HttpServiceIF.class.getPackage().getName());
            bindService(intent, mBServiceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            try {
                mHttpServiceIF.registerListener(mHttpServiceListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /** unregister and unbind to BService */
    private void unregisterBServiceListener() {
        if (mHttpServiceIF != null) {
            try {
                mHttpServiceIF.unregisterListener(mHttpServiceListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                unbindService(mBServiceConnection);
                mHttpServiceIF = null;
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }
    }

    /**
     * BService初回接続時
     */
    abstract protected void onHttpServiceConnected();

    /**
     * HttpServiceからのイベントコールバック
     *
     * @param event
     */
    protected boolean onHttpServiceEvent(HEvent event) {
        return false;
    }

    /**
     * HttpService DataStoreからのgetValue
     *
     * @param key
     * @return
     */
    protected String getDataStoreValue(String key) {
        if (mHttpServiceIF == null) {
            Log.d(TAG, "getDataStoreValue(mHttpServiceIF=null)");
            return null;
        }

        String json = null;
        try {
            json = mHttpServiceIF.getValue(key);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * HttpService DataStoreへのsetValue
     *
     * @param key
     * @param value
     * @return
     */
    protected void setDataStoreValue(String key, String value) {
        if (mHttpServiceIF == null) {
            Log.d(TAG, "setDataStoreValue(mHttpServiceIF=null)");
            return;
        }
        try {
            mHttpServiceIF.setValue(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBServiceListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBServiceListener();
    }
}

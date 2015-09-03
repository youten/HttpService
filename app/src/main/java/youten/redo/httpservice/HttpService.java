package youten.redo.httpservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import youten.redo.httpservice.event.HEvent;
import youten.redo.httpservice.server.JServer;

/**
 * AIDL Client -[HttpService]- Browser間でのデータ送信・同期を行う。
 * オンメモリKVS（ただのメンバ変数、ValueはJSON Stringのみを想定）を生成し、GETで取得、POSTで更新する。
 */
public class HttpService extends Service {
    public static final String TAG = HttpService.class.getSimpleName();

    private static final String PREFIX_ACTION = HttpService.class.getPackage().getName() + ".action";
    private static final String ACTION_START = PREFIX_ACTION + ".START";
    private static final String ACTION_STOP = PREFIX_ACTION + ".STOP";

    /** callback list */
    private RemoteCallbackList<HttpServiceListener> callbackList = new RemoteCallbackList<>();

    private JServer mJServer;
    private static final int port = 8888;

    private final HttpServiceIF.Stub stub = new HttpServiceIF.Stub() {
        @Override
        public void registerListener(HttpServiceListener listener) throws RemoteException {
            synchronized (callbackList) {
                Log.d(TAG, "registerListener");
                callbackList.register(listener);
            }
        }

        @Override
        public void unregisterListener(HttpServiceListener listener) throws RemoteException {
            synchronized (callbackList) {
                Log.d(TAG, "unregisterListener");
                callbackList.unregister(listener);
            }
        }

        @Override
        public void setValue(String key, String value) throws RemoteException {
            DataStore.set(key, value);
        }

        @Override
        public String getValue(String key) throws RemoteException {
            return DataStore.get(key);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mJServer = new JServer(port);
        mJServer.start(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mJServer.stop();
        mJServer = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {
            if (HttpServiceIF.class.getName().equals(intent.getAction())) {
                return stub;
            }
        }

        return null;
    }

    private void handleCommand(Intent intent) {
        if (intent == null) {
            Log.d(TAG, "intent==null");
            return;
        }

        String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            Log.d(TAG, "ACTION_START");

        } else if (ACTION_STOP.equals(action)) {
            Log.d(TAG, "ACTION_STOP");

        } else {
            Log.d(TAG, "Unknown Action");
        }
    }

    /**
     * HttpService EventをListenerへcallbackするThread.<br>
     * new CallbackThread(new HEvent(method, key, valueJson)).start();の様にして使う。
     */
    public class CallbackThread extends Thread {
        private HEvent mEvent;

        public CallbackThread(HEvent event) {
            mEvent = event;
        }

        @Override
        public void run() {
            synchronized (callbackList) {
                int listeners = callbackList.beginBroadcast();
                for (int i = 0; i < listeners; i++) {
                    try {
                        if (callbackList.getBroadcastItem(i).onEvent(mEvent)) {
                            // consumed
                        } else {
                            // not consumed
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                callbackList.finishBroadcast();
            }
        }
    }
}

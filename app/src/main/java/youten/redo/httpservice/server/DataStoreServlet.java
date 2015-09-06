package youten.redo.httpservice.server;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import youten.redo.httpservice.DataStore;
import youten.redo.httpservice.HttpEventListener;
import youten.redo.httpservice.event.HEvent;
import youten.redo.httpservice.http.Charset;
import youten.redo.httpservice.http.ContentType;

public class DataStoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = DataStoreServlet.class.getSimpleName();
    private static final String KEY_KEY = "key";
    private HttpEventListener mListener;

    public DataStoreServlet(HttpEventListener listener) {
        mListener = listener;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter(KEY_KEY);
        Log.d(TAG, "doGet key=" + key);

        String json = DataStore.get(key);
        Log.d(TAG, " json=" + json);
        ServletUtil.responseJSON(resp, json);

        if (mListener != null) {
            mListener.onEvent(new HEvent("GET", key, json));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter(KEY_KEY);
        Log.d(TAG, "doPost key=" + key);
        String json = null;
        req.setCharacterEncoding(Charset.UTF_8);
        resp.setStatus(HttpServletResponse.SC_OK);

        if (StringUtils.isBlank(key) || !req.getContentType().startsWith(ContentType.APPLICATION_JSON)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            json = IOUtils.toString(req.getReader());
        }
        Log.d(TAG, " json=" + json);

        if (StringUtils.isBlank(json)) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } else {
            DataStore.set(key, json);
        }

        json = DataStore.get(key);
        Log.d(TAG, " json=" + json);
        ServletUtil.responseJSON(resp, json);

        if (mListener != null) {
            mListener.onEvent(new HEvent("POST", key, json));
        }
    }

}

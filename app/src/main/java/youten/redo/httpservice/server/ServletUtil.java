package youten.redo.httpservice.server;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import youten.redo.httpservice.http.Charset;
import youten.redo.httpservice.http.ContentType;

public class ServletUtil {
    public static final String LS = System.getProperty("line.separator");

    /**
     * 単純なHTMLを返す。
     *
     * @param resp
     * @param title
     * @param message
     * @throws IOException
     */
    public static void responseHtml(HttpServletResponse resp, String title, String message) throws IOException {
        resp.setCharacterEncoding(Charset.UTF_8);
        resp.setContentType(ContentType.TEXT_HTML);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append(LS);
        sb.append("<head><title>").append(title).append("</title></head>").append(LS);
        sb.append("<body><h1>").append(title).append("</h1>").append(LS);
        sb.append("<p>").append(message).append("</p>").append(LS);
        sb.append("</body></html>").append(LS);

        resp.getWriter().println(sb.toString());
    }

    /**
     * JSON文字列を返す
     *
     * @param resp
     * @param json
     * @throws IOException
     */
    public static void responseJSON(HttpServletResponse resp, String json) throws IOException {
        if ((resp == null) || (json == null)) {
            throw new IllegalArgumentException();
        }

        resp.setCharacterEncoding(Charset.UTF_8);
        resp.setContentType(ContentType.APPLICATION_JSON);

        resp.getWriter().println(json);
    }


    private ServletUtil() {
        // Util
    }
}
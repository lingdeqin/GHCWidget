package by.aleks.ghcwidget.api;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import by.aleks.ghcwidget.R;

public class GitHubHelper {

    private static final int HTTP_STATUS_OK = 200;
    private static byte[] buff = new byte[1024];
    private static final String logTag = "GHCWidget";

    public static class ApiException extends Exception {
        private static final long serialVersionUID = 1L;

        public ApiException(String msg) {
            super(msg);
        }

        public ApiException(String msg, Throwable thr) {
            super(msg, thr);
        }
    }

    /**
     * download user contribution data.
     *
     * @param username GitHub username
     * @return Array of html strings returned by the API.
     * @throws ApiException
     */
    protected static synchronized String downloadFromServer(String username, Context context)
            throws ApiException {
        String retval = null;
        String urlstr = "https://github.com/users/" + username + "/contributions";
        Log.d(logTag, "Fetching " + urlstr);
        try{
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                String line;

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                retval = sb.toString();
            }else{
                retval = "invalid_response";
            }
        }catch (IOException e){
            retval = "invalid_response";
        }
        return retval;
    }

}

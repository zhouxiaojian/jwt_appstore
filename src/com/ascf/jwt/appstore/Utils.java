package com.ascf.jwt.appstore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class Utils {

    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final String LOG_TAG = "Utils";
    
    private Utils(){
        
    }

    public static InputStream getInputStreamFromServer(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        HttpGet getRequest = null;
        getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                return entity.getContent();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG + "getInputStreamFromServer", " fail.");
        }
        return null;
    }

    public static byte[] downloadFromServer2Buffer(String url){
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        HttpGet getRequest = null;
        ByteArrayOutputStream dataStream = null;
        try {
            getRequest = new HttpGet(url);

            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = entity.getContent();
                    dataStream = new ByteArrayOutputStream();
                    outputStream = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                    copy(inputStream, outputStream);
                    outputStream.flush();
                    return dataStream.toByteArray();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            if (getRequest != null)getRequest.abort();
            Log.w(LOG_TAG + "downloadFromServer2Buffer", "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            if (getRequest != null)getRequest.abort();
            Log.w(LOG_TAG + "downloadFromServer2Buffer", "Incorrect URL: " + url);
        } catch (Exception e) {
            if (getRequest != null) getRequest.abort();
            Log.w(LOG_TAG + "downloadFromServer2Buffer", "Error while retrieving bitmap from " + url, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        if (dataStream != null){
            return dataStream.toByteArray();
        }
        return null;
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}

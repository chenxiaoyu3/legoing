package com.Legoing.webimage;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


public interface HttpWrapper {
	public InputStream getInputStream(String url);
}

class DefaultHttpWrapper implements HttpWrapper{
	private static final int TIME_OUT = 30000;
	public InputStream getInputStream(String url) {
		try {
			return getStreamFromNetwork(url, TIME_OUT);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static InputStream getStreamFromNetwork(String url,
            int mTimeoutMilliseconds) throws ClientProtocolException,
            IOException {
        return getStreamFromNetwork(url, "", mTimeoutMilliseconds);
    }

    public static InputStream getStreamFromNetwork(String url,
            String mUserAgent, int mTimeoutMilliseconds)
            throws ClientProtocolException, IOException {
        HttpGet httpget = new HttpGet(url.toString());
        httpget.addHeader("User-Agent", mUserAgent);

        HttpClient httpclient = getDefaultHttpClient(mTimeoutMilliseconds);
        HttpResponse mResponse = httpclient.execute(httpget);
        HttpEntity entity = mResponse.getEntity();
        if (mResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK
                || entity == null || entity.getContentLength() == 0) {
            return null;
        }
        InputStream is = entity.getContent();
        return is;
    }
    public static DefaultHttpClient getDefaultHttpClient(
            int mTimeoutMilliseconds) {
        HttpParams httpParameters = new BasicHttpParams();
        if (mTimeoutMilliseconds > 0) {
            // Set timeouts to wait for connection establishment / receiving
            // data.
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    mTimeoutMilliseconds);
            HttpConnectionParams.setSoTimeout(httpParameters,
                    mTimeoutMilliseconds);
        }
        // Set the buffer size to avoid OutOfMemoryError exceptions on certain
        // HTC devices.
        // http://stackoverflow.com/questions/5358014/android-httpclient-oom-on-4g-lte-htc-thunderbolt
        HttpConnectionParams.setSocketBufferSize(httpParameters, 8192);
        return new DefaultHttpClient(httpParameters);
    }
}
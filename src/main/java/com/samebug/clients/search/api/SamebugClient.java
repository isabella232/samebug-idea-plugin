/**
 * Copyright 2016 Samebug, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.search.api;

import com.google.gson.*;
import com.intellij.util.net.IdeHttpClientHelpers;
import com.samebug.clients.search.api.entities.GroupedHistory;
import com.samebug.clients.search.api.entities.SearchResults;
import com.samebug.clients.search.api.entities.UserInfo;
import com.samebug.clients.search.api.entities.legacy.Solutions;
import com.samebug.clients.search.api.entities.tracking.TrackEvent;
import com.samebug.clients.search.api.exceptions.*;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.http.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.RequestAbortedException;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class SamebugClient {
    private final String apiKey;
    final static String USER_AGENT = "Samebug-Idea-Client/1.3.0";
    final static String API_VERSION = "0.8";
    //    public final static URI root = URI.create("http://localhost:9000/");
//    public final static URI root = URI.create("http://nightly.samebug.com/");
    public final static URI root = URI.create("https://samebug.io/");
    //        final static URI trackingGateway = URI.create("http://nightly.samebug.com/").resolve("track/trace/");
    final static URI trackingGateway = URI.create("https://samebug.io/").resolve("track/trace");
    final static URI gateway = root.resolve("rest/").resolve(API_VERSION + "/");

    final static Gson gson;
    final static HttpClient httpClient;
    final static RequestConfig requestConfig;
    final static RequestConfig trackingConfig;


    static {
        // Build gson
        // TODO is this a fine way of serialization of Date?
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                }
        );
        gson = gsonBuilder.create();

        // Build http client
        HttpClientBuilder httpBuilder = HttpClientBuilder.create();
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        CredentialsProvider provider = new BasicCredentialsProvider();

        requestConfigBuilder.setConnectTimeout(3000).setSocketTimeout(7000).setConnectionRequestTimeout(500);
        IdeHttpClientHelpers.ApacheHttpClient4.setProxyForUrlIfEnabled(requestConfigBuilder, root.toString());
        IdeHttpClientHelpers.ApacheHttpClient4.setProxyCredentialsForUrlIfEnabled(provider, root.toString());

        requestConfig = requestConfigBuilder.build();
        trackingConfig = requestConfigBuilder.setSocketTimeout(3000).build();
        httpClient = httpBuilder.setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(20).setMaxConnPerRoute(20)
                .setDefaultCredentialsProvider(provider)
                .setRetryHandler(new AggresiveRetryHandler())
                .setDefaultHeaders(Arrays.asList(new BasicHeader("User-Agent", USER_AGENT)))
                .build();

    }

    public SamebugClient(final String apiKey) {
        this.apiKey = apiKey;
    }

    public static URL getSearchUrl(int searchId) {
        String uri = "search/" + searchId;
        try {
            return root.resolve(uri).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalUriException("Unable to resolve uri " + uri, e);
        }
    }

    public static URL getUserProfileUrl(Integer userId) {
        String uri = "user/" + userId;
        try {
            return root.resolve(uri).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalUriException("Unable to resolve uri " + uri, e);
        }
    }

    public SearchResults searchSolutions(String stacktrace) throws SamebugClientException {
        URL url = getApiUrl("search");
        HttpPost post = new HttpPost(url.toString());
        post.setEntity(new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair("exception", stacktrace)), Consts.UTF_8));
        return requestJson(post, SearchResults.class);
    }

    public UserInfo getUserInfo(String apiKey) throws SamebugClientException {
        String url;
        try {
            url = getApiUrl("checkApiKey").toString() + "?apiKey=" + URLEncoder.encode(apiKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnableToPrepareUrl("Unable to resolve uri with apiKey " + apiKey, e);
        }
        HttpGet request = new HttpGet(url);

        return requestJson(request, UserInfo.class);
    }

    public GroupedHistory getSearchHistory() throws SamebugClientException {
        URL url = getApiUrl("history");
        HttpGet request = new HttpGet(url.toString());

        return requestJson(request, GroupedHistory.class);
    }

    public Solutions getSolutions(int searchId) throws SamebugClientException {
        URL url = getApiUrl("search/" + searchId);
        HttpGet request = new HttpGet(url.toString());

        return requestJson(request, Solutions.class);
    }

    public void trace(TrackEvent event) throws SamebugClientException {
        HttpPost post = new HttpPost(trackingGateway);
        postJson(post, event.fields);
    }

    // implementation
    private <T> T requestJson(HttpRequestBase request, final Class<T> classOfT)
            throws SamebugTimeout, UnsuccessfulResponseStatus, RemoteError, UserUnauthenticated, UserUnauthorized, HttpError {
        request.setHeader("Accept", "application/json");
        final HttpResponse httpResponse = executePatient(request);
        return new HandleResponse<T>(httpResponse) {
            @Override
            T process(Reader reader) {
                return gson.fromJson(reader, classOfT);
            }
        }.handle();
    }

    private void postJson(HttpPost post, Object data)
            throws SamebugTimeout, UnsuccessfulResponseStatus, RemoteError, UserUnauthenticated, UserUnauthorized, HttpError {
        String json = gson.toJson(data);
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        HttpResponse httpResponse = executeFailFast(post);

        new HandleResponse<Void>(httpResponse) {
            @Override
            Void process(Reader reader) {
                return null;
            }
        }.handle();
    }


    private HttpResponse executeFailFast(HttpRequestBase request)
            throws SamebugTimeout, UnsuccessfulResponseStatus, RemoteError, UserUnauthenticated, UserUnauthorized, HttpError {
        return execute(request, trackingConfig);
    }

    private HttpResponse executePatient(HttpRequestBase request)
            throws SamebugTimeout, UnsuccessfulResponseStatus, RemoteError, UserUnauthenticated, UserUnauthorized, HttpError {
        return execute(request, null);
    }


    /**
     * @param request              the http request
     * @return the http response
     * @throws SamebugTimeout             if the server exceeded the timeout during connection or execute
     * @throws HttpError                  in case of a problem or the connection was aborted or   if the response is not readable
     * @throws UnsuccessfulResponseStatus if the response status is not 200
     * @throws RemoteError                if the server returned error in the X-Samebug-Errors header
     * @throws UserUnauthenticated        if the user was not authenticated (401)
     * @throws UserUnauthorized           if the user was not authorized (403)
     */
    private HttpResponse execute(HttpRequestBase request, @Nullable RequestConfig config)
            throws SamebugTimeout, UnsuccessfulResponseStatus, RemoteError, UserUnauthenticated, UserUnauthorized, HttpError {
        addDefaultHeaders(request);
        if (config != null) request.setConfig(config);

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(request);
        } catch (IOException e) {
            throw new HttpError(e);
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();

        switch (statusCode) {
            case HttpStatus.SC_OK:
                final Header errors = httpResponse.getFirstHeader("X-Samebug-Errors");
                if (errors != null) {
                    throw new RemoteError(errors.getValue());
                }
                return httpResponse;
            case HttpStatus.SC_UNAUTHORIZED:
                throw new UserUnauthenticated();
            case HttpStatus.SC_FORBIDDEN:
                throw new UserUnauthorized(httpResponse.getStatusLine().getReasonPhrase());
            default:
                throw new UnsuccessfulResponseStatus(statusCode);
        }
    }

    private URL getApiUrl(String uri) throws SamebugClientError {
        URL url;
        try {
            url = gateway.resolve(uri).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalUriException("Unable to resolve uri " + uri, e);
        }
        return url;
    }

    private void addDefaultHeaders(HttpRequest request) {
        if (apiKey != null) request.addHeader("X-Samebug-ApiKey", apiKey);
    }
}


abstract class HandleResponse<T> {
    final HttpResponse response;

    HandleResponse(HttpResponse response) {
        this.response = response;
    }

    abstract T process(Reader reader);

    public T handle() throws HttpError {
        InputStream content = null;
        Reader reader = null;
        try {
            content = response.getEntity().getContent();
            reader = new InputStreamReader(content);
            return process(reader);
        } catch (IOException e) {
            throw new HttpError(e);
        } finally {
            try {
                if (content != null) content.close();
                if (reader != null) reader.close();
            } catch (IOException ignored) {
            }
        }
    }
}

class AggresiveRetryHandler extends DefaultHttpRequestRetryHandler {
    public AggresiveRetryHandler() {
        super(3, false, Arrays.asList(ConnectTimeoutException.class, SocketTimeoutException.class, RequestAbortedException.class, UnknownHostException.class, ConnectException.class, SSLException.class));
    }
}

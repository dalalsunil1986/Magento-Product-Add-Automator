package mariell;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpClientHelper {

    private static HttpClientHelper instance;

    private HttpClient httpClient;
    private HttpClientContext context;

    public static HttpClientHelper getHelper() {
        if (instance == null) {
            instance = new HttpClientHelper();
            try {
                instance.initializeHttpClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    private HttpClientHelper() {
    }

    private void initializeHttpClient() throws IOException {
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .build();
        CookieStore cookieStore = new BasicCookieStore();
        context = HttpClientContext.create();
        context.setCookieStore(cookieStore);

        Header header1 = new BasicHeader("Authorization", "Basic YWRtaW46RGE1eUxSa2JBUQ==");
        Header header2 = new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
        Header header3 = new BasicHeader("Cookie", "xidP_remember=sadafi; xid=149166d769bf2039e5344c9a7ed7a07d; xid=149166d769bf2039e5344c9a7ed7a07d");
        List<Header> headers = Arrays.asList(header1, header2, header3);

//        httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore)
//                .setDefaultHeaders(headers).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
                .setDefaultHeaders(headers).build();

        loginToMariell();
        loginToSincereWedding();
    }

    private void loginToMariell() throws IOException {
        HttpPost post = new HttpPost("https://www.mariellonline.com/login.asp");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Email", "vendor.relations@s3stores.com"));
        nameValuePairs.add(new BasicNameValuePair("password", "P1IW6VN5F6I6uAT"));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpClient.execute(post, context);
    }

    private void loginToSincereWedding() throws IOException {
        HttpGet getCategories = new HttpGet("http://www.artistsupplysource.com/provider/categories.php");
        HttpResponse categoryPageResponse = httpClient.execute(getCategories, context);

//        HttpGet getFirstAccess = new HttpGet("http://www.artistsupplysource.com/provider/error_message.php?antibot_error");
//        HttpResponse firstAccessResponse = httpClient.execute(getFirstAccess, context);
//
//        HttpGet getAdaptive = new HttpGet("http://www.artistsupplysource.com/adaptive.php?send_browser=YYY%7CSafari%7C537.36%7CMacIntel%7CY%7C1440%7C900%7CP");
//        HttpResponse adaptiveResponse = httpClient.execute(getFirstAccess, context);
//
////        Header[] headers = firstAccessResponse.getAllHeaders();
////        for(Header header:headers){
////            if (header.getName().equalsIgnoreCase("Location")) {
////                HttpGet getErrorMsgPage = new HttpGet(header.getValue());
////                HttpResponse errorMsgPageResponse = httpClient.execute(getErrorMsgPage, context);
////            }
////        }
//
//        HttpPost postLogin = new HttpPost("http://www.artistsupplysource.com/include/login.php");
//
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//        nameValuePairs.add(new BasicNameValuePair("is_remember", "Y"));
//        nameValuePairs.add(new BasicNameValuePair("username", "sadafi"));
//        nameValuePairs.add(new BasicNameValuePair("password", "8fr7xFuw"));
//        nameValuePairs.add(new BasicNameValuePair("usertype", "P"));
//        nameValuePairs.add(new BasicNameValuePair("mode", "login"));
//        nameValuePairs.add(new BasicNameValuePair("redirect", "provider"));
//        postLogin.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//        HttpResponse loginResponse = httpClient.execute(postLogin, context);
//
//        HttpGet getHome = new HttpGet("http://www.artistsupplysource.com/provider/home.php");
//        HttpResponse homePageResponse = httpClient.execute(getHome, context);
//
//        HttpGet getCategories = new HttpGet("http://www.artistsupplysource.com/provider/categories.php");
//        HttpResponse categoryPageResponse = httpClient.execute(getCategories, context);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public HttpClientContext getContext() {
        return context;
    }
}

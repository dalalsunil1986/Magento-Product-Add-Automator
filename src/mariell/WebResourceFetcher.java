package mariell;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebResourceFetcher {

    public static String fetchHtml(String urlString) throws IOException {
        HttpClientHelper httpClientHelper = HttpClientHelper.getHelper();

        HttpGet httpGet = new HttpGet(urlString);
        HttpResponse response = httpClientHelper.getHttpClient().execute(httpGet, httpClientHelper.getContext());

//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();

        // open the stream and put it into BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuilder html = new StringBuilder(1024 * 1024);
        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            html.append(inputLine);
        }

        br.close();

        return html.toString();
    }

    public static BufferedImage fetchImage(String urlString) throws IOException {
        return ImageIO.read(new URL(urlString));

//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//
//        // open the stream and put it into BufferedReader
//        BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
//
//        while (bin.available() > 0) {
//            bin.re
//        }
//
//        bin.close();
//
//        return html.toString();
    }
}

package mariell;

import mariell.csv.Entry;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sharafat
 * Created: 7/23/14 1:03 AM
 */
public class ProductPoster {

    /**
     * @return Product ID
     */
    public static String post(Entry entry) throws IOException {
        HttpClientHelper httpClientHelper = HttpClientHelper.getHelper();

        HttpPost postProduct = new HttpPost("http://www.artistsupplysource.com/provider/product_modify.php");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("section", "main"));
        nameValuePairs.add(new BasicNameValuePair("mode", "product_modify"));
        nameValuePairs.add(new BasicNameValuePair("forsale", "Y"));
        nameValuePairs.add(new BasicNameValuePair("manufacturerid", "223"));
        nameValuePairs.add(new BasicNameValuePair("brandid", "2995"));
        nameValuePairs.add(new BasicNameValuePair("categoryid_text", Integer.toString(entry.categoryId)));
        nameValuePairs.add(new BasicNameValuePair("categoryid", Integer.toString(entry.categoryId)));
        nameValuePairs.add(new BasicNameValuePair("productcode", entry.sku));
        nameValuePairs.add(new BasicNameValuePair("product", entry.name));
        nameValuePairs.add(new BasicNameValuePair("product_froogle", entry.name));
        nameValuePairs.add(new BasicNameValuePair("fulldescr", entry.description));
        nameValuePairs.add(new BasicNameValuePair("list_price", entry.listPrice));
        nameValuePairs.add(new BasicNameValuePair("cost_to_us", entry.costToUs));
        nameValuePairs.add(new BasicNameValuePair("calculate_price_for_new_product", "Y"));
        nameValuePairs.add(new BasicNameValuePair("product_price_multiplier", "0.00"));
        nameValuePairs.add(new BasicNameValuePair("new_map_price", entry.mapPrice));
        nameValuePairs.add(new BasicNameValuePair("map_price", "0.00"));
        nameValuePairs.add(new BasicNameValuePair("avail", "1000000"));
        nameValuePairs.add(new BasicNameValuePair("low_avail_limit", "1000"));
        nameValuePairs.add(new BasicNameValuePair("min_amount", "1"));
        nameValuePairs.add(new BasicNameValuePair("free_tax", "Y"));
        nameValuePairs.add(new BasicNameValuePair("weight", entry.weight));
        nameValuePairs.add(new BasicNameValuePair("dimensions", "0,0,0"));
        nameValuePairs.add(new BasicNameValuePair("shipping_freight", "0.01"));
        nameValuePairs.add(new BasicNameValuePair("free_ship_zone", "-1"));
        nameValuePairs.add(new BasicNameValuePair("discount_slope", "0.60"));
        nameValuePairs.add(new BasicNameValuePair("discount_table", "2,3,4,6,8,12"));
        nameValuePairs.add(new BasicNameValuePair("discount_avail", "Y"));
        nameValuePairs.add(new BasicNameValuePair("generate_similar_products", "Y"));
        nameValuePairs.add(new BasicNameValuePair("free_ship_text", ""));
        nameValuePairs.add(new BasicNameValuePair("productid", ""));
        nameValuePairs.add(new BasicNameValuePair("geid", ""));
        nameValuePairs.add(new BasicNameValuePair("categoryids", ""));
        nameValuePairs.add(new BasicNameValuePair("upc", ""));
        nameValuePairs.add(new BasicNameValuePair("google_search_term", ""));
        nameValuePairs.add(new BasicNameValuePair("descr", ""));
        nameValuePairs.add(new BasicNameValuePair("lead_time_message", ""));

        postProduct.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClientHelper.getHttpClient().execute(postProduct, httpClientHelper.getContext());
        System.out.println(entry.sku + " add status: " + response.getStatusLine());

        Header[] headers = response.getAllHeaders();
        for(Header header:headers){
            if (header.getName().equalsIgnoreCase("Location")) {
                String productId = header.getValue().substring(header.getValue().indexOf('=') + 1);
                System.out.println("Location header content: " + header.getValue() + ", Product ID: " + productId);

                return productId;
            }
        }

        return null;
    }

    public static String getProductId(String sku) throws IOException {
        String html = WebResourceFetcher.fetchHtml("http://www.sincerewedding.com/product.php?sku=" + sku);

        Document doc = Jsoup.parse(html);

        if (doc.select("meta[itemprop=url]").size() == 0) {
            return null;
        }

        String url = doc.select("meta[itemprop=url]").get(0).attr("content");

        return url.substring(url.indexOf('=') + 1);
    }
}

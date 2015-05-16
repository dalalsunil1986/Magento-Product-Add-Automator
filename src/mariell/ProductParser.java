package mariell;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductParser {

    public static Product parseMariellProduct(String url) throws IOException {
        String html = WebResourceFetcher.fetchHtml(url);

        Product product = new Product();
        Document doc = Jsoup.parse(html);

        if (doc.select("[itemprop=name]").size() == 0) {
            return null;
        }

        product.title = doc.select("[itemprop=name]").text();
        product.price = doc.select("[itemprop=price]").get(0).childNodes().get(0).toString().substring(1);
        product.sku = doc.select("[class=product_code]").text();
        product.noOfImages = doc.select("[id=altviews] a").size();
        product.shortDescription = doc.select("[itemprop=description]").text();
        product.longDescription = doc.select("[id=product_description]").text();
        product.hasOptions = doc.select("[id=options_table] select").size() > 0;

        for (Element category : doc.select("[class=vCSS_breadcrumb_td] > b > a")) {
            product.categoryBreadCrumb.add(category.text());
        }
        product.categoryBreadCrumb.remove(0);

        if (product.hasOptions) {
            Map<String, String> options = new HashMap<String, String>();
            Elements selects = doc.select("#options_table select");
            for (Element select : selects) {
                Elements selectOptions = select.children();
                for (Element selectOption : selectOptions) {
                    options.put(selectOption.attr("value"), selectOption.text());
                }
            }

            Elements images = doc.select("#options_table img.vCSS_img_swatch");
            if (images.size() > 0) {
                for (Element img : images) {
                    String src = img.attr("src").replace("S.jpg", "T.jpg");
                    String id = img.attr("id").replace("optionimg_", "");

                    product.optionImages.put("http:" + src, options.get(id));
                }
            }
        }

        return product;
    }

    public static Product parseSincereWeddingProduct(String url) throws IOException {
        String html = WebResourceFetcher.fetchHtml(url);

        Product product = new Product();
        Document doc = Jsoup.parse(html);

        product.title = doc.select("h1[itemprop=name]").text();
        product.price = doc.select("[id=product_price]").text().substring(1);
        product.listPrice = ((TextNode) doc.select(":containsOwn(List price)").get(0).nextSibling()
                .childNode(0).childNode(0).childNode(0)).text().substring(1);
        product.sku = doc.select(":containsOwn(SKU)").text().substring(5);
        product.noOfImages = doc.select("[itemprop=image]").size();
        product.longDescription = doc.select("[itemprop=description]").text()
                .replaceAll("<br>\n", "").replaceAll("\\* ", "\n").substring(1);
        product.weight = doc.select("[id=product_weight]").text();

        for (Element category : doc.select("[itemprop=title")) {
            if (!category.text().equals("SincereWedding.com") && !category.text().equals("Mariell")) {
                product.categoryBreadCrumb.add(category.text());
            }
        }

        return product;
    }
}

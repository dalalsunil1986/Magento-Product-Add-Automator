package mariell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    public String title;
    public String listPrice;
    public String price;
    public String weight;
    public String sku;
    public String shortDescription;
    public String longDescription;
    public List<String> categoryBreadCrumb = new ArrayList<String>();
    public Map<String, String> optionImages = new HashMap<String, String>();
    public boolean hasOptions;
    public int noOfImages;

    protected Product clone() {
        Product clone = new Product();

        clone.title = String.valueOf(title);
        clone.listPrice = String.valueOf(listPrice);
        clone.price = String.valueOf(price);
        clone.weight = weight;
        clone.sku = String.valueOf(sku);
        clone.shortDescription = String.valueOf(shortDescription);
        clone.longDescription = String.valueOf(longDescription);
        clone.categoryBreadCrumb = new ArrayList<String>(categoryBreadCrumb);
        clone.optionImages = new HashMap<String, String>(optionImages);
        clone.hasOptions = hasOptions;
        clone.noOfImages = noOfImages;

        return clone;
    }
}

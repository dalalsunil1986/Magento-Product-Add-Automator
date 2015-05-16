package mariell;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.writer.CSVEntryConverter;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import mariell.csv.CsvParser;
import mariell.csv.Entry;
import mariell.image.ImageUpload;
import mariell.image.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String CSV_FILE_PATH = "/Users/sharafat/Downloads/Mariell Product Add Checking - Sheet1.csv";
    private static final String PRODUCT_LIST_INPUT_CSV_FILE_PATH = "Mariell_2014_Product_Listing_TEST.csv";
    private static final String PRODUCT_LIST_OUTPUT_CSV_FILE_PATH = "Mariell_2014_Product_Listing_Completed.csv";

    public static void main(String[] args) throws IOException {
        generateProductDetails();
    }

    private static void writeCsvEntry(Entry entry) throws IOException {
        Writer out = new FileWriter(PRODUCT_LIST_OUTPUT_CSV_FILE_PATH, true);
        CSVWriter<Entry> csvWriter = new CSVWriterBuilder<Entry>(out).strategy(CSVStrategy.UK_DEFAULT)
                .entryConverter(new CSVEntryConverter<Entry>() {
                    @Override
                    public String[] convertEntry(Entry entry) {
                        String[] columns = new String[20];

                        columns[0] = entry.givenProductCode;
                        columns[1] = entry.givenProductName;
                        columns[2] = entry.givenProductWeight;
                        columns[3] = entry.givenProductPrice;
                        columns[4] = entry.givenProductDescLong;
                        columns[5] = entry.givenProductDescShort;
                        columns[6] = entry.givenProductUrl;
                        columns[7] = entry.mariellProductLink;
                        columns[8] = entry.category;
                        columns[9] = entry.sku;
                        columns[10] = entry.name;
                        columns[11] = entry.description;
                        columns[12] = entry.listPrice;
                        columns[13] = entry.costToUs;
                        columns[14] = entry.mapPrice;
                        columns[15] = entry.weight;
                        columns[16] = entry.noOfImages;
                        columns[17] = entry.hasOptions;
                        columns[18] = entry.sincereWeddingProductLink;
                        columns[19] = entry.errors;

                        return columns;
                    }
                }).build();
        csvWriter.write(entry);
        csvWriter.close();
        out.close();
    }

    public static void generateProductDetails() throws IOException {
        List<Entry> entries = CsvParser.parse(new File(PRODUCT_LIST_INPUT_CSV_FILE_PATH));
        Entry columnTitles = entries.get(0);
        writeCsvEntry(columnTitles);
        entries.remove(0);

        int counter = 1;
        for (Entry entry : entries) {
            System.out.println(counter++ + ". " + entry.givenProductCode);

            System.out.println("Retrieving product link from Mariell...");
            String mariellProductUrl = getMariellProductUrl(entry.givenProductCode);

            entry.sku = "MRL-" + entry.givenProductCode;
            entry.name = "Mariell " + entry.givenProductName.replaceFirst(" -", ":").replaceAll(" -", ",");
            entry.sincereWeddingProductLink = "http://www.sincerewedding.com/product.php?sku=" + entry.sku;

            if (mariellProductUrl != null) {
                entry.mariellProductLink = mariellProductUrl;

                try {
                    System.out.println("Retrieving product details from Mariell...");
                    Product sourceProduct = ProductParser.parseMariellProduct(entry.mariellProductLink);

                    if (sourceProduct == null) {
                        System.out.println("Mariell product URL does not contain this SKU.");
                        entry.errors += " * " + "Mariell product URL does not contain this SKU.";
                    } else {
                        System.out.println("Product found. Processing Product...");

                        entry.category = StringUtils.join(sourceProduct.categoryBreadCrumb, " > ");
                        entry.name = "Mariell " + sourceProduct.title;
                        entry.costToUs = entry.givenProductPrice;
                        entry.mapPrice = Float.toString(Float.parseFloat(entry.costToUs) * 2);
                        entry.listPrice = Float.toString(Float.parseFloat(entry.mapPrice) + 2);
                        entry.weight = entry.givenProductWeight;
                        entry.noOfImages = Integer.toString(sourceProduct.noOfImages);
                        entry.hasOptions = sourceProduct.hasOptions ? "Yes" : "";

                        if (!sourceProduct.longDescription.contains(sourceProduct.shortDescription)) {
                            entry.description = "* " + sourceProduct.shortDescription
                                    + (sourceProduct.shortDescription.matches(".*\\W$") ? "" : ".");
                        }
                        if (sourceProduct.longDescription != null && !sourceProduct.longDescription.isEmpty()) {
                            entry.description += (entry.description == null || entry.description.isEmpty() ? "* " : "")
                                    + sourceProduct.longDescription;
                        }
                        entry.description = entry.description
                                .replaceAll("\\.", ".<br />\n* ")
                                .replaceAll("!", "!<br />\n* ")
                                .replaceAll("<br />\n\\* $", "")
                                .replaceAll("&nbsp;", " ")
                                .replaceAll("\\s{2,}", " ");

                        int categoryId = 55955;
                        for (int i = 0; i < sourceProduct.categoryBreadCrumb.size(); i++) {
                            categoryId = Category.getCategoryId(sourceProduct.categoryBreadCrumb.get(i), categoryId);
                            if (categoryId == -1) {
                                entry.categoryId = 56233;
                                entry.errors += " * " + "Fix category.";
                                break;
                            }
                            if (i == sourceProduct.categoryBreadCrumb.size() - 1) {
                                entry.categoryId = categoryId;
                                break;
                            }
                        }

                        if (entry.name.length() > 70) {
                            entry.errors += " * " + "Fix Froogle.";
                        }

                        System.out.println("Fetching " + sourceProduct.noOfImages + " detail images and "
                                + sourceProduct.optionImages.size() + " option images...");
                        List<String> imagePaths = fetchImages(entry.givenProductCode, sourceProduct.noOfImages,
                                sourceProduct.optionImages);

                        System.out.println("Retrieving product ID from SincereWedding...");
                        String productId = ProductPoster.getProductId(entry.sku);

                        if (productId == null || productId.isEmpty()) { //product does not exists
                            System.out.println("Product does not exist in SincereWedding. Adding product to SincereWedding...");
                            productId = ProductPoster.post(entry);
                        } else {
                            System.out.println("Product exists in SincereWedding.");
                        }

                        if (productId != null && !productId.isEmpty() && imagePaths.size() > 0) {
                            System.out.println("Processing fetched images...");

                            for (String imagePath : imagePaths) {
                                ImageUtil.processImage(imagePath);
                            }

                            System.out.println("Adding images...");
                            ImageUpload.postImage(imagePaths, productId, entry.sku, ImageUtil.isThumbnailSize(imagePaths.get(0)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Product does not exist at Mariell.");
            }

            writeCsvEntry(entry);

            System.out.println("Done...\n");
        }
    }

    private static List<String> fetchImages(String givenProductCode, int noOfImages, Map<String, String> optionImages)
            throws IOException {

        List<String> detailImages = new ArrayList<String>();

        for (int i = 2; i < noOfImages + 2; i++) {
            String imageFileName = givenProductCode + "-" + i + ".jpg";
            BufferedImage image = WebResourceFetcher.fetchImage("http://www.mariellonline.com/v/vspfiles/photos/"
                    + imageFileName);
            File outputfile = new File("images/" + imageFileName);
            ImageIO.write(image, "jpg", outputfile);

            detailImages.add("images/" + imageFileName);
        }

        for (Map.Entry<String, String> optionImage : optionImages.entrySet()) {
            String imageFileName = givenProductCode + "-" + optionImage.getValue().replace("/", "-") + ".jpg";
            BufferedImage image = WebResourceFetcher.fetchImage(optionImage.getKey());
            File outputfile = new File("images/" + imageFileName);
            ImageIO.write(image, "jpg", outputfile);
        }

        return detailImages;
    }

    private static String getMariellProductUrl(String givenProductCode) throws IOException {
        String html = WebResourceFetcher.fetchHtml("http://www.mariellonline.com/SearchResults.asp?Search=" + givenProductCode);
        Document doc = Jsoup.parse(html);
        if (doc.select(".matching_results_text").text().equalsIgnoreCase("We found 0 results matching your criteria.")) {
            return null;
        }

        Elements productUrl = doc.select("a[href$=" + givenProductCode + ".htm]");
        if (productUrl.size() == 0) {
            return null;
        }

        return productUrl.attr("href");
    }

    public static void checkProducts() throws IOException {
        List<Entry> entries = CsvParser.parse(new File(CSV_FILE_PATH));
        entries.remove(0);

        for (Entry entry : entries) {
            Product sourceProduct = ProductParser.parseMariellProduct(entry.mariellProductLink);
            Product targetProduct = ProductParser.parseSincereWeddingProduct(entry.sincereWeddingProductLink);

//            assertEquals(sourceProduct, targetProduct);
        }
    }

    private static List<String> assertEquals(Product sourceProduct, Product targetProduct) {
        List<String> errors = new ArrayList<String>();

        if (!targetProduct.title.startsWith("Mariell ")) {
            errors.add("Title is missing 'Mariell'.");
        }

        if (!targetProduct.title.contains(sourceProduct.title)) {
            errors.add("Title doesn't match.");
        }

        if (!targetProduct.sku.startsWith("MRL-")) {
            errors.add("SKU is missing 'MRL-'.");
        }

        if (!targetProduct.sku.equals("MRL-" + sourceProduct.sku)) {
            errors.add("SKU doesn't match.");
        }

        if (!targetProduct.price.equals(Float.toString(Float.parseFloat(sourceProduct.price) * 2))) {
            errors.add("Price doesn't match.");
        }

        if (!targetProduct.listPrice.equals(Float.toString(Float.parseFloat(targetProduct.price) + 2))) {
            errors.add("List Price is wrong.");
        }

        if (!targetProduct.weight.equals(getProductWeight(sourceProduct.sku))) {
            errors.add("Weight is wrong.");
        }

        if (targetProduct.noOfImages != sourceProduct.noOfImages) {
            errors.add("No. of images doesn't match.");
        }


        return errors;
    }

    private static String getProductWeight(String sku) {
        return null;
    }
}

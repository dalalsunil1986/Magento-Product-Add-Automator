package mariell.image;

import mariell.HttpClientHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUpload {

    private static HttpClientHelper httpClientHelper = HttpClientHelper.getHelper();

    public static void postImage(List<String> imageFilePaths, String productId, String sku,
                                 boolean generateThumbnailAndDeleteOriginal) throws IOException {

        StringBody mode = new StringBody("product_images", ContentType.TEXT_PLAIN);
        StringBody productIdBody = new StringBody(productId, ContentType.TEXT_PLAIN);
        StringBody alt1 = new StringBody("", ContentType.TEXT_PLAIN);
        StringBody thumbid = new StringBody("", ContentType.TEXT_PLAIN);
        StringBody geid = new StringBody("", ContentType.TEXT_PLAIN);
        StringBody additionalOperation1 = new StringBody(generateThumbnailAndDeleteOriginal
                ? "gt_del_img" : "gt", ContentType.TEXT_PLAIN);
        StringBody additionalOperation = new StringBody("none", ContentType.TEXT_PLAIN);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
                .addPart("mode", mode)
                .addPart("productid", productIdBody)
                .addPart("thumbid", thumbid)
                .addPart("geid", geid);

        List<FileBody> fileBodies = new ArrayList<FileBody>(imageFilePaths.size());
        for (String filePath : imageFilePaths) {
            fileBodies.add(new CustomFileBody(new File(filePath), filePath.substring(7), "image/jpeg"));
        }

        for (int i = 1; i <= fileBodies.size(); i++) {
            multipartEntityBuilder
                    .addPart("userfile_D_" + i, fileBodies.get(i - 1))
                    .addPart("additional_operation[" + i + "]", i == 1 ? additionalOperation1 : additionalOperation);

            if (i != 1) {
                multipartEntityBuilder
                        .addPart("additional_operation[" + i + "]", additionalOperation)
                        .addPart("additional_operation[" + i + "]", additionalOperation);
            }

            multipartEntityBuilder.addPart("alt[" + i + "]", alt1);
        }

        multipartEntityBuilder
                .setBoundary("----WebKitFormBoundaryG8CEG4TtDB9nL3jo")
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        HttpPost httppost = new HttpPost("http://www.artistsupplysource.com/provider/product_modify.php");
        httppost.setEntity(multipartEntityBuilder.build());

        httppost.setConfig(RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(60000)
                .setProxy(new HttpHost("127.0.0.1", 8008, "http"))
                .build());

        HttpClient httpclient = httpClientHelper.getHttpClient();

        HttpResponse response = httpclient.execute(httppost, httpClientHelper.getContext());
        System.out.println(sku + " image add status: " + response.getStatusLine());

//        Header[] headers = response.getAllHeaders();
//        for(Header header:headers){
//            if (header.getName().equalsIgnoreCase("Location")) {
//                WebResourceFetcher.fetchHtml("http://www.artistsupplysource.com/provider/" + header.getValue());
//                break;
//            }
//        }
    }


    public static void postImage_TEST(String imageFilePath, String text) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://localhost/multipart/index.php");

            FileBody file = new FileBody(new File(imageFilePath));
            StringBody txt = new StringBody(text, ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", file)
                    .addPart("txt", txt)
                    .build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    public static void main(String[] args) throws IOException {
        postImage_TEST("/home/sharafat/Desktop/test.png", "Hello World!");
    }

    private static class CustomFileBody extends FileBody {

        public CustomFileBody(final File file,
                        final String filename,
                        final String mimeType) {
            super(file, ContentType.create(mimeType), filename);
        }
    }
}

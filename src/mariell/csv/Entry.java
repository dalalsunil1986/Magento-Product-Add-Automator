package mariell.csv;

import com.googlecode.jcsv.annotations.MapToColumn;

public class Entry {

    @MapToColumn(column = 0)
    public String givenProductCode;

    @MapToColumn(column = 1)
    public String givenProductName;

    @MapToColumn(column = 2)
    public String givenProductWeight;

    @MapToColumn(column = 3)
    public String givenProductPrice;

    @MapToColumn(column = 4)
    public String givenProductDescLong;

    @MapToColumn(column = 5)
    public String givenProductDescShort;

    @MapToColumn(column = 6)
    public String givenProductUrl;

    @MapToColumn(column = 7)
    public String mariellProductLink;

    @MapToColumn(column = 8)
    public String category;

    @MapToColumn(column = 9)
    public String sku;

    @MapToColumn(column = 10)
    public String name;

    @MapToColumn(column = 11)
    public String description;

    @MapToColumn(column = 12)
    public String listPrice;

    @MapToColumn(column = 13)
    public String costToUs;

    @MapToColumn(column = 14)
    public String mapPrice;

    @MapToColumn(column = 15)
    public String weight;

    @MapToColumn(column = 16)
    public String noOfImages;

    @MapToColumn(column = 17)
    public String hasOptions;

    @MapToColumn(column = 18)
    public String sincereWeddingProductLink;

    @MapToColumn(column = 19)
    public String errors;

    public int categoryId;
}

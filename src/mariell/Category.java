package mariell;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sharafat
 * Created: 7/23/14 3:27 AM
 */
public class Category {

    private static final List<Integer> categoryIds = new ArrayList<Integer>();
    private static final List<Integer> parentCategoryIds = new ArrayList<Integer>();
    private static final List<String> categoryNames = new ArrayList<String>();

    static {
        categoryIds.add(55955); parentCategoryIds.add(0); categoryNames.add("Mariell");
        categoryIds.add(56258); parentCategoryIds.add(55955); categoryNames.add("Bridal & Wedding Veils");
        categoryIds.add(56275); parentCategoryIds.add(55955); categoryNames.add("Bridal Garters");
        categoryIds.add(56262); parentCategoryIds.add(55955); categoryNames.add("Bridal Purses, Evening Bags & Clutches");
        categoryIds.add(55956); parentCategoryIds.add(55955); categoryNames.add("Bridal, Wedding, Prom & Special Occasion Jewelry");
        categoryIds.add(56239); parentCategoryIds.add(55955); categoryNames.add("New Arrivals");
        categoryIds.add(56233); parentCategoryIds.add(55955); categoryNames.add("Prom 2014");
        categoryIds.add(56257); parentCategoryIds.add(55955); categoryNames.add("Shawls & Wraps");
        categoryIds.add(56314); parentCategoryIds.add(55955); categoryNames.add("Shoe Clips");
        categoryIds.add(56228); parentCategoryIds.add(55955); categoryNames.add("Tiaras, Headpieces & Bridal Hair Accessories");
        categoryIds.add(56345); parentCategoryIds.add(55955); categoryNames.add("Wholesale Bridal Garters");
        categoryIds.add(56259); parentCategoryIds.add(55955); categoryNames.add("Wholesale Shawls, Wraps & Faux Fur Wedding Stoles");
        categoryIds.add(56337); parentCategoryIds.add(55955); categoryNames.add("Wholesale Tiaras, Headpieces & Hair Accessories");
        categoryIds.add(56274); parentCategoryIds.add(55955); categoryNames.add("Wholesale Wedding Belts & Bridal Sashes");
        categoryIds.add(56261); parentCategoryIds.add(56258); categoryNames.add("Birdcage & French Netting");
        categoryIds.add(56273); parentCategoryIds.add(56258); categoryNames.add("Bead Or Crystal Embellished");
        categoryIds.add(56271); parentCategoryIds.add(56258); categoryNames.add("Cathedral Or floor Length");
        categoryIds.add(56272); parentCategoryIds.add(56258); categoryNames.add("Corded Or Ribbon Edge");
        categoryIds.add(56269); parentCategoryIds.add(56258); categoryNames.add("Mantillas Or Embroidered");
        categoryIds.add(56267); parentCategoryIds.add(56262); categoryNames.add("Bridal Handbags");
        categoryIds.add(56263); parentCategoryIds.add(56262); categoryNames.add("Prom & Evening Bags");
        categoryIds.add(56240); parentCategoryIds.add(55956); categoryNames.add("Bracelets");
        categoryIds.add(56247); parentCategoryIds.add(55956); categoryNames.add("Brooches & Comb Converters");
        categoryIds.add(55957); parentCategoryIds.add(55956); categoryNames.add("Earrings");
        categoryIds.add(56235); parentCategoryIds.add(55956); categoryNames.add("Necklace & Earring Sets");
        categoryIds.add(56237); parentCategoryIds.add(55956); categoryNames.add("Necklace Extenders");
        categoryIds.add(56225); parentCategoryIds.add(55956); categoryNames.add("Necklaces");
        categoryIds.add(56245); parentCategoryIds.add(55956); categoryNames.add("Rings");
        categoryIds.add(56230); parentCategoryIds.add(56228); categoryNames.add("Bridal Combs & Barrettes");
        categoryIds.add(56270); parentCategoryIds.add(56228); categoryNames.add("Hair Spirals & Hair Sticks");
        categoryIds.add(56260); parentCategoryIds.add(56228); categoryNames.add("Headbands & Hair Vines");
        categoryIds.add(56268); parentCategoryIds.add(56228); categoryNames.add("Tiaras & Tiara Combs");
        categoryIds.add(56242); parentCategoryIds.add(56240); categoryNames.add("Cubic Zirconia");
        categoryIds.add(56241); parentCategoryIds.add(56240); categoryNames.add("Pearl");
        categoryIds.add(56353); parentCategoryIds.add(56240); categoryNames.add("Petite Bracelets");
        categoryIds.add(56250); parentCategoryIds.add(56240); categoryNames.add("Rhinestone");
        categoryIds.add(56249); parentCategoryIds.add(56247); categoryNames.add("Crystal Or Rhinestone");
        categoryIds.add(56248); parentCategoryIds.add(56247); categoryNames.add("Pearl");
        categoryIds.add(56343); parentCategoryIds.add(55957); categoryNames.add("Rhinestone");
        categoryIds.add(55958); parentCategoryIds.add(55957); categoryNames.add("Cubic Zirconia");
        categoryIds.add(56227); parentCategoryIds.add(55957); categoryNames.add("Pearl");
        categoryIds.add(56254); parentCategoryIds.add(56235); categoryNames.add("Rhinestone");
        categoryIds.add(56252); parentCategoryIds.add(56235); categoryNames.add("Colored");
        categoryIds.add(56251); parentCategoryIds.add(56235); categoryNames.add("Crystal");
        categoryIds.add(56236); parentCategoryIds.add(56235); categoryNames.add("Cubic Zirconia");
        categoryIds.add(56243); parentCategoryIds.add(56235); categoryNames.add("Pearl");
        categoryIds.add(56244); parentCategoryIds.add(56225); categoryNames.add("Cubic Zirconia");
        categoryIds.add(56226); parentCategoryIds.add(56225); categoryNames.add("Pearl");
        categoryIds.add(56246); parentCategoryIds.add(56245); categoryNames.add("Pearl");
    }

    public static int getCategoryId(String categoryName, int parentCategoryId) {
        for (int i = 0; i < categoryNames.size(); i++) {
            if (categoryNames.get(i).equalsIgnoreCase(categoryName) && parentCategoryIds.get(i) == parentCategoryId) {
                return categoryIds.get(i);
            }
        }

        return -1;
    }
}

public class Main {

    public static void main(String[] args) {
        String test = "* For a sparkling tailored style, Mariell presents our top-selling round solitaire crystal wedding or prom drop earrings at a fantastic wholesale price! For a sparkling design with a tailored style, Mariell presents our top-selling     beautiful  round &nbsp; solitaire crystal earrings framed with pave crystals in a rich gold tone setting. These sophisticated wedding or prom earrings measure 1/2\" and dangle from a 1/2\" euro wire top. They are available in an array of colors at a fantastic price!";

        System.out.println(test
                .replaceAll("\\.", ".<br />\n* ")
                .replaceAll("!", "!<br />\n* ")
                .replaceAll("<br />\n\\* $", "")
                .replaceAll("&nbsp;", " ")
                .replaceAll("\\s{2,}", " "));

//        String data = "a:2:{s:4:\"list\";a:1:{i:0;a:12:{i:0;s:2:\"75\";s:11:\"draw_number\";s:2:\"75\";i:1;s:10:\"2014-04-30\";s:9:\"draw_date\";s:10:\"2014-04-30\";i:2;s:9:\"1st Prize\";s:14:\"prize_category\";s:9:\"1st Prize\";i:3;s:7:\"0112526\";s:16:\"prizebond_number\";s:7:\"0112526\";i:4;s:6:\"600000\";s:12:\"prize_amount\";s:6:\"600000\";i:5;s:59:\"http://www.bb.org.bd/investfacility/prizebond/images/75.gif\";s:7:\"imgpath\";s:59:\"http://www.bb.org.bd/investfacility/prizebond/images/75.gif\";}}s:6:\"status\";s:5:\"200ok\";}";
//        MixedArray responseArray = Pherialize.unserialize(data).toArray();
//        MixedArray list = responseArray.getArray("list").getArray(0);
//        System.out.println("draw_number: " + list.get("draw_number"));
//        System.out.println("draw_date: " + list.get("draw_date"));
//        System.out.println("prize_category: " + list.get("prize_category"));
//        System.out.println("prizebond_number: " + list.get("prizebond_number"));
//        System.out.println("prize_amount: " + list.get("prize_amount"));
//        System.out.println("imgpath: " + list.get("imgpath"));
    }
}

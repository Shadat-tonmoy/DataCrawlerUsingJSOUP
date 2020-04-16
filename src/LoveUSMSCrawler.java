import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.Constants;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class LoveUSMSCrawler {

    public static void crawlData() {

        try {

            ArrayList<String> smsList = new ArrayList<>();
            for (int i = 101; i <= 133; i++) {
                String link = Constants.PAGE_LINK + i;
                Document document = Jsoup.connect(link).get();
//            System.out.println(document.toString());
                Elements ps = document.getElementsByClass("bx");
                String sms = "";
                for (Element element : ps) {
                    String line = element.text();
                    String header = element.child(0).text();
                    String footer = " by - Admin Copy Text";
//                System.out.println("Header\n\n"+header+"\n\n");
                    line = line.replace(header, "").replace(footer, "").trim();
//                System.out.println("Text\n\n"+line+"\n\n");

                    if (line.length() > 5) {
                        smsList.add(line);
                    }
                }
            }

            int i = 0;
            JSONArray jsonArray = new JSONArray();
            for (String smsText : smsList) {
                System.out.println("At index " + i + " SMS is \n\n" + smsText);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", smsText);
                jsonArray.add(jsonObject);
                i++;
            }
            String filePath = Constants.CURRENT_DIR + "/SMS/data.txt";
            System.out.println(jsonArray.toString());
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonArray.toString());
            System.out.println("Successfully Copied JSON Object to File... at " + filePath);
            fileWriter.close();
//                System.out.println("\nJSON Object: " + obj);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

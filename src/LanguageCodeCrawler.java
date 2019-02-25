import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class LanguageCodeCrawler {

    public static void crawlLanguageCode()
    {
        String url = "https://wiki.openstreetmap.org/wiki/Nominatim/Country_Codes";
        List<String> supportedLanguages = Utitlites.readJSONFile("");

        try {

            Document document = Jsoup.connect(url).get();
            Elements languageTables = document.getElementsByTag("table");
            for(String supportedLanguage:supportedLanguages)
            {
                for(Element languageTable:languageTables)
                {
                    Elements rows = languageTable.getElementsByTag("tr");
                    for(Element row:rows)
                    {
                        Elements columns = row.getElementsByTag("td");
                        String columnData = "";
                        if(columns.size()>0)
                        {
                            String country = columns.get(1).text();
                            String[] languages = columns.get(columns.size()-1).text().split(",");
                            if(isLanguageMatched(supportedLanguage,languages))
                            {
                                Utitlites.println("MatchedWith Country : "+country+" Languages : "+supportedLanguage);
                                Utitlites.renameFlagFile(country,supportedLanguage);

                            }
                        }
                    }
                }
            }






        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("Error",e.getMessage());
        }




    }

    private static boolean isLanguageMatched(String supportedLanguage,String[] languages)
    {
        for(String language:languages)
        {
            if(language.trim().equals(supportedLanguage.trim()))
                return true;
        }
        return false;
    }
}

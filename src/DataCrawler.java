import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.Utitlites;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataCrawler {

    public static void main(String args[]) {
        LanguageCodeCrawler.crawlLanguageCode();

    }

    private static void getTimeStamps(String dateTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy h:m a");
        Locale[] locales = SimpleDateFormat.getAvailableLocales();

        try {

            Date date = simpleDateFormat.parse(dateTime);
            long timeStamp = date.getTime();
            System.out.println(date.toString()+" TimeStamp "+timeStamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void getLiveScoreData()
    {
        String url = "http://www.espncricinfo.com/series/18042/game/1152763/melbourne-stars-women-vs-melbourne-renegades-women-39th-match-womens-big-bash-league-2018-19";

        try {

            Document document = Jsoup.connect(url).get();
            Elements teamScores = document.getElementsByClass("cscore_item");
            Elements runRates= document.getElementsByClass("gp__cricket__stat");
            Utitlites.println(teamScores.toString());
            Utitlites.println(runRates.toString());
            Element teamAInfo = teamScores.get(0);
            Element teamBInfo = teamScores.get(1);

            Element currentRunRateInfo = runRates.get(0);
            Element requiredRunRateInfo = runRates.get(1);

            String teamAName = teamAInfo.getElementsByClass("cscore_name").first().text();
            String teamAScore = teamAInfo.getElementsByClass("cscore_score").first().text();


            String teamBName = teamBInfo.getElementsByClass("cscore_name").first().text();
            String teamBScore = teamBInfo.getElementsByClass("cscore_score").first().text();

            String currentRunRate = runRates.get(0).text();
            String requiredRunRate = runRates.get(1).text();

            Utitlites.println("TeamA Name "+teamAName+" Score "+teamAScore);
            Utitlites.println("TeamB Name "+teamBName+" Score "+teamBScore);
            Utitlites.println("RR currentRunRate "+currentRunRate+" requiredRunRate "+requiredRunRate);




        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("Error",e.getMessage());
        }


    }


}


import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;

public class BPLCrawler {

    public static void readLocalData()
    {
        String dataURL = Constants.CURRENT_DIR+"/crawled_data/BPLPlayersWithURL.json";
        try{
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(dataURL));
            for(int i=0;i<jsonArray.size();i++)
            {
                JSONObject team = (JSONObject) jsonArray.get(i);
                String temaName = (String) team.get("teamName");
                JSONArray players = (JSONArray) team.get("players");
                Utitlites.println("Players for "+temaName+"\n\n");
                for(int j=0;j<players.size();j++)
                {
                    JSONObject player = (JSONObject) players.get(j);
                    String name = (String) player.get("name");
                    String url = (String) player.get("url");
                    Utitlites.println("Name : "+name+"\tURL "+url+"\n");
                    fetchPlayerDetail(url,name);


                }

            }

        }catch (Exception e)
        {

        }



    }
    public static void crawlData()
    {
        try
        {
            Document document = Jsoup.connect(Constants.BPL_LINK).get();
            Elements paragraphs = document.getElementsByTag("p");
            JSONArray jsonArray = new JSONArray();
            for(Element paragraph : paragraphs)
            {
                Elements elements = paragraph.getAllElements();
                for(Element paraElement:elements)
                {
                    String text = paraElement.text();
                    if(text.startsWith(Constants.Dhaka_Dynamites) ||text.startsWith(Constants.Chittagong_Vikings)
                            || text.startsWith(Constants.Rangpur_Riders) || text.startsWith(Constants.Comilla_Victorians)
                            || text.startsWith(Constants.Khulna_Titans) || text.startsWith(Constants.Rajshahi_Kings)
                            || text.startsWith(Constants.Sylhet_Sixers))
                    {

                        String parts[] = text.split(":");
                        if(parts.length==4)
                        {
                            JSONObject team = new JSONObject();
                            String teamName = parts[0];
                            team.put("teamName",teamName);
                            String[] players = parts[1].replace(" Retained Players","").split(",");
                            for(String player:players)
                            {
                                searchPlayer(player.trim());
                            }
                            team.put("players",jsonArrayFromString(players));
                            String retained_players[] = parts[2].replace(" Direct Signings","").split(",");
                            team.put("retained_players",jsonArrayFromString(retained_players));
                            String[] direct_signing_players = parts[3].split(",");
                            team.put("direct_signing_players",jsonArrayFromString(direct_signing_players));
                            jsonArray.add(team);
//                            Utitlites.println("\n\nFor Team "+teamName+" Players\n"+players);
//                            Utitlites.println("Retained For Team "+teamName+" Players\n"+retained_players);
//                            Utitlites.println("Direct Signing For Team "+teamName+" Players\n"+direct_signing_players);

                        }
                    }
//                    Utitlites.println("Element "+paraElement.text()+"\n\n\n");
                }
            }
            Utitlites.println(jsonArray.toString());
//            Utitlites.JSONFileWriter("BPLPlayersWithURL",jsonArray);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void fetchPlayerDetail(String url,String name)
    {
        try {
            Document document = Jsoup.connect(Constants.CRIC_API+url).get();
            Elements tables = document.getElementsByTag("table");
            Element personalInfo = tables.get(0);
            Element affiliationInfo = tables.get(1);
            Element battingInfo = tables.get(2);
            Element bowlingInfo = tables.get(3);
            Elements rows = personalInfo.getElementsByTag("tr");
            Utitlites.println("Personal Info\n");
            for(Element row:rows)
            {
                Element attributeNode = row.getElementsByTag("th").first();
                Element valueNode  = row.getElementsByTag("td").first();
                String attribute = "",value="";
                if(attributeNode!=null)
                    attribute = attributeNode.text();
                if(valueNode!=null)
                    value = valueNode.text();
                Utitlites.println(attribute+" : "+value);
            }

            rows = affiliationInfo.getElementsByTag("tr");
            Utitlites.println("Affiliation Info\n");
            for(Element row:rows)
            {
                Elements affiliations= row.getElementsByTag("td");
                String affiliation = "";
                for(Element affiliationNode:affiliations)
                {
                    if(affiliationNode!=null)
                    {
                        affiliation = affiliationNode.text();
                        Utitlites.print(affiliation+"\t");
                    }
                }
            }
            Utitlites.println("");


            rows = battingInfo.getElementsByTag("tr");
            Utitlites.println("Batting Info\n");
            for(Element row:rows)
            {
                Elements headers= row.getElementsByTag("th");
                Elements datas= row.getElementsByTag("td");
                if(headers!=null)
                {
                    for(Element header:headers)
                    {
                        String headerText = header.text();
                        Utitlites.print(String.format("%10s",headerText));
                    }
                }

                if(datas!=null)
                {
                    for(Element data:datas)
                    {
                        String dataText = data.text();
                        Utitlites.print(String.format("%10s",dataText));
                    }
                }
                Utitlites.println("");
            }

            rows = bowlingInfo.getElementsByTag("tr");
            Utitlites.println("Bowling Info\n");
            for(Element row:rows)
            {
                Elements headers= row.getElementsByTag("th");
                Elements datas= row.getElementsByTag("td");
                if(headers!=null)
                {
                    for(Element header:headers)
                    {
                        String headerText = header.text();
                        Utitlites.print(String.format("%10s",headerText));
                    }
                }

                if(datas!=null)
                {
                    for(Element data:datas)
                    {
                        String dataText = data.text();
                        Utitlites.print(String.format("%10s",dataText));
                    }
                }
                Utitlites.println("");
            }
            fetchProfileImage(document,name.trim());


        }catch (Exception e)
        {
//            e.printStackTrace();

        }

    }

    private static void fetchProfileImage(Document document,String playerName)
    {
        Element imageNode = document.getElementsByTag("img").first();
        if(imageNode!=null)
        {
            String imageUrl = imageNode.attr("src");
            Utitlites.println("Image "+imageUrl);
            Utitlites.writeImageFile(imageUrl,playerName.replace(" ","_").toLowerCase());
        }

    }

    private static String searchPlayer(String playerName)
    {
        playerName = playerName.replace(" ","+");
        try {
            Document document = Jsoup.connect(Constants.SEARCH_URL+playerName).get();
            Utitlites.println("Searching For....."+playerName);
            Element linkField = document.getElementsByAttribute("itemprop").first();
            String id = linkField.attr("href");
            Utitlites.println("ID for "+playerName+" "+id);
            return id;


        }catch (Exception e)
        {
            e.printStackTrace();
            return null;

        }
    }

    public static JSONArray jsonArrayFromString(String[] arr)
    {
        JSONArray jsonArray = new JSONArray();
        for(String name:arr)
        {
            JSONObject player = new JSONObject();
            player.put("name",name);
            player.put("url",searchPlayer(name.trim()));
            player.put("image",name.replace(" ","_").toLowerCase());
            jsonArray.add(player);
        }
        return jsonArray;
    }
}

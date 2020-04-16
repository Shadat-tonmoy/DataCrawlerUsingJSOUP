package EnglishWordCollections;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import utility.Utitlites;

import java.util.ArrayList;
import java.util.List;

public class EnglishWordCrawler
{
    private static ListOfWord listOfWord;
    public static void main(String args[])
    {
        crawlEnglishWord();

    }

    public static void crawlEnglishWord()
    {
        String url = "https://www.vocabulary.com/lists/52473";
        List<Word> wordList = new ArrayList<>();

        try {

            Document document = Jsoup.connect(url).get();
            Elements wordDetails = document.getElementsByClass("learnable");
            int wordIndex = 0;
            for(Element wordDetail : wordDetails)
            {
//                System.out.println("WordDetail : "+wordDetail.toString());
                List<Element> childNodes = wordDetail.children();
                String word = "", definition = "",example = "";
                for(Element childNode : childNodes)
                {
//                    System.out.println("ChildNode : "+childNode.toString() +" className "+childNode.className());
                    switch (childNode.className())
                    {
                        case "word dynamictext":
                            //get word;
                            word = childNode.text();
                            break;
                        case "definition":
                            //get definition;
                            definition = childNode.text();
                            break;
                        case "details":
                            //get details;
                            example = getExampleFromDetails(childNode);
                            break;
                    }
                    /*Element word = childNode.getElementsByClass("word").get(0);
                    Element definition = childNode.getElementsByClass("definition").get(0);
                    System.out.println("ChildNode : Word - "+word+" Definition - "+definition);*/
                }
                wordList.add(new Word(wordIndex++,word,definition,example));
//                System.out.println("WordDetailObject\nWord : "+word+" Definition : "+definition+" Example : "+example);
            }
            listOfWord = new ListOfWord(wordList);
            String wordListJSON = new Gson().toJson(listOfWord);
            System.out.println("WordListJSON : "+wordListJSON);
            Utitlites.JSONFileWriter("english_word_collections",wordListJSON);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("Error",e.getMessage());
        }




    }

    private static String getExampleFromDetails(Element element)
    {
        List<Element> children = element.children();
        for(Element child : children)
        {
            if(child.className().equals("example"))
                return child.text();
        }
        return "NOT_WORKING";
    }
}


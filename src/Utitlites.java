import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utitlites {
    public static void println(String str)
    {
        System.out.println(str);
    }

    public static void print(String str)
    {
        System.out.print(str);
    }



    public static void JSONFileWriter(String fileName, JSONObject jsonArray)
    {
        String filePath = Constants.CURRENT_DIR + "/crawled_data/"+fileName+".json";
//        System.out.println(jsonArray.toString());
        try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonArray.toString());
            System.out.println("Successfully Copied JSON Object to File... at " + filePath);
            fileWriter.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void writeToFile(JSONObject jsonArray)
    {
        String filePath = Constants.CURRENT_DIR + "/language_data/mapped"+".json";
//        System.out.println(jsonArray.toString());
        try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonArray.toString());
            System.out.println("Successfully Copied JSON Object to File... at " + filePath);
            fileWriter.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void writeImageFile(String imageUrl,String fileName)
    {
        try
        {
            String filePath = Constants.CURRENT_DIR+"/crawled_data/images/";
            filePath+=fileName+".png";
            InputStream inputStream = new URL(imageUrl).openStream();
            File file = new File(filePath);
            if(!file.exists())
            {
                Utitlites.println("Creating File at "+filePath);
                file.createNewFile();
            }
//            FileUtils.copyInputStreamToFile(inputStream, file);

            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();


//            Files.copy(in, Paths.get(filePath));


        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public static List<String> readJSONFile(String filePath)
    {
        filePath = Constants.CURRENT_DIR+"/language_data/languages.json";
        JSONParser parser = new JSONParser();
        List<String> languages = new ArrayList<>();

        try {
            Object obj = parser.parse(new FileReader(filePath));

            JSONArray jsonArray = (JSONArray) obj;

            for(int i=0;i<jsonArray.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String code = (String) jsonObject.get("code");
                languages.add(code);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return languages;
    }

    public static void renameFlagFile(String countryName,String languageName)
    {
        File folder = new File(Constants.CURRENT_DIR+"/languages_pics");
        File[] files = folder.listFiles();
        countryName = countryName.replace(" ","-").toLowerCase()+".png";
        for(File file:files)
        {
            Utitlites.println("FileName "+file.getName()+" CountryName "+countryName);
            if(file.getName().equals(countryName))
            {
                File renamedFile = new File(Constants.CURRENT_DIR+"/languages_pics/renamed/"+languageName+".png");
                if(!renamedFile.exists())
                {
//                    Utitlites.println("RenamedTo "+renamedFile.getName());
                    if(file.renameTo(renamedFile))
                        Utitlites.println("Renamed");
                    else Utitlites.println("Error Renaming");
                }
                else
                {
                    Utitlites.println("FileExists "+file.getAbsolutePath());
                }
                Utitlites.println("MatchedFile "+countryName);
            }

        }
    }

}

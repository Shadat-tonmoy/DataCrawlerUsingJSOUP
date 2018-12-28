import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}

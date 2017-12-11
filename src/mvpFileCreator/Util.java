package mvpFileCreator;

import com.intellij.openapi.project.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
    public static String readPackageName(String modulePath){
        final String MatchStr = "src/main/java/";
        int po = modulePath.lastIndexOf(MatchStr);
        po+=MatchStr.length();
        String tmp = modulePath.substring(po);
        tmp = tmp.replace('/','.');
        return tmp;
    }
    public static String getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static boolean isValidViewOrModuleName(String name) throws RuntimeException{
        if(name==null||name.isEmpty()){
            throw new RuntimeException("非法的名称：非空");
        }
        char c = name.charAt(0);
        if(!Character.isUpperCase(c)){
            throw new RuntimeException("非法的名称：首字母必须大写");
        }

        String importFileRole = "^[A-Za-z]+$";
        Pattern p = Pattern.compile(importFileRole);
        if (!p.matcher(name).matches()) {
            throw new RuntimeException("非法的名称：只可以包含大小写字母");
        }
        return true;
    }
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void writeFile(String content,String fileName){
        try {
            OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName), "utf-8");
            oStreamWriter.append(content);
            oStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

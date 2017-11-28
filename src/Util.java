import com.intellij.openapi.project.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
    public static String readPackageName(Project project){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getBasePath() + "/App/src/main/AndroidManifest.xml");

            NodeList dogList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < dogList.getLength(); i++) {
                Node dog = dogList.item(i);
                Element elem = (Element) dog;
                return elem.getAttribute("package");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static boolean isValidViewName(String name) throws RuntimeException{
        if(name==null||name.isEmpty()){
            throw new RuntimeException("非法的视图名称：非空");
        }
        char c = name.charAt(0);
        if(!Character.isUpperCase(c)){
            throw new RuntimeException("非法的视图名称：首字母必须大写");
        }

        String importFileRole = "[a-zA-Z0-9_-]";//正则表达式
        Pattern p = Pattern.compile(importFileRole);//获取正则表达式中的分组，每一组小括号为一组
        if (p.matcher(name).matches()) {//判断正则表达式是否匹配到
            return true;
        }
        return false;
    }
}

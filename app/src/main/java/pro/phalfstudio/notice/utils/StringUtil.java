package pro.phalfstudio.notice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static ArrayList<String> string2arraylist(String str) {
        Pattern pattern = Pattern.compile("http://[^\\s,\"]+");
        Matcher matcher = pattern.matcher(str);
        ArrayList<String> imageList = new ArrayList<>();
        while (matcher.find()) {
            imageList.add(matcher.group());
        }
        return imageList;
    }
}

package com.fxlweb.sexspider.sexspider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengxl on 2014-4-6 006.
 */
public class HtmlHelper {

    public HtmlHelper() {}

    public static String getString(String site, String url, String str, String start, String end, String include) {
        str = str.toLowerCase();
        if(site.equals("http://www.aa544.com")) {
            str = str.replaceAll("href=", "href=\"");
            str = str.replaceAll(".html", ".html\"");
        }

        StringBuilder sb = new StringBuilder();
        String result = getHtml(str, start, end);

        result = result.replaceAll("<img.*?>", "");
        result = result.replaceAll("<font.*?>", "");
        result = result.replaceAll("</font>|<b>|</b>|<span.*?>|</span>|<br/>", "");

        //Pattern pattern = Pattern.compile("<span.*?/span>");
        //Matcher matcher = pattern.matcher(result);
        //result = matcher.replaceAll("");

        String role = "<ahref=(?=\"" + include + ").*?/a>";
        Pattern pattern = Pattern.compile(role);
        Matcher matcher = pattern.matcher(result);
        while(matcher.find()) {
            Pattern p = Pattern.compile(">.*?</a>");
            Matcher m = p.matcher(matcher.group());
            boolean b = false;
            while(m.find()) {
                String s = m.group().replaceAll(">|</a>", "");
                if(s.length() <= 2 || s.matches("^[a-zA-Z0-9_]*$")) b = true;
                else sb.append(s);
            }
            if(b) continue;
            sb.append("||");
            p = pattern.compile("href=\".*?\"");
            m = p.matcher(matcher.group());
            while(m.find()) {
                String link = m.group().replaceAll("href=\"|\"", "");
                if(link.indexOf("http") == -1) sb.append(site);
                sb.append(link);
            }

            sb.append("@@");
        }

        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static String getImages(String site, String str, String start, String end) {
        str = str.toLowerCase();

        StringBuilder sb = new StringBuilder();
        String result = getHtml(str, start, end);

        Pattern pattern = Pattern.compile("<img.*?>");
        Matcher matcher = pattern.matcher(result);
        while(matcher.find()) {
            Pattern p = Pattern.compile("src=\".*?\"");
            Matcher m = p.matcher(matcher.group());
            while(m.find()) {
                String link = m.group().replaceAll("src=\"|\"", "");
                if(link.contains("/tmb/")) continue;
                if(link.indexOf("http") == -1) sb.append(site);
                sb.append(link);
            }

            sb.append("@@");
        }

        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static String getHtml(String str, String start, String end) {
        String result = "";
        //result = str.replace("\"", "");
        result = str.replaceAll("\\s|\r\n|\n|\t", "");
        result = result.substring(result.indexOf(start), result.length());
        result = result.substring(0, result.indexOf(end));

        return result;
    }

}

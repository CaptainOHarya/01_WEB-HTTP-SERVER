package ru.netology.server.request;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;


import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leonid Zulin
 * @date 19.03.2023 15:19
 */
public class Example {

    public static void main(String[] args) throws URISyntaxException {
        String allPath = "http://www.google.com/search?q=httpclient&btnG=Google+Search&aq=f&oq";
        String path = null;
        String pathQuery = null;
          if (allPath.contains("?")) {
            path = allPath.substring(0, allPath.indexOf("?"));
              System.out.println(allPath);
             pathQuery = allPath.substring(allPath.indexOf("?") + 1);
        }
        System.out.println(path);
        System.out.println(pathQuery);


        Map<String, String> result = getQueryParams(pathQuery);
       // Map<String, String> result = getQueryParams("HGBGKJGJKJ&name=dsdd&name=ww");



        for(Map.Entry<String,String> entry : result.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }


    }



    public static Map<String, String> getQueryParams(String extras) {
        Map<String, String> results = new HashMap<>();
        try {
            // URI rawExtras = new URI("?" + extras);
            // System.out.println(rawExtras);
           // CharSequence charSequence = new String("&");
            // CharSequence charSequence;
                       // List<NameValuePair> extraList = URLEncodedUtils.parse(rawExtras,  Charset.forName("UTF-8"));
            List<NameValuePair> extraList = URLEncodedUtils.parse(extras,  Charset.forName("UTF-8"));
            for (NameValuePair item : extraList) {
                String name = item.getName();
                String value;
                int i = 0;
                while (results.containsKey(name)) {
                    name = item.getName() + ++i;
                }
                value = item.getValue();
                results.put(name, value);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}

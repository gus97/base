package cn.gus.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class HttpClientUtils {


    public static void main(String[] args) throws IOException {

        Map<String,String> map = new HashMap<>();
        map.put("name","guxichang");
        map.put("age","37");
        sendPostWithSynchronized("http://127.0.0.1:8081/test",map,"<123>");

    }


    public static String sendPostWithSynchronized(String url, Map<String, String> param,String se) throws IOException {

        HttpPost httpPost = new HttpPost(url);
        HttpEntity requestEntity = new StringEntity(se, "UTF-8");
        if(!param.isEmpty()) {
            List<BasicNameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));

            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");

            httpPost.setEntity(entity);
        }else{
            httpPost.setEntity(requestEntity);
        }

        //HttpClient httpClient = new SSLClient();

        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse httpResponse = httpClient.execute(httpPost);

        HttpEntity responsetEntity = httpResponse.getEntity();
        InputStream inputStream = responsetEntity.getContent();

        StringBuilder reponseXml = new StringBuilder();
        byte[] b = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(b)) != -1) {
            reponseXml.append(new String(b, 0, length));
        }

        System.out.println(reponseXml + "----");

        return null;
    }
}

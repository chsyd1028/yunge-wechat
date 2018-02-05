package com.csyd.yungewechat.util;

import com.alibaba.fastjson.JSONObject;
import com.csyd.yungewechat.menu.Button;
import com.csyd.yungewechat.menu.ClickButton;
import com.csyd.yungewechat.menu.Menu;
import com.csyd.yungewechat.menu.ViewButton;
import com.csyd.yungewechat.po.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Created by ChengShanyunduo
 * 2018/1/24
 */
public class WxUtil {

    //private static final String APP_ID = "wxf3a013e299ac5cca";

    //private static final String APP_SECRET = "3c16a0ba661f15575d5cec5280320c4f";

    private static final String APP_ID = "wx79f1748e1f4b42b6";

    private static final String APP_SECRET = "1a2e3a2a68b684e8fdd4b3abd6147ba2";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    /**
     * get请求
     * @param url
     * @return
     */
    public static JSONObject doGetStr(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (!Objects.isNull(entity)){
                String result = EntityUtils.toString(entity);
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * post请求
     * @param url
     * @param outStr
     * @return
     */
    public static JSONObject doPostStr(String url, String outStr){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        try {
            httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    /**
     * 获取access_token
     * @return
     */
    public static AccessToken getAccessToken(){
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", APP_ID).replace("APPSECRET", APP_SECRET);
        JSONObject jsonObject = doGetStr(url);
        if (!Objects.isNull(jsonObject)){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
        }
        return token;

    }

    /**\
     * 上传素材返回mediaId
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     */
    public static String upload(String filePath, String accessToken, String type) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()){
            throw new IOException("文件不存在");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);

        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition:form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");
        OutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(head);

        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1){
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;

        try {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                reader.close();
            }
        }

        JSONObject jsonObject = JSONObject.parseObject(result);
        System.out.println(jsonObject);
        String typeName = "media_id";
        if (!"image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObject.getString(typeName);
        return mediaId;
    }

    /**
     * 组装菜单
     * @return
     */
    public static Menu initMenu(){
        Menu menu = new Menu();

        ClickButton button11 = new ClickButton();
        button11.setName("click菜单");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();
        button21.setName("view菜单");
        button21.setType("view");
        button21.setUrl("http://www.baidu.com");

        ClickButton button31 = new ClickButton();
        button31.setName("扫一扫");
        button31.setType("scancode_push");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("我在哪？");
        button32.setType("location_select");
        button32.setKey("32");

        Button button = new Button();
        button.setName("菜单");
        button.setSub_button(new Button[]{button31, button32});

        menu.setButton(new Button[]{button11, button21, button});


        return menu;
    }

    public static int createMenu(String token, String menu){
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, menu);

        if (!Objects.isNull(jsonObject)){
            result = jsonObject.getIntValue("errcode");
        }
        return result;
    }

    public static JSONObject queryMenu(String token){
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        return jsonObject;
    }

    public static int deleteMenu(String token){
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        int result = 0;

        if (!Objects.isNull(jsonObject)){
            result = jsonObject.getIntValue("errcode");
        }

        return result;
    }
}

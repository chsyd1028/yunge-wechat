package com.csyd.yungewechat.controller;

import com.csyd.yungewechat.po.TextMessage;
import com.csyd.yungewechat.util.MessageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by ChengShanyunduo
 * 2018/1/18
 */

@RestController
public class WxController {

    private Log logger = LogFactory.getLog(WxController.class);

    //此处TOKEN即我们刚刚所填的token
    private String TOKEN = "csyd1028";

    /**
     * 接收并校验四个请求参数
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return echostr
     */
    @RequestMapping(value = "/",method= RequestMethod.GET)
    public String checkName(@RequestParam(name="signature")String signature,
                            @RequestParam(name="timestamp")String timestamp,
                            @RequestParam(name="nonce")String nonce,
                            @RequestParam(name="echostr")String echostr){
        System.out.println("-----------------------开始校验------------------------");
        //排序
        String sortString = sort(TOKEN, timestamp, nonce);
        //加密
        String myString = sha1(sortString);
        //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            logger.info("----------->>>>>>>>>>>>签名校验成功");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            logger.info("----------->>>>>>>>>>>>签名校验失败");
            return "";
        }
    }

    /**
     * 解析微信端发送的xml
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/",method=RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Map<String, String> map = MessageUtil.xmlToMap(request);
            logger.info("\n + 收到信息------------------------------\n" + map.toString());
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            //保存返回的xml
            String message = null;

            //如果是text文本事件
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)){
                if ("1".equals(content) || "一".equals(content)){
                    message = MessageUtil.initText(toUserName, fromUserName, "你最大你最大。好了伐。");
                }else if ("2".equals(content) || "二".equals(content)){
                    message = MessageUtil.initNewsMessage(toUserName, fromUserName);
                }else if ("3".equals(content) || "三".equals(content)){
                    message = MessageUtil.initImageMessage(toUserName, fromUserName);
                }else if ("4".equals(content) || "四".equals(content)){
                    message = MessageUtil.initMusicMessage(toUserName, fromUserName);
                }else if ("?".equals(content) || "？".equals(content)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }else {
                    message = MessageUtil.initText(toUserName, fromUserName, "请审题请审题，脑子是个好东西哇。");
                }

            }
            //如果是事件推送
            else if (MessageUtil.MESSAGE_EVENT.equals(msgType)){
                String eventType = map.get("Event");

                //关注事件
                if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }
                //click菜单
                else if (MessageUtil.MESSAGE_CLICK.equals(eventType)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
                }
                //view菜单
                else if (MessageUtil.MESSAGE_VIEW.equals(eventType)){
                    String url = map.get("EventKey");
                    message = MessageUtil.initText(toUserName, fromUserName, url);
                }
                //扫描事件
                else if (MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
                    String key = map.get("EventKey");
                    message = MessageUtil.initText(toUserName, fromUserName, key);
                }
            }
            //地理位置推送
            else if (MessageUtil.MESSAGE_LOCATION.equals(msgType)){
                String label = map.get("Label");
                message = MessageUtil.initText(toUserName, fromUserName, label);
            }

            logger.info("\n + 返回信息------------------------------\n" + message);
            out.print(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }



    }

    /**
     * 排序方法
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

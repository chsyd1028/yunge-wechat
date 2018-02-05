package com.csyd.yungewechat.util;

import com.csyd.yungewechat.po.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by ChengShanyunduo
 * 2018/1/19
 */
public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_MUSIC = "music";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
    public static final String MESSAGE_SCANCODE = "scancode_push";


    /**
     * xml转为map集合
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();

        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);

        Element root = doc.getRootElement();

        List<Element> list = root.elements();

        for (Element e : list){
            map.put(e.getName(), e.getText());
        }

        ins.close();
        return map;
    }

    /**
     * 将文本消息对象转为xml
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", textMessage.getClass());
        return xStream.toXML(textMessage);
    }

    /**
     * 图文消息转为xml
     * @param newsMessage
     * @return
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", newsMessage.getClass());
        xStream.alias("item", new News().getClass());
        return xStream.toXML(newsMessage);
    }

    /**
     * 图片消息转为xml
     * @param imageMessage
     * @return
     */
    public static String imageMessageToXml(ImageMessage imageMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", imageMessage.getClass());
        xStream.alias("item", new Image().getClass());
        return xStream.toXML(imageMessage);
    }

    /**
     * 音乐消息转为xml
     * @param musicMessage
     * @return
     */
    public static String musicMessageToXml(MusicMessage musicMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", musicMessage.getClass());
        xStream.alias("item", new Image().getClass());
        return xStream.toXML(musicMessage);
    }

    /*************************信息的组装*********************************

    /**
     * 拼接主菜单，（关注回复）
     * @return
     */
    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("你瞅啥， 选下面！\n");
        sb.append("1.我是1，还是一\n");
        sb.append("2.洛天依和乐正绫\n");
        sb.append("3.云朵朵是谁\n");
        sb.append("4.想听歌么，快选我\n\n");
        sb.append("还想找我？ 回复\"？\"");
        return sb.toString();
    }


    /**
     * 拼接消息
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    public static String initText(String toUserName, String fromUserName, String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(String.valueOf(new Date().getTime()));
        text.setContent(content);

        return MessageUtil.textMessageToXml(text);
    }

    /**
     * 图文消息组装
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initNewsMessage(String toUserName, String fromUserName){
        String message = null;

        List<News> newsList = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();

        News news = new News();
        news.setTitle("洛天依和乐正绫");
        news.setDescription("乐正绫：活力十足的16岁女初中生，乐器制造商和音乐大企业--乐正集团的大小姐。个性直来直去，不拘小节，一天有3/4的时间都在跑来跑去，那个精神头让男生也自叹不如。喜欢音乐和巨大的好捏的毛茸茸的东西。虽然看起来很大方，理所当然的，作为女生的绫偶尔也会有少女的烦恼。最近最大的烦恼之源是来自哥哥……？　某一天，与突然从天而降的少女相遇（这里所说的少女就是洛天依）\n" +
                "洛天依：\n" +
                "新人VOCALOID。能够敏锐地感受到他人感情，有点内向的少女。 对过去曾经在人类世界创造了历史的传说中的VOCALOID前辈们非常憧憬，也梦想着自己有朝一日能够成为用歌声为别人传递感动与幸福的歌姬，这样的她在某日突然获得了召唤，并且带着某个重要的任务，作为新的VOCALOID来到了人类的世界。\n" +
                "能够为了别人而流泪的温柔，以及无论经历多少挫折也绝不放弃的坚强少女\n" +
                "因为初到人类世界，完全不懂人类的语言，所以平常不能与人对话，只能通过歌唱来表达自己的感情。\n" +
                "因为原设定中体现了许多南方风格的特质，与北方风格的乐正绫正好形成了互补，组成了cp“南北组（北南组）”，七灵石最新的pv[1]也相当卖萌的出现了此cp。");
        news.setPicUrl("http://i1.hdslb.com/bfs/archive/e7fb875ef70082928f6ae383e2739a8977eb3ea3.jpg");
        news.setUrl("https://zhidao.baidu.com/question/501381156.html");

        newsList.add(news);

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(String.valueOf(new Date().getTime()));
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        message = newsMessageToXml(newsMessage);

        return message;
    }

    /**
     * 组装图片消息
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initImageMessage(String toUserName, String fromUserName){
        String message = null;
        Image image = new Image();
        image.setMediaId("jGuyyqeYA5FkPDL83G0ngxaIYpQeOsoCoNm5T-XkiChcRXP1I0o_o7_FH2Y8euMF");
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setMsgType(MESSAGE_IMAGE);
        imageMessage.setCreateTime(String.valueOf(new Date().getTime()));
        imageMessage.setImage(image);

        message = imageMessageToXml(imageMessage);

        return message;

    }

    /**
     * 组装音乐消息
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initMusicMessage(String toUserName, String fromUserName){
        String message = null;
        Music music = new Music();
        music.setThumbMediaId("uCTh3nVfjf879gErA0VEzmT5cmWiAKrN4lcIZZg7R8nv4t3Wm50Shv67MwF1qU4T");
        music.setTitle("还不是因为长得不好看");
        music.setDescription("洛天依");
        music.setMusicUrl("http://2e02d1b6.ngrok.io/还不是因为你长得不好看.mp3");
        music.setHQMusicUrl("http://2e02d1b6.ngrok.io/还不是因为你长得不好看.mp3");

        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setCreateTime(String.valueOf(new Date().getTime()));
        musicMessage.setMusic(music);

        message = musicMessageToXml(musicMessage);

        return message;

    }


}

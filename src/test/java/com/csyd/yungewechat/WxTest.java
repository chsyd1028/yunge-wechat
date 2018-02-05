package com.csyd.yungewechat;

import com.alibaba.fastjson.JSONObject;
import com.csyd.yungewechat.po.AccessToken;
import com.csyd.yungewechat.util.WxUtil;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by ChengShanyunduo
 * 2018/1/24
 */



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Bootstrap.class)
@ActiveProfiles("test")
public class WxTest {

    @Test
    public void testWx() {

        Log logger = LogFactory.getLog(WxTest.class);

        AccessToken token = WxUtil.getAccessToken();
        System.out.println("票据：" + token.getToken());
        System.out.println("有效时间" + token.getExpiresIn());


            //上传素材测试
//            String path = "/Users/chengshanyunduo/Pictures/桌面壁纸/洛天依/timg.jpeg";
//            String mediaId = WxUtil.upload(path, token.getToken(), "thumb");
//            System.out.println(mediaId);


            //创建菜单测试
//            String menu = JSONObject.toJSON(WxUtil.initMenu()).toString();
//            logger.info(menu);
//            int result = WxUtil.createMenu(token.getToken(), menu);
//
//            if (result == 0){
//                logger.info("----------------->>>>>>>>创建菜单成功");
//            }else {
//                logger.info("----------------->>>>>>>>创建菜单失败，错误码：" + result);
//            }

        //查询菜单测试
        JSONObject jsonObject = WxUtil.queryMenu(token.getToken());
        logger.info(jsonObject);

        //删除菜单测试
        int result = WxUtil.deleteMenu(token.getToken());
        if (result == 0){
            logger.info("--------->>>>>>>删除成功");
        }else{
            logger.info("----------->>>>删除失败错误码：" + result);
        }

    }
}

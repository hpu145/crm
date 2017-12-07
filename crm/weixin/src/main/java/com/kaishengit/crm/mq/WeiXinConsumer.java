package com.kaishengit.crm.mq;

import com.alibaba.fastjson.JSON;
import com.kaishengit.crm.util.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 2017/11/23.
 */
@Component
public class WeiXinConsumer {

    @Autowired
    private WeiXinUtil weiXinUtil;

    @JmsListener(destination = "weixinMessageQueue")
    public void sendMessageToUser(String json) {
        Map<String,Object> map = JSON.parseObject(json, HashMap.class);
        weiXinUtil.sendTextMessage(Arrays.asList(1,2),map.get("message").toString());
    }

}





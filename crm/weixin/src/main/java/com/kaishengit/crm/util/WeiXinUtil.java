package com.kaishengit.crm.util;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kaishengit.crm.exception.WeiXinException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyu on 2017/11/21.
 */
@Component
public class WeiXinUtil {

    //将配置文件中的corpid corpsecret注入进来
    @Value("${weixin.agentid}")
    private String agentId;
    @Value("${weixin.corpid}")
    private String corpId;
    @Value("${weixin.corpsecret}")
    private String corpSecret;
    @Value("${weixin.contact.secret}")
    private String contactSecret;

    //在getAccessToken方法中传入要获取的类型这个参数
    public static final String ACCESS_TOKEN_TYPE_NORMAL = "normal";
    public static final String ACCESS_TOKEN_TYPE_CONTACTS = "contacts";

    //获取accessToken的url
    private static final String GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    //创建部门的url
    private static final String POST_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=%s";
    //删除部门的url
    private static final String GET_DEPT_DEL_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";
    //创建成员
    private static final String CREATE_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";
    //删除成员
    private static final String DEL_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=%s&userid=%s";
    //发送消息的url
    private static final String POST_SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";


    /**
     * AccessToken的缓存
     */
    private LoadingCache<String,String> accessTokenCache = CacheBuilder.newBuilder()
            .expireAfterAccess(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String type) throws Exception {
                    //判断accessToken的类型 normal还是通讯录contactas
                    String url = "";
                    if (ACCESS_TOKEN_TYPE_NORMAL.equals(type)) {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpId,corpSecret);
                    } else {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpId,contactSecret);
                    }

                    String resultJson = sendHttpGetRequest(url);
                    Map<String,Object> map = JSON.parseObject(resultJson, HashMap.class);
                    if (map.get("errcode").equals(0)) {
                        return (String) map.get("access_token");
                    }
                    throw new WeiXinException(resultJson);
                }
            });


    /**
     * 获取AccessToken的类型  normal  contacts
     * @param type
     * @return
     */
    public String getAccessToken(String type) {
        try {
            return accessTokenCache.get(type);
        } catch (ExecutionException e) {
            throw new RuntimeException("获取AccessToken异常",e);
        }
    }


    /**
     * 发出HTTP的GET请求
     * @param url 请求的url地址
     * @return
     */
    private String sendHttpGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("HTTP/GET请求出现异常",e);
        }
    }

    /**
     *发出HTTP的POST请求
     * @param url
     * @param json
     * @return
     */
    private String sendHttpPostRequest(String url,String json) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("HTTP/POST请求出现异常",e);
        }
    }


    /**
     * 创建部门
     * @param id 部门id
     * @param parentId 父部门id
     * @param name 部门名称
     */
    public void createDept(Integer id,Integer parentId,String name) {
        //获取accessToken
        String url = String.format(POST_DEPT_URL,getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS));

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("parentid",parentId);
        map.put("id",id);
        map.put("name",name);
        String resultJson = sendHttpPostRequest(url,JSON.toJSONString(map));
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeiXinException("创建部门失败 :" + resultJson);
        }

    }


    /**
     * 删除部门
     * @param id 部门id。
     *  不能删除根部门；不能删除含有子部门、成员的部门
     */
    public void delDept(Integer id) {
        String url = String.format(GET_DEPT_DEL_URL,getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS),id);

        String resultJson = sendHttpGetRequest(url);
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeiXinException("删除部门失败 :" + resultJson);
        }
    }

    /**
     * 创建成员
     * @param userId  成员UserID
     * @param name  成员名称
     * @param mobile  手机号码
     * @param deptIdList  成员所属部门id列表,不超过20个
     */
    public void createUser(Integer userId, String name,String mobile,List<Integer> deptIdList) {
        String url = String.format(CREATE_USER_URL,getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS));
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userid",userId);
        map.put("name",name);
        map.put("mobile",mobile);
        map.put("department",deptIdList);

        String resultJson = sendHttpPostRequest(url,JSON.toJSONString(map));
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeiXinException("创建成员失败 :" + resultJson);
        }
    }


    /**
     * 删除成员
     * @param id
     */
    public void delUser(Integer id) {
        String url = String.format(DEL_USER_URL,getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS),id);
        String resultJson = sendHttpGetRequest(url);
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeiXinException("删除成员失败 :" + resultJson);
        }
    }


    /**
     * 发送文本消息
     * @param userIdList
     * @param message
     */
    public void sendTextMessage(List<Integer> userIdList,String message) {
        String url = String.format(POST_SEND_MESSAGE_URL,getAccessToken(ACCESS_TOKEN_TYPE_NORMAL));

        StringBuilder stringBuilder = new StringBuilder();
        for (Integer userId : userIdList) {
            stringBuilder.append(userId).append("|");
        }
        String idStr = stringBuilder.toString();
        idStr = idStr.substring(0,idStr.lastIndexOf("|"));

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("msgtype","text");
        map.put("agentid",agentId);
        map.put("touser",idStr);
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put("content", message);
        map.put("text",messageMap);

        String resultJson = sendHttpPostRequest(url,JSON.toJSONString(map));
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeiXinException("发送文本消息失败 :" + resultJson);
        }



    }



}

import com.kaishengit.crm.util.WeiXinUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Created by zhangyu on 2017/11/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-weixin.xml")
public class WeiXinTest {

    @Autowired
    private WeiXinUtil weiXinUtil;

    @Test
    public void getAccessToken() {
        String accessToken = weiXinUtil.getAccessToken(WeiXinUtil.ACCESS_TOKEN_TYPE_CONTACTS);
        System.out.println(accessToken);
    }


    @Test
    public void createDept() {
        weiXinUtil.createDept(12,11,"审计部");
    }

    @Test
    public void delDept() {
        weiXinUtil.delDept(11);
    }

    @Test
    public void ceeateUser() {
        weiXinUtil.createUser(2,"韩梅梅","18595736798", Arrays.asList(3));
    }

    @Test
    public void delUser() {
        weiXinUtil.delUser(1);
    }

    @Test
    public void sendTextMessageToUser() {
        weiXinUtil.sendTextMessage(Arrays.asList(100,109,108),"你有一个待办任务需要今天完成，请点击<a href=\"http://www.kaishengit.com\">链接</a>查看");
    }



}

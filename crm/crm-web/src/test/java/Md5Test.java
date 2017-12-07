import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * Created by zhangyu on 2017/11/24.
 */
public class Md5Test {


    @Test
    public void md5() {
        String salt = "shaahldiwqqwi1w21hSKAD";

        System.out.println(DigestUtils.md5Hex("000000"+salt));
    }

}

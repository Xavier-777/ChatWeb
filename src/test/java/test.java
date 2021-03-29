import com.mei.enums.MessageType;
import com.mei.utils.AgoraUtils;
import com.mei.utils.FileUtils;
import org.junit.Test;

import java.util.UUID;

public class test {

    @Test
    public void test(){
        String token = AgoraUtils.getRtcToken("100");
        System.out.println(token);
    }

    @Test
    public void getUUID(){
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(s);
    }

}

package dynomite;

import com.netflix.dyno.jedis.DynoJedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:richie.yan@happyelements.com">richie.yan</a>
 * @date 8/23/16 10:58 AM
 */
@RestController
public class WebController {
    @Autowired
    private DynoJedisClient jedisClient;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(@RequestParam("key") String key){
        return jedisClient.get(key);
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public String set(@RequestParam("key") String key,@RequestParam("value") String value){
        jedisClient.set(key,value);
        return key + "=" + value;
    }


}

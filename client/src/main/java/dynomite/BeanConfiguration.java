package dynomite;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.HostConnectionPool;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.exception.DynoException;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.lb.HostToken;
import com.netflix.dyno.connectionpool.impl.utils.CollectionUtils;
import com.netflix.dyno.jedis.DynoJedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author <a href="mailto:richie.yan@happyelements.com">richie.yan</a>
 * @date 8/23/16 10:56 AM
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public DynoJedisClient dynoJedisClient(){
        final HostSupplier customHostSupplier = new HostSupplier() {
            final List<Host> hosts = new ArrayList<Host>();
            String host = "10.130.138.47";

            @Override
            public Collection<Host> getHosts() {
                //notice: port will be modified by connection pool configuration(CPConfig)
                //default value is 8102
                hosts.add(new Host(host, 8102, Host.Status.Up).setRack("rack1"));
                return hosts;
            }
        };

        //customize a TokenMapSupplier
        TokenMapSupplier testTokenMapSupplier = new TokenMapSupplier() {

            @Override
            public List<HostToken> getTokens(Set<Host> activeHosts) {
                Set<HostToken> allTokens = new HashSet<HostToken>();
                int i = 0;
                for (Host host : activeHosts) {//关联token和host
                    long token  = (4294967295L/3)*i;
                    allTokens.add(new HostToken(token,host));
                    i++;
                }
                return new ArrayList<HostToken>(allTokens);
            }

            @Override
            public HostToken getTokenForHost(Host host, Set<Host> activeHosts) {
                List<HostToken>  hostTokens = getTokens(activeHosts);
                return CollectionUtils.find(hostTokens, new CollectionUtils.Predicate<HostToken>() {
                    @Override
                    public boolean apply(HostToken x) {
                        return x.getHost().getHostName().equals(host.getHostName());
                    }
                });
            }
        };
        ConnectionPoolConfigurationImpl cpConfig = new ConnectionPoolConfigurationImpl("TestDemo"){
            @Override
            public ConnectionPoolConfigurationImpl withTokenSupplier(TokenMapSupplier tSupplier) {
                return super.withTokenSupplier(testTokenMapSupplier);
            }
        };
        return new DynoJedisClient.Builder()
                .withApplicationName("TestDemo")
                .withDynomiteClusterName("LocalCluster")
                .withHostSupplier(customHostSupplier)
                .withCPConfig(cpConfig)
                .build();
    }
}

package properties;

@Data
@ConfigurationProperties(prefix = "jieApi.gateway.filter")
public class CloudSecurityProperties {
 
 
    /**
     * 是否只能通过网关获取资源
     * 默认为True
     */
    private Boolean onlyFetchByGateway = Boolean.TRUE;
 
 
}
 
 
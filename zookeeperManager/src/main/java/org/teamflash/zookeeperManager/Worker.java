package org.teamflash.zookeeperManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
        import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;
import org.apache.commons.io.IOUtils;

        import javax.ws.rs.GET;
        import javax.ws.rs.Path;
        import java.io.InputStream;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;
/**
 * Created by girish on 11/17/16.
 */
@Path("/registerWorker")
public class Worker {

    static ServiceProvider<Void> serviceProvider;
    static String[] urlEndpoints = {"http://52.52.165.77","http://52.52.164.169"};
    @GET
    @Path("/dataIngestor")
    public static String initializeClusterCurator() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();
        //System.out.println("after curator framework start");
        int port = 8765;

        for(int i=0;i<urlEndpoints.length;i++)
        {
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(urlEndpoints[i])
                    .port(port)
                    .name("worker")
                    .build();
            System.out.println("I am inside new function");
            ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("DataIngestor")
                    .client(curatorFramework)
                    .thisInstance(serviceInstance)
                    .build()
                    .start();

        }

        return "data Ingestor success";
    }



}

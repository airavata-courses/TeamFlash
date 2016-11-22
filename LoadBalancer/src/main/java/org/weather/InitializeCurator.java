package org.weather;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
/**
 * Created by girish on 11/21/16.
 */
@WebListener
public class InitializeCurator implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent servletContext)
    {
        ServiceProvider<Void>  dataIngestorServiceProvider;
        ServiceProvider<Void> stormDetectionServiceProvider;
        ServiceProvider<Void>  stormClusterServiceProvider;
        ServiceProvider<Void> forecastTriggerServiceProvider;
        ServiceProvider<Void> runForecastServiceProvider;

        CuratorFramework curatorFramework=null;
        try
        {
            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

            curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

            curatorFramework.start();

            /*for service Data Ingestor*/
            ServiceDiscovery<Void> dataIngestorServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("DataIngestor")
                    .client(curatorFramework).build();

            dataIngestorServiceDiscovery.start();

            dataIngestorServiceProvider = dataIngestorServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            dataIngestorServiceProvider.start();

            /* for service storm cluster*/
            ServiceDiscovery<Void> stormClusterServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("StormClustering")
                    .client(curatorFramework).build();

            stormClusterServiceDiscovery.start();

            stormClusterServiceProvider = stormClusterServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            stormClusterServiceProvider.start();

            /*for service forecast trigger*/
            ServiceDiscovery<Void> forecastTriggerServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("ForecastTrigger")
                    .client(curatorFramework).build();

            forecastTriggerServiceDiscovery.start();

            forecastTriggerServiceProvider = forecastTriggerServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            forecastTriggerServiceProvider.start();

            /* for service Run Forecast*/
            ServiceDiscovery<Void> runForecastServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("RunForecast")
                    .client(curatorFramework).build();

            runForecastServiceDiscovery.start();

            runForecastServiceProvider = runForecastServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            runForecastServiceProvider.start();

            /* for service Storm Detection*/
            ServiceDiscovery<Void> stormDetectionServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("StormDetection")
                    .client(curatorFramework).build();

            stormDetectionServiceDiscovery.start();

            stormDetectionServiceProvider = stormDetectionServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            stormDetectionServiceProvider.start();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            curatorFramework.close();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContext)
    {
        System.out.println("After exit");
    }



}

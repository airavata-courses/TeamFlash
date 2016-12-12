package org.weather;

import javax.ws.rs.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.commons.io.IOUtils;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import org.apache.curator.test.KillSession;
import org.apache.zookeeper.ZooKeeper;

@Path("/")
public class Manager {

    ServiceProvider<Void>  dataIngestorServiceProvider;
    ServiceProvider<Void> stormDetectionServiceProvider;
    ServiceProvider<Void>  stormClusterServiceProvider;
    ServiceProvider<Void> forecastTriggerServiceProvider;
    ServiceProvider<Void> runForecastServiceProvider;

    @GET
    @Path("/dataIngestor")
    public String delegate(@QueryParam("username") String username, @QueryParam("id") String id, @QueryParam("date") String date
            ,@QueryParam("time") String time, @QueryParam("station") String station,
                           @QueryParam("msvc") String msvc) throws Exception {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        String hosts = "52.52.144.190:2181,52.52.165.77:2181,52.9.64.108:2181";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start();
        /*for service Storm Detectionr*/
        ServiceDiscovery<Void> dataIngestorServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("DataIngestor")
                .client(curatorFramework).build();

        dataIngestorServiceDiscovery.start();
        //ServiceProvider<Void>  dataIngestorServiceProvider;
       String response = "NONE";
        try {
            dataIngestorServiceProvider = dataIngestorServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            dataIngestorServiceProvider.start();
            ServiceInstance<Void> instance;
            instance = dataIngestorServiceProvider.getInstance();
            if(instance==null){
                return "instance is null";
            }
            String address = instance.buildUriSpec();

            if(address==null){
                return "address not found";
            }

            String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        /*String username = "debasisdwivedy";
        String id = "dfb3b368-3430-4f14-ab1c-231c2e93cf41";
        String date = "1999-04-05";
        String time = "000408";*/
            //station = "KAMX";
            msvc = "Data Ingestor";

            String query = String.format("username=%s&id=%s&date=%s&time=%s&station=%s&msvc=%s",
                    URLEncoder.encode(username, charset),
                    URLEncoder.encode(id, charset),
                    URLEncoder.encode(date, charset),
                    URLEncoder.encode(time, charset),
                    URLEncoder.encode(station, charset),
                    URLEncoder.encode(msvc, charset)
            );

            //String response = (address + "/StormExists").toURL().getText();
            URL url = new URL(address + "/data?"+query);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept-Charset", charset);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            response = IOUtils.toString(inputStream, charset);
            inputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if (dataIngestorServiceDiscovery != null){
                dataIngestorServiceProvider.close();
            }
            if (dataIngestorServiceDiscovery != null){
                dataIngestorServiceDiscovery.close();
            }

            curatorFramework.close();
            return response ;
        }
    }

    @GET
    @Path("/detectClusters")
    public String stormClusterDelegate() throws Exception {

        /**/
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        String hosts = "52.52.144.190:2181,52.52.165.77:2181,52.9.64.108:2181";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start();
        /*for service Storm Detectionr*/
        ServiceDiscovery<Void> stormClusteringrServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("StormClustering")
                .client(curatorFramework).build();

        stormClusteringrServiceDiscovery.start();

        stormClusterServiceProvider = stormClusteringrServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        stormClusterServiceProvider.start();


        ServiceInstance<Void> instance;
        instance = stormClusterServiceProvider.getInstance();
        if (instance == null) {
            return "instance is null";
        }
        String address = instance.buildUriSpec();

        if (address == null) {
            return "address not found";
        }
        String charset = "UTF-8";
        URL url = new URL(address);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept-Charset", charset);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = IOUtils.toString(inputStream, charset);
        inputStream.close();
//        try {
//            KillSession.kill(curatorFramework.getZookeeperClient().getZooKeeper(), hosts);
//        }catch (Exception e) {
//            System.out.println("Error - " + e.getMessage());
//        }
        stormClusteringrServiceDiscovery.close();
        stormClusterServiceProvider.close();
        curatorFramework.close();
        return response;
    }

    @GET
    @Path("/detectStorm")
    public String detectStormDelegate(@QueryParam("url") String url) throws Exception {

        /**/
        CuratorFramework curatorFramework=null;
        String response="";
        try
        {
            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
            String hosts = "52.52.144.190:2181,52.52.165.77:2181,52.9.64.108:2181";
            curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
            curatorFramework.start();
        /*for service Storm Detectionr*/
            ServiceDiscovery<Void> stormDetectionrServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("StormDetection")
                    .client(curatorFramework).build();

            stormDetectionrServiceDiscovery.start();

            stormDetectionServiceProvider = stormDetectionrServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            stormDetectionServiceProvider.start();


            ServiceInstance<Void> instance;
            instance = stormDetectionServiceProvider.getInstance();
            if (instance == null) {
                return "instance is null";
            }
            String address = instance.buildUriSpec();

            if (address == null) {
                return "address not found";
            }
            String charset = "UTF-8";
            String query = String.format("url%s",URLEncoder.encode(url, charset));

            URL url1= new URL(address + "?"+query);
            URLConnection conn = url1.openConnection();
            conn.setRequestProperty("Accept-Charset", charset);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            response = IOUtils.toString(inputStream, charset);
            inputStream.close();
            //stormDetectionrServiceDiscovery.close();
            //stormDetectionServiceProvider.close();
//            try {
//                KillSession.kill(curatorFramework.getZookeeperClient().getZooKeeper(), hosts);
//            }catch (Exception e) {
//                System.out.println("Error - " + e.getMessage());
//            }

        }
        catch(Exception e)
        {
               e.printStackTrace();
        }
        finally
        {
            curatorFramework.close();
        }

        return response;
    }

    @GET
    @Path("/forecastTrigger")
    public  String forecastTriggerDelegate(@QueryParam("value") boolean exists) throws Exception {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(Config.get().get("zk.quorum"),Config.get().getInt("zk.session.timeout", 3000),1000,retryPolicy);
        String hosts = "52.52.144.190:2181,52.52.165.77:2181,52.9.64.108:2181";
        //List<String> hosts = Arrays.asList("52.52.144.190:2181", "52.52.165.77:2181", "52.52.164.169:2181");
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181",retryPolicy);
        curatorFramework.start();
        /*for service forecast trigger*/
        ServiceDiscovery<Void> forecastTriggerServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("ForecastTrigger")
                .client(curatorFramework).build();

        forecastTriggerServiceDiscovery.start();

        while (true){
            forecastTriggerServiceProvider = forecastTriggerServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            if (forecastTriggerServiceProvider != null) {
                forecastTriggerServiceProvider.start();
                break;
            }
        }

        ServiceInstance<Void> instance;
        instance = forecastTriggerServiceProvider.getInstance();
        
        if (instance == null) {
            return "instance is null";
        }
        String address = instance.buildUriSpec();
        String URLvalue = exists?"yes":"no";
        if (address == null) {
            return "address not found";
        }
        String charset = "UTF-8";

        String query = String.format("value=%s",URLEncoder.encode(URLvalue, charset));


        URL url = new URL(address + "/StormExists/api/verify?"+query);
        
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept-Charset", charset);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = IOUtils.toString(inputStream, charset);
        inputStream.close();
//        try {
//            KillSession.kill(curatorFramework.getZookeeperClient().getZooKeeper(), hosts);
//        }catch (Exception e) {
//            System.out.println("Error - " + e.getMessage());
//        }
//        forecastTriggerServiceDiscovery.close();
//        forecastTriggerServiceProvider.close();
//        curatorFramework.close();
        return response;
    }

    @GET
    @Path("/runForecast")
    public String runForecastDelegate(@QueryParam("location") String locationName,
                                      @QueryParam("username") String userName,
                                      @QueryParam("jobId") String jobId)
            throws Exception {
        String hosts = "52.52.144.190:2181,52.52.165.77:2181,52.9.64.108:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start(); 
        ServiceDiscovery<Void> runForecastServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("RunForecast")
                .client(curatorFramework).build();

        runForecastServiceDiscovery.start();


        while (true){
            runForecastServiceProvider = runForecastServiceDiscovery
                    .serviceProviderBuilder()
                    .serviceName("worker").build();
            if (runForecastServiceDiscovery != null){
                runForecastServiceProvider.start();
                break;
            }

        }

        ServiceInstance<Void> instance;
        instance = runForecastServiceProvider.getInstance();
        if (instance == null) {
            return "instance is null";
        }
        String address = instance.buildUriSpec();

        if (address == null) {
            return "address not found";
        }
        String charset = "UTF-8";
        String query = String.format("location=%s&jobId=%s&username=%s",
                URLEncoder.encode(locationName, charset),
                URLEncoder.encode(jobId,charset),
                URLEncoder.encode(userName, charset)

        );

        URL url = new URL(address + "/RunForecast/api/run?"+query);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept-Charset", charset);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = IOUtils.toString(inputStream, charset);
        inputStream.close();
//        try {
//            KillSession.kill(curatorFramework.getZookeeperClient().getZooKeeper(), hosts);
//        }catch (Exception e) {
//            System.out.println("Error - " + e.getMessage());
//        }
//        curatorFramework.close();
//        runForecastServiceDiscovery.close();
//        runForecastServiceProvider.close();
        return response;
    }


}

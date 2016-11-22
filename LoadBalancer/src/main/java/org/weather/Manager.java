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
/**
 * Created by girish on 9/18/16.
 */
@Path("/")
public class Manager {

    ServiceProvider<Void>  dataIngestorServiceProvider;
    ServiceProvider<Void> stormDetectionServiceProvider;
    ServiceProvider<Void>  stormClusterServiceProvider;
    ServiceProvider<Void> forecastTriggerServiceProvider;
    ServiceProvider<Void> runForecastServiceProvider;

    /*@GET
    public static String initializeCurator() throws Exception {

        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();
        //System.out.println("after curator framework start");

        *//*for service Data Ingestor*//*
        ServiceDiscovery<Void> dataIngestorServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("DataIngestor")
                .client(curatorFramework).build();

        dataIngestorServiceDiscovery.start();

        dataIngestorServiceProvider = dataIngestorServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        dataIngestorServiceProvider.start();

        *//* for service storm cluster*//*
        ServiceDiscovery<Void> stormClusterServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("StormClustering")
                .client(curatorFramework).build();

        stormClusterServiceDiscovery.start();

        stormClusterServiceProvider = stormClusterServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        stormClusterServiceProvider.start();

        *//*for service forecast trigger*//*
        ServiceDiscovery<Void> forecastTriggerServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("ForecastTrigger")
                .client(curatorFramework).build();

        forecastTriggerServiceDiscovery.start();

        forecastTriggerServiceProvider = forecastTriggerServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        forecastTriggerServiceProvider.start();

        *//* for service Run Forecast*//*
        ServiceDiscovery<Void> runForecastServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("RunForecast")
                .client(curatorFramework).build();

        runForecastServiceDiscovery.start();

        runForecastServiceProvider = runForecastServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        runForecastServiceProvider.start();

        *//**//*
        ServiceDiscovery<Void> stormDetectionServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("StormDetection")
                .client(curatorFramework).build();

        stormDetectionServiceDiscovery.start();

        stormDetectionServiceProvider = stormDetectionServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        stormDetectionServiceProvider.start();

        return "done";
    }*/

    @GET
    @Path("/dataIngestor")
    public String delegate(@QueryParam("username") String username, @QueryParam("id") String id, @QueryParam("date") String date
            ,@QueryParam("time") String time, @QueryParam("station") String station,
                           @QueryParam("msvc") String msvc) throws Exception {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start();
        /*for service Storm Detectionr*/
        ServiceDiscovery<Void> dataIngestorServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("DataIngestor")
                .client(curatorFramework).build();

        dataIngestorServiceDiscovery.start();
        ServiceProvider<Void>  dataIngestorServiceProvider;
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
        String response = IOUtils.toString(inputStream, charset);
        inputStream.close();
        curatorFramework.close();
        return response;
    }

    @GET
    @Path("/detectClusters")
    public String stormClusterDelegate() throws Exception {

        /**/
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
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
        URL url = new URL(address + "/detectClusters");
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept-Charset", charset);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = IOUtils.toString(inputStream, charset);
        inputStream.close();
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

            URL url1= new URL(address + "/detectStorm?"+query);
            URLConnection conn = url1.openConnection();
            conn.setRequestProperty("Accept-Charset", charset);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            response = IOUtils.toString(inputStream, charset);
            inputStream.close();

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
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("52.52.144.190:2181",retryPolicy);
        curatorFramework.start();
        /*for service forecast trigger*/
        ServiceDiscovery<Void> forecastTriggerServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("ForecastTrigger")
                .client(curatorFramework).build();

        forecastTriggerServiceDiscovery.start();

        forecastTriggerServiceProvider = forecastTriggerServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        forecastTriggerServiceProvider.start();
        
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
        //curatorFramework.close();
        return response;
    }

    @GET
    @Path("/runForecast")
    public String runForecastDelegate(@QueryParam("location") String locationName) throws Exception {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start(); 
        ServiceDiscovery<Void> runForecastServiceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .basePath("RunForecast")
                .client(curatorFramework).build();

        runForecastServiceDiscovery.start();

        runForecastServiceProvider = runForecastServiceDiscovery
                .serviceProviderBuilder()
                .serviceName("worker").build();
        runForecastServiceProvider.start();
        
        
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
        String query = String.format("location=%s",
                URLEncoder.encode(locationName, charset)
        );

        URL url = new URL(address + "/RunForecast/api/run?"+query);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept-Charset", charset);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        String response = IOUtils.toString(inputStream, charset);
        inputStream.close();
        curatorFramework.close();
        return response;
    }


}

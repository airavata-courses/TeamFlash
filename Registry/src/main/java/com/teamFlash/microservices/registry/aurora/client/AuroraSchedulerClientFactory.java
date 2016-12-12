package com.teamFlash.microservices.registry.aurora.client;
import com.teamFlash.microservices.registry.aurora.client.sdk.AuroraSchedulerManager;
import com.teamFlash.microservices.registry.aurora.client.sdk.ReadOnlyScheduler;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

/**
 * Created by girish on 12/10/16.
 */
public class AuroraSchedulerClientFactory {

    //private final static Logger logger = LoggerFactory.getLogger(AuroraSchedulerClientFactory.class);


    static TTransport transport;
    static TProtocol protocol;

    public static ReadOnlyScheduler.Client createReadOnlySchedulerClient(String connectionUrl) throws Exception {
        try {
            transport = new THttpClient(connectionUrl);
            transport.open();
            protocol = new TJSONProtocol(transport);

        } catch(Exception ex) {
            /*logger.error(ex.getMessage(), ex);
            throw ex;*/
            ex.printStackTrace();
        }
        return new ReadOnlyScheduler.Client(protocol);
    }

    public static AuroraSchedulerManager.Client createSchedulerManagerClient(String connectionUrl) throws Exception {
        try {
            transport = new THttpClient(connectionUrl);
            transport.open();
            protocol = new TJSONProtocol(transport);

        } catch(Exception ex) {
            /*logger.error(ex.getMessage(), ex);
            throw ex;*/
            ex.printStackTrace();
        }
        return new AuroraSchedulerManager.Client(protocol);
    }

}

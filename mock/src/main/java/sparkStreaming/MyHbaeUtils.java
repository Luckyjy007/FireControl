package sparkStreaming;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/*
Create by jiangyun on 2017/12/14
*/
public class MyHbaeUtils {

    public static void creteTable(String tableNmae, String[] cfs) {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "cors-01,cors-02,cors-03");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");

        Connection connection = null;
        Admin admin = null;

        try {
            connection = ConnectionFactory.createConnection();
            admin = (HBaseAdmin)connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

}

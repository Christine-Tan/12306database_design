import DataImpl.PurchaseTicketImpl;
import org.junit.Before;
import org.junit.Test;
import util.Constant;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Seven on 2016/11/11.
 */
public class PurchaseTicketImplTest {
    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void purchaseTicket() throws Exception {


        ExecutorService exec= Executors.newCachedThreadPool();
        for(int i=0;i<200;i++) {
            exec.execute(new Runnable() {
                public void run() {
                        System.out.println("begin");
                        Connection connection = Constant.connector.getMySqlConnection();
                        PurchaseTicketImpl purchaseTicket=new PurchaseTicketImpl(connection);
                        purchaseTicket.purchaseTicket("420602199605162024", "二等座", "2016-11-12", 16, "南京南", "北京南");
                        System.out.println("end");


                }
            });
        }
        exec.shutdown();
    }
}

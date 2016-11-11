package DataImpl;

import util.Constant;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Seven on 2016/11/10.
 */
public class Main {

    /**
     *initial all the tables
     **/
    private void initialTables(Connection connection) throws SQLException, ClassNotFoundException {
                 InitialData initialData=new InitialData();
                 initialData.initial(connection);
                 initialData.initialRoute();
                 initialData.initialTimetable();
                 initialData.initialCarriage();
                 initialData.initialSeatBusiness();
                 initialData.initialSeatFirst();
                 initialData.initialSeatSecond();
                 initialData.initialTicketPrice();
                 initialData.initialUser();
                 initialData.initialTrain();
                 initialData.initialRemainTicket();
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = Constant.connector.getMySqlConnection();
        Main main=new Main();
//        main.initialTables(connection);
        PurchaseTicketImpl purchaseTicket=new PurchaseTicketImpl(connection);
//        purchaseTicket.getFrequency("2016-11-09","南京南","济南西");
//        purchaseTicket.getRemainTicket("2016-11-09",16,"南京南","济南西");
//        purchaseTicket.ticketAvailable("2016-11-09",16,"南京南","济南西","second_remain");
        purchaseTicket.purchaseTicket("420602199605162024","二等座","2016-11-11",16,"南京南","北京南");
    }

}

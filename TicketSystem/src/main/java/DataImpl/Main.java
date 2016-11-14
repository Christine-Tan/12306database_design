package DataImpl;

import com.mongodb.client.MongoDatabase;
import util.Constant;

import java.sql.Connection;

/**
 * Created by Seven on 2016/11/10.
 */
public class Main {

    /**
     *initial all the tables
     **/
    private void initialMysqlTables(Connection connection){
        InitialData initialData=new InitialData(connection);

        long startTime=System.currentTimeMillis();   //获取开始时间
        initialData.initialRoute();
        long endTime= System.currentTimeMillis();
        System.out.println("Mysql Initial Route takes "+(endTime-startTime)+"ms");

        initialData.initialTimetable();
        startTime=System.currentTimeMillis();
        System.out.println("Mysql Initial Timetable takes "+(startTime-endTime)+"ms");

        initialData.initialCarriage();
        endTime= System.currentTimeMillis();
        System.out.println("Mysql Initial Carriage takes "+(endTime-startTime)+"ms");

        initialData.initialSeatBusiness();
        startTime=System.currentTimeMillis();
        System.out.println("Mysql Initial Business takes "+(startTime-endTime)+"ms");

        initialData.initialSeatFirst();
        endTime= System.currentTimeMillis();
        System.out.println("Mysql Initial SeatFirst takes "+(endTime-startTime)+"ms");

        initialData.initialSeatSecond();
        startTime=System.currentTimeMillis();
        System.out.println("Mysql Initial SeatSecond takes "+(startTime-endTime)+"ms");

        initialData.initialTicketPrice();
        endTime= System.currentTimeMillis();
        System.out.println("Mysql Initial TicketPrice takes "+(endTime-startTime)+"ms");

        initialData.initialUser();
        startTime=System.currentTimeMillis();
        System.out.println("Mysql Initial User takes "+(startTime-endTime)+"ms");

        initialData.initialTrain();
        endTime= System.currentTimeMillis();
        System.out.println("Mysql Initial Train takes "+(endTime-startTime)+"ms");

        initialData.initialRemainTicket();
         startTime=System.currentTimeMillis();
        System.out.println("Mysql Initial RemainTicket takes "+(startTime-endTime)+"ms");
    }

    private void initialMongoDBCollections(MongoDatabase database){

        InitialCollection initialCollection=new InitialCollection(database);
//        initialCollection.createCollections();

        long startTime=System.currentTimeMillis();
        initialCollection.initialRoute();
        long endTime= System.currentTimeMillis();
        System.out.println("MongoDB Initial Route takes "+(endTime-startTime)+"ms");

        initialCollection.initialTrain();
        startTime= System.currentTimeMillis();
        System.out.println("MongoDB Initial Train takes "+(startTime-endTime)+"ms");

        initialCollection.initialTimeTable();
        endTime= System.currentTimeMillis();
        System.out.println("MongoDB Initial TimeTable takes "+(endTime-startTime)+"ms");

//        initialCollection.initialRemainTicket();
        startTime= System.currentTimeMillis();
        System.out.println("MongoDB Initial RemainTicket takes "+(startTime-endTime)+"ms");


    }

    private void connectMySQL(){
        Connection connection = Constant.connector.getMySqlConnection();
        this.initialMysqlTables(connection);
        PurchaseTicketImpl purchaseTicket=new PurchaseTicketImpl(connection);
        purchaseTicket.purchaseTicket("420602199605162024","二等座","2016-11-12",16,"南京南","北京南");

        //测试高并发
//        final long startTime=System.currentTimeMillis();   //获取开始时间
//        //测试的代码段
//        final Connection connection = Constant.connector.getMySqlConnection();
//        ExecutorService exec= Executors.newCachedThreadPool();
//        for(int i=0;i<50;i++) {
//            exec.execute(new Runnable() {
//                public void run() {
//                    PurchaseTicketImpl purchaseTicket = new PurchaseTicketImpl(connection);
//                    purchaseTicket.purchaseTicket("420602199605162024", "二等座", "2016-11-12", 16, "南京南", "北京南");
//                    long endTime=System.currentTimeMillis(); //获取结束时间
//                    System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//                }
//            });
//        }
//        exec.shutdown();
    }

    private void connectMongoDB(){

        try{
            MongoDatabase mongoDatabase=Constant.mongoconnector.getMongoDBConnection();
            System.out.println("Connect to database successfully");
            this.initialMongoDBCollections(mongoDatabase);
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public static void main(String[] args){
        Main main=new Main();
        main.connectMongoDB();
//        main.connectMySQL();
    }

}

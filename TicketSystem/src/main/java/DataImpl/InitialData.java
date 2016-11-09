package DataImpl;

import util.Constant;
import util.SeatNumber;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Seven on 2016/11/9.
 */
public class InitialData {
    Connection connection;

    public void initial() throws SQLException, ClassNotFoundException {
        DBConnector connector=new DBConnector();
        connection=connector.getMySqlConnection();
    }

    public BufferedReader readFromTXT(String fileRoute) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileRoute)), "UTF8"));
            return br;
        } catch (FileNotFoundException e) {
            System.out.println("Ooops...File Not Found!");
        } catch (IOException e) {
            System.out.println("Ooops...Read File Failed!");
        }
        return null;
    }

    public void initialRoute() {
        String timetable = "timetable.txt";
        BufferedReader br = this.readFromTXT(timetable);
        if (br != null) {
            String line = "";
            try {
                while ((line = br.readLine()) != null) {
                    String[] items = line.split(" ");
                    int trainNum = Integer.valueOf(items[0].substring(1, items[0].length()));
                    int station_num = Integer.valueOf(items[1]);
                    String station_name = items[2];
                    Statement statement;
                    try {
                        statement = connection.createStatement();
                        String sql = "INSERT INTO route (train_num,station_name,station_num) VALUES (\"" + trainNum + "\",\"" + station_name + "\",\"" + station_num + "\");";
//                       System.out.println(sql);
                        statement.execute(sql);
                    } catch (SQLException e) {
                        System.out.println("Ooops...sql execute failed!");
                    }
                }
            } catch (IOException e) {
                System.out.println("Ooops...read file failed!");
            }
            System.out.println("Initial Route Successfully!");
    }
    }

    public void initialTimetable(){
        String timetable="timetable.txt";
        BufferedReader br=this.readFromTXT(timetable);
        if(br!=null){
            String line="";
            try {
                while ((line=br.readLine())!=null){
                    String[] stations=line.split(" ");
                    //
                    int train_num=Integer.valueOf(stations[0].substring(1,stations[0].length()));
                    int station_num=Integer.valueOf(stations[1]);
                    String station_name=stations[2];
                    Time arrive_time=Time.valueOf(stations[3]+":00");
                    Time departure_time=Time.valueOf(stations[4]+":00");
                    int distance;
                    if(!stations[6].equals("NULL")) {
                        distance = Integer.valueOf(stations[6]);
                    }else{
                        distance=0;
                    }
                    String sql = "INSERT INTO timetable(train_num,station_name,station_num,arrive_time,departure_time,distance) VALUES (\""
                            + train_num + "\",\"" + station_name + "\",\"" + station_num +"\",\""+arrive_time+"\",\""+departure_time+"\",\""+distance+ "\");";
                    System.out.println(sql);
                    Statement statement = connection.createStatement();
                    statement.execute(sql);
                }
            } catch (IOException e) {
                System.out.println("Ooops...read file failed!");
            } catch (SQLException e) {
//                e.printStackTrace();
                System.out.println("Ooops...execute sql failed!");
            }
            System.out.println("Initial Timetable Successfully!");
        }
    }

    public void initialCarriage(){
        try {
            Statement statement=connection.createStatement();
            String sql = "INSERT INTO carriage(name,seat_quantity) VALUES ('商务座','30')";
            statement.execute(sql);
            sql="INSERT INTO carriage(name,seat_quantity) VALUES ('一等座','70')";
            statement.execute(sql);
            sql="INSERT INTO carriage(name,seat_quantity) VALUES ('二等座','100')";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial carriage successfully");
    }

    public void initialSeatBusiness(){
        //每行3座ABC，每车厢10行
        try {
            Statement statement=connection.createStatement();
            String sql;
            for(int i=1;i<=10;i++){
                for(int j=1;j<=3;j++){
                    int row_number=i;
                    String seatNumber=SeatNumber.getNumber(j);
                    sql="INSERT INTO seat_business(row_number,seat_number) VALUES("+"\""+row_number+"\",\""+seatNumber+"\")";
//                    System.out.println(sql);
                    statement.execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial seatbusiness successfully");
    }

    public void initialSeatFirst(){
        //每行4座ABCD，每车厢15行
        try {
            Statement statement=connection.createStatement();
            String sql;
            for(int i=1;i<=15;i++){
                for(int j=1;j<=4;j++){
                    int row_number=i;
                    String seatNumber=SeatNumber.getNumber(j);
                    sql="INSERT INTO seat_first(row_number,seat_number) VALUES("+"\""+row_number+"\",\""+seatNumber+"\")";
//                    System.out.println(sql);
                    statement.execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial seatfirst successfully");
    }


    public void initialSeatSecond(){
        //每行4座ABCD，每车厢15行
        try {
            Statement statement=connection.createStatement();
            String sql;
            for(int i=1;i<=20;i++){
                for(int j=1;j<=5;j++){
                    int row_number=i;
                    String seatNumber=SeatNumber.getNumber(j);
                    sql="INSERT INTO seat_second(row_number,seat_number) VALUES("+"\""+row_number+"\",\""+seatNumber+"\")";
//                    System.out.println(sql);
                    statement.execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial seatsecond successfully");
    }

    public void initialTicketPrice(){
        try {
            Statement statement=connection.createStatement();
            String sql;
            sql="INSERT INTO ticket_price(price) VALUE (\""+ Constant.BUSINESS_PRICE+"\")";
            statement.execute(sql);
            sql="INSERT INTO ticket_price(price) VALUE (\""+ Constant.FIRST_PRICE+"\")";
            statement.execute(sql);
            sql="INSERT INTO ticket_price(price) VALUE (\""+ Constant.SECOND_PRICE+"\")";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial ticket_price successfully");
    }

    public void initialTrain(){
        Random r=new Random();
        List<Integer> train_nums=this.getTrainNum();
        try {
            Statement statement=connection.createStatement();
            Date d1=new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-09");
            Calendar c1=Calendar.getInstance();
            c1.setTime(d1);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            while (c1.getTime().before(
                    new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-26"))){
                //获得日期
                String date=sdf.format(c1.getTime());
               //获得所有车次
               for(int train_num:train_nums){
                    int random=r.nextInt(2);
                    //随机生成车厢大小
                    int[] trainSize;
                    if(random==1){
                        trainSize=Constant.LARGE_TRAIN;

                    }else{
                        trainSize=Constant.SMALL_TRAIN;
                    }
                    String sql;
                    sql="INSERT INTO train(date,train_num,business_num,first_num,second_num) VALUES("+
                            "\""+date+"\",\""+train_num+"\",\""+trainSize[0]+"\",\""+trainSize[1]+"\",\""+trainSize[2]+"\")";
                    statement.execute(sql);
//                    System.out.println(sql);
               }
//                System.out.println(random+"  "+date);
                c1.add(Calendar.DAY_OF_MONTH,1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial train Successfully!");
    }

    public List<Integer> getTrainNum(){
        List<Integer> train_nums=new ArrayList<Integer>();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql="SELECT DISTINCT train_num FROM route";
            ResultSet rs=statement.executeQuery(sql);
            while (rs.next()){
                train_nums.add(rs.getInt("train_num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return train_nums;
    }

    public void initialUser(){
        try {
            Statement statement=connection.createStatement();
            String sql;
            sql="INSERT INTO user(user_name,identity_num) VALUES (\"txy\",\"420602199605162024\");";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void initialRemainTicket(){
        try {
            ResultSet rs;
            Statement statement=connection.createStatement();
            String sql="SELECT seat_quantity FROM carriage";
            rs=statement.executeQuery(sql);
            rs.next();
            int business_seat=rs.getInt("seat_quantity");
            rs.next();
            int first_seat=rs.getInt("seat_quantity");
            rs.next();
            int second_seat=rs.getInt("seat_quantity");
            rs.next();
            int no_seat=rs.getInt("seat_quantity");

            sql="SELECT date,train_num,business_num,first_num,second_num FROM train";
            rs=statement.executeQuery(sql);

            while(rs.next()){
                //取出列车字段信息
                String date=rs.getString("date");
                int train_num=rs.getInt("train_num");
                int business_num=rs.getInt("business_num");
                int first_num=rs.getInt("first_num");
                int second_num=rs.getInt("second_num");
                //计算每种座位类型的个数
                int business_remain=business_seat*business_num;
                int first_remain=first_seat*first_num;
                int second_remain=second_seat*second_num;
                int noSeat_remain=no_seat*second_num;
                Statement statement1=connection.createStatement();
                sql="SELECT station_name FROM route WHERE train_num="+train_num;
                ResultSet re=statement1.executeQuery(sql);
                List<String> stations=new ArrayList<String>();
                while (re.next()){
                    stations.add(re.getString("station_name"));
                }
                //该车次对应的所有站名
                for(int i=0;i<stations.size();i++){
                    for(int j=0;j<stations.size();j++){
                        if(i!=j){
                            String departure=stations.get(i);
                            String destination=stations.get(j);
                            sql="INSERT INTO remain_ticket(date,train_num,departure,destination,business_remain,first_remain,second_remain,noSeat_remain) VALUES("
                                    +"\""+date+"\",\""+train_num+"\",\""+departure+"\",\""+destination+"\",\""
                                    +business_remain+"\",\""+first_remain+"\",\""+second_remain+"\",\""+noSeat_remain+"\")";
                            statement1.execute(sql);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Initial remain_ticket Successfully!");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitialData initialData=new InitialData();
        initialData.initial();
//        initialData.initialRoute();
//        initialData.initialTimetable();
//        initialData.initialCarriage();
//        initialData.initialSeatBusiness();
//        initialData.initialSeatFirst();
//        initialData.initialSeatSecond();
//        initialData.initialTicketPrice();
//        initialData.initialUser();
//        initialData.initialTrain();
//        initialData.initialRemainTicket();
    }
}

package DataImpl;

import PO.FrequencyPO;
import PO.RemainTicketPO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/11/10.
 */
public class PurchaseTicketImpl {
    private Connection connection;
    private List<FrequencyPO> frequencyPOList;
    private RemainTicketPO remainTicketPO;

    public PurchaseTicketImpl(Connection connection){
        this.connection=connection;

    }

    /**
     * 查询某一天从起始地到目的地的所有车次及其出发时间、到达时间
     * @param date
     * @param departure
     * @param destination
     * @return
     */
    public List<FrequencyPO> getFrequency(String date,String departure,String destination) {
        frequencyPOList=new ArrayList<FrequencyPO>();
        //查询route表，返回所有满足的车次
        try {
            Statement statement = connection.createStatement();
            String sql ="SELECT r1.train_num,t1.departure_time,t2.arrive_time," +
                    "(TIME_TO_SEC(t2.arrive_time)-TIME_TO_SEC(t1.departure_time)) during_time " +
                    "FROM route r1,route r2,timetable t1,timetable t2 " +
                    "WHERE r1.train_num=r2.train_num AND r1.station_name=\""+departure+"\" " +
                    "AND r2.station_name=\""+destination+"\" AND r1.station_num<r2.station_num " +
                    "AND r1.station_num=t1.station_num AND r1.train_num=t1.train_num " +
                    "AND r1.train_num=t2.train_num AND r2.station_num=t2.station_num " +
                    "ORDER BY t1.departure_time";
//            System.out.println(sql);
            ResultSet rs=statement.executeQuery(sql);
            while(rs.next()){
                int train_num=rs.getInt("r1.train_num");
                String departure_time=rs.getString("t1.departure_time");
                String arrive_time=rs.getString("t2.arrive_time");
                int during_time=rs.getInt("during_time");
                FrequencyPO frequencyPO=new FrequencyPO(date,train_num,departure,destination,departure_time,arrive_time,during_time);
                frequencyPOList.add(frequencyPO);
//                System.out.println(frequencyPO.toString());
            }
        } catch (SQLException e) {
            System.out.println("Create sql executor failed");
            e.printStackTrace();
        }
        return frequencyPOList;
    }

    /**
     * 某一车次的余票查询
     * @param date
     * @param train_num
     * @param departure
     * @param destination
     * @return
     */
    public RemainTicketPO getRemainTicket(String date,int train_num,String departure,String destination){
        try {
            Statement statement=connection.createStatement();

            String sql="SELECT business_remain,first_remain,second_remain,noSeat_remain FROM remain_ticket WHERE date=\"" +
                    date+"\" AND train_num="+train_num+" AND departure=\"" +
                    departure + "\" AND destination=\"" + destination + "\"";

            ResultSet rs=statement.executeQuery(sql);
            if(rs.next()){
                int business_remain=rs.getInt("business_remain");
                int first_remain=rs.getInt("first_remain");
                int second_remain=rs.getInt("second_remain");
                int noseat_remain=rs.getInt("noSeat_remain");
                remainTicketPO=new RemainTicketPO(date,train_num,departure,destination,business_remain,first_remain,second_remain,noseat_remain);
                System.out.println(remainTicketPO.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remainTicketPO;
    }

    /**
     * 判断此趟列车是否还有余票可售，并返回相关的余票信息id
     * @param date
     * @param train_num
     * @param departure
     * @param destination
     * @param seat_type
     * @return
     */
    public List<Integer> ticketAvailable(String date, int train_num, String departure,String destination,String seat_type){
        List<Integer> ids=new ArrayList<Integer>();
        try {
            Statement statement=connection.createStatement();
            //查找所有目的地或始发地包含在始发站和目的站之间的站，destination为目的地，departure为出发地
            //对应座位类型下的剩余票数
            //去重
            String sql="SELECT rt.id,rt."+seat_type+
                    " FROM (SELECT id,departure,destination," +seat_type+
                    " FROM remain_ticket WHERE train_num=" +
                    train_num + " AND date=\""+date +
                    "\") rt, (SELECT DISTINCT r2.station_name,r2.station_num FROM route r1,route r2,route r3 " +
                    "WHERE r1.train_num=r2.train_num AND r1.train_num=r3.train_num AND r1.train_num=" +
                    train_num + " AND r1.station_name=\""+departure+"\" AND r2.station_num>r1.station_num AND r3.station_name=\"" +
                    destination+"\" AND r2.station_num<r3.station_num) r WHERE rt.departure=r.station_name OR rt.destination=r.station_name OR rt.departure=\"" +
                    departure+"\" OR rt.destination=\"" +
                    destination+"\" FOR UPDATE;";
//            System.out.println(sql);
            ResultSet rs=statement.executeQuery(sql);
            while (rs.next()){
                if(rs.getInt("rt.second_remain")<=0){
                    ids.clear();
                    break;
                }else{
                    ids.add(rs.getInt("rt.id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(ids.size());
        return ids;
    }

    /**
     * 根据seat_type返回在remain_tickets中的对应字段名
     * @param seat_type
     * @return
     */
    public String[] getSeatType(String seat_type){
        int seat_type_num=0;
        String seatRemainType;
        String seatName;
        try {
            Statement statement=connection.createStatement();
            String sql="SELECT type FROM carriage WHERE name=\""+seat_type+"\"";
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next()){
                seat_type_num=rs.getInt("type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (seat_type_num){
            case 1:
                seatRemainType="business_remain";
                seatName="business_seat";
                break;
            case 2:
                seatRemainType="first_remain";
                seatName="first_seat";
                break;
            case 3:
                seatRemainType="second_remain";
                seatName="second_seat";
                break;
            case 4:
                seatRemainType="noSeat_remain";
                seatName="no_seat";
                break;
            default:
                seatRemainType="second_remain";
                seatName="second_seat";
                break;
        }
        String[] seatIndex={seatRemainType,seatName};
        return seatIndex;
    }

    /**
     * 购票流程，若成功，则将该票相关信息打印并持久化保存，若失败则告知用户购票失败
     * @param identity_num
     * @param seat_type
     * @param date
     * @param train_num
     * @param departure
     * @param destination
     */
    public void purchaseTicket(String identity_num,String seat_type,String date,int train_num,
                               String departure,String destination){
        List<Integer> ids=new ArrayList<Integer>();
        try {
            Statement statement=connection.createStatement();
            String work="SET AUTOCOMMIT=0;";
            statement.execute(work);
            work="BEGIN WORK;";
            statement.execute(work);
            String seatRemain_name=this.getSeatType(seat_type)[0];
            String seatNum_name=this.getSeatType(seat_type)[1];
            //事务开始
            //加锁
            if((ids=this.ticketAvailable(date,train_num,departure,destination,seatRemain_name)).size()>0){
                //更新对应表项
                System.out.println("Tickets Available!");
                for(int id:ids) {
                    String update = "UPDATE remain_ticket SET "+seatRemain_name+"="+seatRemain_name+"-1"+ " WHERE id=" + id;
//                    System.out.println(update);
                    statement.execute(update);
                    update = "UPDATE remain_ticket SET "+seatNum_name+"="+seatNum_name+"+1"+ " WHERE id=" + id;
                    statement.execute(update);
                }
            }else{
                System.out.println("Tickets NOT Available!");
            }
            work="COMMIT WORK;";
            statement.execute(work);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //出票
        //计算座位、票价

        System.out.println("Purchase TicketPO Successfully!");
    }
}

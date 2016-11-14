package DataImpl;

import PO.FrequencyPO;
import PO.RemainTicketPO;
import PO.TicketPO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/11/10.
 */
public class PurchaseTicketImpl {
    private Connection connection;
    private List<FrequencyPO> frequencyPOList;
    private RemainTicketPO remainTicketPO;

    public PurchaseTicketImpl(Connection connection) {
        this.connection = connection;

    }

    /**
     * 查询某一天从起始地到目的地的所有车次及其出发时间、到达时间
     *
     * @param date
     * @param departure
     * @param destination
     * @return
     */
    public List<FrequencyPO> getFrequency(String date, String departure, String destination) {
        frequencyPOList = new ArrayList<FrequencyPO>();
        //查询route表，返回所有满足的车次
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT r1.train_num,t1.departure_time,t2.arrive_time," +
                    "(TIME_TO_SEC(t2.arrive_time)-TIME_TO_SEC(t1.departure_time)) during_time " +
                    "FROM route r1,route r2,timetable t1,timetable t2 " +
                    "WHERE r1.train_num=r2.train_num AND r1.station_name=\"" + departure + "\" " +
                    "AND r2.station_name=\"" + destination + "\" AND r1.station_num<r2.station_num " +
                    "AND r1.station_num=t1.station_num AND r1.train_num=t1.train_num " +
                    "AND r1.train_num=t2.train_num AND r2.station_num=t2.station_num " +
                    "ORDER BY t1.departure_time";
//            System.out.println(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int train_num = rs.getInt("r1.train_num");
                String departure_time = rs.getString("t1.departure_time");
                String arrive_time = rs.getString("t2.arrive_time");
                int during_time = rs.getInt("during_time");
                FrequencyPO frequencyPO = new FrequencyPO(date, train_num, departure, destination, departure_time, arrive_time, during_time);
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
     *
     * @param date
     * @param train_num
     * @param departure
     * @param destination
     * @return
     */
    public RemainTicketPO getRemainTicket(String date, int train_num, String departure, String destination) {
        try {
            Statement statement = connection.createStatement();

            String sql = "SELECT business_remain,first_remain,second_remain,noSeat_remain FROM remain_ticket WHERE date=\"" +
                    date + "\" AND train_num=" + train_num + " AND departure=\"" +
                    departure + "\" AND destination=\"" + destination + "\"";

            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                int business_remain = rs.getInt("business_remain");
                int first_remain = rs.getInt("first_remain");
                int second_remain = rs.getInt("second_remain");
                int noseat_remain = rs.getInt("noSeat_remain");
                remainTicketPO = new RemainTicketPO(date, train_num, departure, destination, business_remain, first_remain, second_remain, noseat_remain);
                System.out.println(remainTicketPO.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remainTicketPO;
    }

    /**
     * 判断此趟列车是否还有余票可售，并返回相关的余票信息id
     *
     * @param date
     * @param train_num
     * @param departure
     * @param destination
     * @param seat_type
     * @return
     */
    public int ticketAvailable(String date, int train_num, String departure,
                               String destination, String seat_type,
                               String seat_num_name) throws SQLException {
        int seat_num = 0;
        Statement statement = connection.createStatement();
        //查找所有目的地或始发地包含在始发站和目的站之间的站，destination为目的地，departure为出发地
        //对应座位类型下的剩余票数
        //去重
        String sql = "SELECT id" +
                " FROM remain_ticket rt" +
                " WHERE rt.train_num=" + train_num + " AND date=\"" + date + "\" AND rt.departure_num<(SELECT station_num" +
                " FROM route" +
                " WHERE train_num=" + train_num + " AND station_name=\"" + destination + "\")" +
                " AND rt.destination_num>(SELECT station_num" +
                " FROM route" +
                " WHERE station_name=\"" + departure + "\" AND train_num=" + train_num + ")" +
                ";";
//            System.out.println(sql);

        ResultSet rs = statement.executeQuery(sql);
        List<Integer> idList = new ArrayList<Integer>();
        String ids = "";
        String seats = "";
        int i = 0;
        while (rs.next()) {
            idList.add(rs.getInt("id"));
            if (i == 0) {
                ids = "id=" + idList.get(i);
                seats = "(id=" + idList.get(i) + " AND " + seat_type + ">0)";
            } else {
                ids = ids + " OR id=" + idList.get(i);
                seats = seats + " OR (id=" + idList.get(i) + " AND " + seat_type + ">0)";
            }
            i++;
        }

        String auto = "SET AUTOCOMMIT=0";
        statement.execute(auto);

        String work = "BEGIN WORK;";
        statement.execute(work);

        sql = "SELECT id," + seat_type + "," + seat_num_name +
                " FROM remain_ticket WHERE " + ids + " FOR UPDATE;";

        rs = statement.executeQuery(sql);

        while (rs.next()) {
            if (rs.getInt(seat_type) <= 0) {
                System.out.println(rs.getInt("id"));
                seat_num = -1;
                work = "COMMIT WORK";
                statement.execute(work);
                return seat_num;
            }
        }

        String update = "UPDATE remain_ticket SET " + seat_type + "=" + seat_type + "-1," + seat_num_name + "=" + seat_num_name + "+1" +
                " WHERE " + seats + ";";
//        System.out.println(update);
        int s=statement.executeUpdate(update);

        sql = "SELECT " + seat_num_name + " FROM remain_ticket WHERE id=" + "(SELECT id FROM remain_ticket WHERE " +
                "date='" + date + "' AND train_num=" + train_num + " AND departure='" + departure +
                "' AND destination='" + destination +"') AND "+seat_type+ ">=0;";
        rs = statement.executeQuery(sql);

        if (s==idList.size()&&rs.next()) {
            seat_num = rs.getInt(seat_num_name);
            work = "COMMIT WORK;";
            statement.execute(work);
        } else {
            System.out.println(s);
            seat_num = -1;
            work = "COMMIT WORK";
            statement.execute(work);
            return seat_num;
        }

        statement.execute(auto);
        return seat_num;
    }

    /**
     * 购票流程，若成功，则将该票相关信息打印并持久化保存，若失败则告知用户购票失败
     *
     * @param identity_num
     * @param seat_type
     * @param date
     * @param train_num
     * @param departure
     * @param destination
     */
    public void purchaseTicket(String identity_num, String seat_type, String date, int train_num, String departure, String destination) {
        try {
            Statement statement = connection.createStatement();

            int seat_type_num = this.getSeatType(seat_type);
            String seatRemain_name = this.getSeatTypeName(seat_type)[0];
            String seatNum_name = this.getSeatTypeName(seat_type)[1];

            //事务开始
            //加锁
            int seat_num = this.ticketAvailable(date, train_num, departure, destination, seatRemain_name, seatNum_name);
            String auto="SET AUTOCOMMIT=1;";
            statement.execute(auto);
            if (seat_num != -1) {
                //购票成功
                ResultSet rs;
                //自动分配座位
                //计算座位号
                System.out.println(seat_num);
                //计算车厢号
                int carriage_number = 0;
                String train_sql = "SELECT business_num,first_num " +
                        "FROM train " +
                        "WHERE date=\"" + date + "\" AND train_num=" + train_num + ";";
                rs = statement.executeQuery(train_sql);
                if (rs.next()) {
                    int business_carriage_num = rs.getInt("business_num");
                    int first_carriage_num = rs.getInt("first_num");
                    switch (seat_type_num) {
                        case 1:
                            carriage_number = 1;
                            break;
                        case 2:
                            carriage_number = business_carriage_num + 1;
                            break;
                        case 3:
                            carriage_number = business_carriage_num + first_carriage_num + 1;
                            break;
                        case 4:
                            carriage_number = business_carriage_num + first_carriage_num + 1;
                            break;
                    }
                }

                int row = 0;
                String seat_number = "";
                int per_carriage = 1;
                if (carriage_number > 0) {
                    String carriage_sql = "SELECT seat_quantity FROM carriage WHERE name=\"" + seat_type + "\";";
                    rs = statement.executeQuery(carriage_sql);
                    if (rs.next()) {
                        per_carriage = rs.getInt("seat_quantity");
                    }
                    carriage_number = carriage_number + (seat_num-1) / per_carriage;
                    seat_num = seat_num % per_carriage;
                    if (seat_num == 0) {
                        seat_num = per_carriage;
                    }
                    String seat_sql = "SELECT row_number,seat_number " +
                            "FROM " + seatNum_name +
                            " WHERE id=" + seat_num + ";";
                    rs = statement.executeQuery(seat_sql);
                    if (rs.next()) {
                        row = rs.getInt("row_number");
                    }
                    seat_number = rs.getString("seat_number");
                }

                //获得列车出发时间
                String departure_time = "";
                String departure_sql = "SELECT departure_time FROM timetable WHERE train_num=" + train_num +
                        " AND station_name=\"" + departure + "\";";
                rs = statement.executeQuery(departure_sql);
                if (rs.next()) {
                    departure_time = rs.getString("departure_time");
                }
                //获得每公里票价
                double per = 0;
                String price_sql = "SELECT price FROM ticket_price WHERE type=" + seat_type_num;
                rs = statement.executeQuery(price_sql);
                if (rs.next()) {
                    per = rs.getDouble("price");
                }
                //获得里程数
                int distance = 0;
                String sql = "SELECT t.distance FROM timetable t " +
                        "WHERE t.train_num=" + train_num + " AND (t.station_name=\"" +
                        departure +
                        "\" OR t.station_name=\"" +
                        destination + "\") ORDER BY t.station_num";
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                    int d1 = rs.getInt("t.distance");
                    if (rs.next()) {
                        int d2 = rs.getInt("t.distance");
                        distance = Math.abs(d2 - d1);
                    }
                }
                //计算票价
                DecimalFormat df = new DecimalFormat("0.00");
                double price = Double.valueOf(df.format(per * distance));


                String user_sql = "SELECT id FROM user WHERE identity_num=\"" + identity_num + "\";";
                rs = statement.executeQuery(user_sql);
                int user_id = 0;
                if (rs.next()) {
                    user_id = rs.getInt("id");
                }

                String ticket_sql = "INSERT INTO ticket(user_id,seat_type,`date`,train_num,departure_time,departure," +
                        "destination,ticket_price,row,seatNum,carriage_num) VALUES(" + user_id + "," + seat_type_num + ",'"
                        + date + "'," + train_num + ",'" + departure_time + "','" + departure + "','" + destination + "'," + price + "," + row + ",'" + seat_number + "','" + carriage_number + "')";
//                System.out.println(ticket_sql);
                statement.execute(ticket_sql);


                TicketPO ticketPO = new TicketPO(seat_type, date, train_num, departure_time,
                        departure, destination, price, carriage_number, row, seat_number);
                System.out.println(ticketPO.getCarriage_num() + "车" + ticketPO.getRow() + "排" + ticketPO.getSeat_number() + "座");

            } else {
                System.out.println("Ooops...Purchase Ticket Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得车厢类型对应的id
     *
     * @param seat_name
     * @return
     */
    public int getSeatType(String seat_name) {
        int seat_type_num = 0;
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT type FROM carriage WHERE name=\"" + seat_name + "\"";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                seat_type_num = rs.getInt("type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seat_type_num;
    }


    /**
     * 根据seat_type返回在remain_tickets中的对应字段名
     *
     * @param seat_type
     * @return
     */
    public String[] getSeatTypeName(String seat_type) {
        int seat_type_num = this.getSeatType(seat_type);
        String seatRemainType;
        String seatName;
        switch (seat_type_num) {
            case 1:
                seatRemainType = "business_remain";
                seatName = "business_seat";
                break;
            case 2:
                seatRemainType = "first_remain";
                seatName = "first_seat";
                break;
            case 3:
                seatRemainType = "second_remain";
                seatName = "second_seat";
                break;
            case 4:
                seatRemainType = "noSeat_remain";
                seatName = "no_seat";
                break;
            default:
                seatRemainType = "second_remain";
                seatName = "second_seat";
                break;
        }
        String[] seatIndex = {seatRemainType, seatName};
        return seatIndex;
    }

}

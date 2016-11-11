package PO;

/**
 * Created by Seven on 2016/11/10.
 */
public class TicketPO {
        String seat_type;
        String date;
        int train_num;
        String departure_time;
        String departure;
        String destination;
        double ticket_price;
        int carriage_num;
        int row;
        String seat_number;

        public TicketPO(String seat_type, String date, int train_num, String departure_time, String departure, String destination, double ticket_price,int carriage_num,int row,String seat_number) {

                this.seat_type = seat_type;
                this.date = date;
                this.train_num = train_num;
                this.departure_time = departure_time;
                this.departure = departure;
                this.destination = destination;
                this.ticket_price = ticket_price;
                this.carriage_num=carriage_num;
                this.row=row;
                this.seat_number=seat_number;
        }

        @Override
        public String toString() {
                return "座位类型'" + seat_type + '\'' +
                        " 日期 '" + date + '\'' +
                        " 车次 " + train_num +
                        " 发车时间 '" + departure_time + '\'' +
                        " 出发地 '" + departure + '\'' +
                        " 目的地 '" + destination + '\'' +
                        " 票价 " + ticket_price +
                        " 座位 " + carriage_num +
                        "车 " + row +
                        "排" + seat_number + '座';
        }
}

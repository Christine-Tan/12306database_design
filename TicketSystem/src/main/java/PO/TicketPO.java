package PO;

/**
 * Created by Seven on 2016/11/10.
 */
public class TicketPO {
        int user_id;
        String seat_type;
        String date;
        int train_num;
        String departure_time;
        String departure;
        String destination;
        double ticket_price;

        public TicketPO(int user_id, String seat_type, String date, int train_num, String departure_time, String departure, String destination, double ticket_price) {
                this.user_id = user_id;
                this.seat_type = seat_type;
                this.date = date;
                this.train_num = train_num;
                this.departure_time = departure_time;
                this.departure = departure;
                this.destination = destination;
                this.ticket_price = ticket_price;
        }


        @Override
        public String toString() {
                return "TicketPO{" +
                        "user_id=" + user_id +
                        ", seat_type='" + seat_type + '\'' +
                        ", date='" + date + '\'' +
                        ", train_num=" + train_num +
                        ", departure_time='" + departure_time + '\'' +
                        ", departure='" + departure + '\'' +
                        ", destination='" + destination + '\'' +
                        ", ticket_price=" + ticket_price +
                        '}';
        }
}

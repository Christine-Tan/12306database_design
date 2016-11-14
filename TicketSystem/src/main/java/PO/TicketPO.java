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

        public String getSeat_type() {
                return seat_type;
        }

        public void setSeat_type(String seat_type) {
                this.seat_type = seat_type;
        }

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public int getTrain_num() {
                return train_num;
        }

        public void setTrain_num(int train_num) {
                this.train_num = train_num;
        }

        public String getDeparture_time() {
                return departure_time;
        }

        public void setDeparture_time(String departure_time) {
                this.departure_time = departure_time;
        }

        public String getDeparture() {
                return departure;
        }

        public void setDeparture(String departure) {
                this.departure = departure;
        }

        public String getDestination() {
                return destination;
        }

        public void setDestination(String destination) {
                this.destination = destination;
        }

        public double getTicket_price() {
                return ticket_price;
        }

        public void setTicket_price(double ticket_price) {
                this.ticket_price = ticket_price;
        }

        public int getCarriage_num() {
                return carriage_num;
        }

        public void setCarriage_num(int carriage_num) {
                this.carriage_num = carriage_num;
        }

        public int getRow() {
                return row;
        }

        public void setRow(int row) {
                this.row = row;
        }

        public String getSeat_number() {
                return seat_number;
        }

        public void setSeat_number(String seat_number) {
                this.seat_number = seat_number;
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

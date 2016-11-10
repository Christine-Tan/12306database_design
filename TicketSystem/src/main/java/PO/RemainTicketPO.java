package PO;

/**
 * Created by Seven on 2016/11/10.
 */
public class RemainTicketPO {
    String date;
    int train_num;
    String departure;
    String destination;
    int business_remain;
    int first_remain;
    int second_remain;
    int noseat_remain;

    public RemainTicketPO(String date, int train_num, String departure,
                          String destination, int business_remain,
                          int first_remain, int second_remain, int noseat_remain){
        this.date=date;
        this.train_num=train_num;
        this.departure=departure;
        this.destination=destination;
        this.business_remain=business_remain;
        this.first_remain=first_remain;
        this.second_remain=second_remain;
        this.noseat_remain=noseat_remain;
    }

    @Override
    public String toString() {
        return "RemainTicketPO{" +
                "date='" + date + '\'' +
                ", train_num=" + train_num +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", business_remain=" + business_remain +
                ", first_remain=" + first_remain +
                ", second_remain=" + second_remain +
                ", noseat_remain=" + noseat_remain +
                '}';
    }
}

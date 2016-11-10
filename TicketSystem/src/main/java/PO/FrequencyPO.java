package PO;

/**
 * Created by Seven on 2016/11/10.
 */
public class FrequencyPO {
    private String date;
    private int train_num;
    private String departure;
    private String destination;
    private String departure_time;
    private String arrive_time;
    private String during_time;

    public FrequencyPO(String date,int train_num,String departure,
                       String destination,String departure_time,String arrive_time,int during_time){
        this.date=date;
        this.train_num=train_num;
        this.departure=departure;
        this.destination=destination;
        this.departure_time=departure_time;
        this.arrive_time=arrive_time;
        this.during_time=this.secondToHour(during_time);
    }

    private String secondToHour(int second){
        int hours=second/3600;
        int minutes=second/60-hours*60;
        String during_time=hours+"h"+minutes+"m";
        return during_time;
    }

    @Override
    public String toString() {
        return "FrequencyPO{" +
                "date='" + date + '\'' +
                ", train_num=" + train_num +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", arrive_time='" + arrive_time + '\'' +
                ", during_time='" + during_time + '\'' +
                '}';
    }
}

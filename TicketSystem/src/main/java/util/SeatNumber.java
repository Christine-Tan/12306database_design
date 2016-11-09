package util;

/**
 * Created by Seven on 2016/11/9.
 */
public enum SeatNumber {

    A("A",1),B("B",2),C("C",3),D("D",4),F("F",5);

    private int value;
    private String number;

    private SeatNumber(String number,int value){
        this.number=number;
        this.value=value;
    }

    public static String getNumber(int value){
        for(SeatNumber s:SeatNumber.values()){
            if(s.getValue()==value){
                return s.number;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

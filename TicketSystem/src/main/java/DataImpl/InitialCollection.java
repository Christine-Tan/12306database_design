package DataImpl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import util.Constant;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Seven on 2016/11/13.
 */
public class InitialCollection {
    MongoDatabase database;
    List<Integer> train_num;
    public InitialCollection(MongoDatabase database){
        this.database=database;
    }

    public void createCollections(){
        database.createCollection("route");
        database.createCollection("timetable");
        database.createCollection("train");
        database.createCollection("remain_ticket");
    }


    public BufferedReader readFromTXT(String fileRoute) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileRoute)), "UTF8"));
            return bf;
        } catch (FileNotFoundException e) {
            System.out.println("Ooops...File Not Found!");
        } catch (IOException e) {
            System.out.println("Ooops...Read File Failed!");
        }
        return null;
    }

    public void initialRoute(){
        MongoCollection<Document> routec=database.getCollection("route");
        routec.drop();
        database.createCollection("route");
        List<Document> routeList=new ArrayList<Document>();
        train_num=new ArrayList<Integer>();
        String timetable = "timetable.txt";
        BufferedReader br=this.readFromTXT(timetable);
        if (br != null) {
            String line = "";
            try {
                while ((line = br.readLine()) != null) {
                    String[] items = line.split(" ");
                    int trainNum = Integer.valueOf(items[0].substring(1, items[0].length()));
                    int station_num = Integer.valueOf(items[1]);
                    String station_name = items[2];
                    Document routed=new Document();
                    routed.put("train_num",trainNum);
                    routed.put("station_num",station_num);
                    routed.put("station_name",station_name);
                    train_num.add(trainNum);
                    routeList.add(routed);
                    if(routeList.size()==100000){
                        routec.insertMany(routeList);
                        routeList.clear();
                    }
                }
            } catch (IOException e) {
                System.out.println("Ooops...read file failed!");
            }
            if(!routeList.isEmpty()){
                routec.insertMany(routeList);
            }
            System.out.println("Initial Route Successfully!");
        }
    }

    public void initialTimeTable(){
        MongoCollection<Document> timetablec=database.getCollection("timetable");
        timetablec.drop();
        database.createCollection("timetable");
        List<Document> timetableList=new ArrayList<Document>();

        String timetable = "timetable.txt";
        BufferedReader br=this.readFromTXT(timetable);
        if (br != null) {
            String line = "";
            try {
                while ((line = br.readLine()) != null) {
                    String[] stations = line.split(" ");
                    int train_num = Integer.valueOf(stations[0].substring(1, stations[0].length()));
                    int station_num = Integer.valueOf(stations[1]);
                    String station_name = stations[2];
                    String arrive_time=stations[3] + ":00";
                    String departure_time = stations[4] + ":00";
                    int distance=0;
                    if (!stations[6].equals("NULL")) {
                        distance = Integer.valueOf(stations[6]);
                    } else {
                        distance = 0;
                    }
//                    train_num,station_name,station_num,arrive_time,departure_time,distance
                    Document timetabled=new Document();
                    timetabled.put("train_num",train_num);
                    timetabled.put("station_name",station_name);
                    timetabled.put("station_num",station_num);
                    timetabled.put("arrive_time",arrive_time);
                    timetabled.put("departure_time",departure_time);
                    timetabled.put("distance",distance);
                    timetableList.add(timetabled);
                    if(timetableList.size()==100000){
                        timetablec.insertMany(timetableList);
                        timetableList.clear();
                    }
                }
            } catch (IOException e) {
                System.out.println("Ooops...read file failed!");
            }
            if(!timetableList.isEmpty()){
                timetablec.insertMany(timetableList);
            }
            System.out.println("Initial Timetable Successfully!");
        }
    }

    public void initialTrain(){
        MongoCollection<Document> trainc=database.getCollection("train");
        trainc.drop();
        database.createCollection("train");

        List<Document> trainList=new ArrayList<Document>();
        Random r = new Random();
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-09");
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while (c1.getTime().before(
                    new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-26"))) {
                //获得日期
                String date = sdf.format(c1.getTime());
                //获得所有车次
                for (int trainNum : train_num) {
                    int random = r.nextInt(2);
                    //随机生成车厢大小
                    int[] trainSize;
                    if (random == 1) {
                        trainSize = Constant.LARGE_TRAIN;
                    } else {
                        trainSize = Constant.SMALL_TRAIN;
                    }

                    Document traind=new Document();
//                    date,train_num,business_num,first_num,second_num
                    traind.put("date",date);
                    traind.put("train_num",trainNum);
                    traind.put("business_num",trainSize[0]);
                    traind.put("first_num",trainSize[1]);
                    traind.put("second_num",trainSize[2]);
                    trainList.add(traind);
                    if(trainList.size()==100000){
                        trainc.insertMany(trainList);
                        trainList.clear();
                    }
                }
                c1.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!trainList.isEmpty()){
            trainc.insertMany(trainList);
        }
        System.out.println("Initial train Successfully!");
    }

    public void initialRemainTicket(){
        MongoCollection<Document> remainc=database.getCollection("remain_ticket");
        remainc.drop();
        database.createCollection("remain_ticket");
        List<Document> remainList=new ArrayList<Document>();

        int business_seat = 30;
        int first_seat = 70;
        int second_seat = 100;
        int no_seat = 30;

        MongoCollection<Document> trainc=database.getCollection("train");
        FindIterable<Document> traini=trainc.find();
        MongoCursor traincursor=traini.iterator();
        while (traincursor.hasNext()){
            Document train=(Document)traincursor.next();
            //取出列车字段信息
            String date = train.getString("date");
            int train_num = train.getInteger("train_num");
            int business_num = train.getInteger("business_num");
            int first_num = train.getInteger("first_num");
            int second_num = train.getInteger("second_num");

            //计算每种座位类型的个数
            int business_remain = business_seat * business_num;
            int first_remain = first_seat * first_num;
            int second_remain = second_seat * second_num;
            int noSeat_remain = no_seat * second_num;

            MongoCollection<Document> routec=database.getCollection("route");
            FindIterable<Document> routei=routec.find();
            MongoCursor routecursor=routei.iterator();
            List<String> station_name = new ArrayList<String>();
            List<Integer> station_num=new ArrayList<Integer>();
            while (routecursor.hasNext()){
                Document route=(Document)routecursor.next();
                station_name.add(route.getString("station_name"));
                station_num.add(route.getInteger("station_num"));
            }
            System.out.println(station_name.size()+"!!!");
            //该车次对应的所有站名
            for (int i = 0; i < station_name.size(); i++) {
                for (int j = i+1; j < station_name.size(); j++) {
                    String departure = station_name.get(i);
                    String destination = station_name.get(j);
                    int departure_num=station_num.get(i);
                    int destination_num=station_num.get(j);
                    Document remaind=new Document();
//                  date,train_num,departure,destination,departure_num,destination_num,business_remain,
// first_remain,second_remain,noSeat_remain)
                    remaind.put("date",date);
                    remaind.put("train_num",train_num);
                    remaind.put("departure",departure);
                    remaind.put("destination",destination);
                    remaind.put("departure_num",departure_num);
                    remaind.put("destination_num",destination_num);
                    remaind.put("business_remain",business_remain);
                    remaind.put("first_remain",first_remain);
                    remaind.put("second_remain",second_remain);
                    remaind.put("noSeat_remain",noSeat_remain);
                    remaind.put("business_seat",0);
                    remaind.put("first_seat",0);
                    remaind.put("second_seat",0);
                    remaind.put("no_seat",0);
                    remainList.add(remaind);
                    if(remainList.size()==100000){
                        remainc.insertMany(remainList);
                        remainList.clear();
                    }
                    System.out.println(date+"  "+train_num);
                }
            }
        }

        if(!remainList.isEmpty()){
            remainc.insertMany(remainList);
        }
        System.out.println("Initial remain_ticket Successfully!");
    }

}

package DataImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Seven on 2016/11/9.
 */
public class DBConnector {
    //ip localhost:3306 database ticket_system user root password 123456 useUnicode=true charcterEncoding=gbk
    String mysqlIP="localhost:3306";
    String database="ticket_system";
    String user_name="root";
    String password="123456";

    public Connection getMySqlConnection() {
        Connection connection=null;
        String url="jdbc:mysql://"+mysqlIP+"/"+database+"?user="+user_name
                +"&password="+password+"&useUnicode=true&characterEncoding=utf8&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(url);
            System.out.println("Connect to database successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("Connect to database failed!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connect to database failed!");
            e.printStackTrace();
        }
        return connection;
    }

}

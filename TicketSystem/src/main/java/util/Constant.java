package util;

import DataImpl.DBConnector;
import DataImpl.MongoDBConnector;

/**
 * Created by Seven on 2016/11/9.
 */
public class Constant {
    public static final DBConnector connector=new DBConnector();
    public static final MongoDBConnector mongoconnector=new MongoDBConnector();
    public static final double BUSINESS_PRICE=1.234;
    public static final double FIRST_PRICE=0.775;
    public static final double SECOND_PRICE=0.485;

    public static final int[] SMALL_TRAIN={1,2,5};
    public static final int[] LARGE_TRAIN={2,4,10};

}

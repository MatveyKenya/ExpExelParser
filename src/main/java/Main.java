import Entity.Order;
import Util.DbConnector;
import Util.MyParserOrder1cXLS;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String... args){
        List<Order> orderList = MyParserOrder1cXLS.parse("отчеты 1с по июнь.xls");
        try {

            var db = new DbConnector();
            //System.out.println("Внесено записей " + db.insertNewOrder(orderList.get(5)));
            db.closeConnection();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}


// String[] ss = {"123-322/16", "125-458", "", "154", "4/10"};
//        for (String s : ss){
//            System.out.println(s.strip().matches("\\d{1,4}-\\d{3,4}(/\\d{1,2})?"));
//        }
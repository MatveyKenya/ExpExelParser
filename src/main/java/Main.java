import Entity.Order;
import Repository.AppConfig;
import Repository.OrderRepository;
import Util.DbConnector;
import Util.MyParserOrder1cXLS;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String... args){

        AppConfig cfg = AppConfig.getInstance();
        assert cfg != null;

        List<Order> orderList = MyParserOrder1cXLS.parse(cfg.getFile1Cxls());


        try {

            var db = new DbConnector();
            var repo = new OrderRepository(db);
            System.out.println("заказы в базе " + repo.getOrderAll());

            System.out.println("всего сохранено в базе заказов - " + repo.saveAfterParsing(orderList));

            System.out.println("заказы в базе " + repo.getOrderAll());

//            Order order = repo.getOrderById("330-322");
//            System.out.println(order);
//            order.setName("name");
//            order.setNumberId("894-622");
//            System.out.println(repo.save(order));

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
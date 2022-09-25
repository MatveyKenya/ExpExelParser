import Repository.OrderRepository;
import Util.DbConnector;
import ui.UiService;
import ui.UiTerminal;

import java.sql.SQLException;

public class Main {

    public static void main(String... args){

        try {

            var db = new DbConnector();
            var orderRepo = new OrderRepository(db);
            var uis = new UiService(orderRepo);
            var ui = new UiTerminal(uis);

            ui.start();

            db.closeConnection();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
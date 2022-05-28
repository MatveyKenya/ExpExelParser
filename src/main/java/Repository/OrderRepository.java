package Repository;

import Entity.Order;
import Util.DbConnector;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository{

    final private DbConnector db;

    public OrderRepository(@NonNull DbConnector db) {
        this.db = db;
    }

    //----PUBLIC--------------------------------------------

    public List<Order> getOrderAll(){
        return getOrderList("select * from \"order\"");
    }

    public Order getOrderById(String id){
        String query = "select * from \"order\" where id = '" + id + "';";
        List<Order> list = getOrderList(query);
        return list.size() > 0 ?
                list.get(0) : null;
    }

    /**
     * Универсально сохраняет с проверкой уникальности
     * переносит картинки на сервере с 1 локации в другую - по датам доставки
     * @param order - insert новый или update существующий
     * @return 1 - успешно занесен новый или обновлен существующий заказ; -1 ошибка
     */
    public int saveOrder(Order order){
        int result = 0;
        Order orderInDb = getOrderById(order.getNumberId());
        if (orderInDb == null){
            return insertNewOrder(order); // сохраняем новый заказ в базу
        }
        // иначе обновляем запись в базе, но с учетом старого заказа в базе
        // если поменялась дата доставки, то нужно и картинки перенести в новую локацию
        return updateOrder(order, orderInDb);
    }

    //-----------PRIVATE TERRITORY--------------------------

    private List<Order> getOrderList(String query){
        List<Order> list = new ArrayList<>();
        try (ResultSet rs = db.executeResult(query)){
            while(rs.next()){
                Order order = Order.builder()
                        .numberId(rs.getString("id"))
                        .date(rs.getDate("date"))
                        .name(rs.getString("name"))
                        .fio1c(rs.getString("fio"))
                        .tel(rs.getString("tel"))
                        .surname(rs.getString("surname"))
                        .address(rs.getString("address"))
                        .build();
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * добавить в базу Заказ
     * @param order только новый заказ.
     * @return 1 запись добавлена или -1 ошибка
     */
    private int insertNewOrder(Order order){
        String query = MessageFormat.format(
                "INSERT INTO public.\"order\" (id, date, name, fio, tel, surname, address)\n" +
                        "VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'');",
                order.getNumberId(),
                order.getDateToSQL(),
                order.getName(),
                order.getFio1c(),
                order.getTel(),
                order.getSurname(),
                order.getAddress());
        return db.execute(query);
    }

    private int updateOrder(Order order, Order oldOrderInDb){
        String query = MessageFormat.format("""
                                UPDATE public.\"order\"
                                SET date    = ''{0}'',
                                    name    = ''{1}'',
                                    fio     = ''{2}'',
                                    tel     = ''{3}'',
                                    surname = ''{4}'',
                                    address = ''{5}''
                                WHERE id LIKE ''{6}'' ESCAPE '#'""",
                order.getDateToSQL(),
                order.getName(),
                order.getFio1c(),
                order.getTel(),
                order.getSurname(),
                order.getAddress(),
                order.getNumberId());

        int result = db.execute(query);
        if (result > 0){
            order.removeJpg(oldOrderInDb);
        }
        return result;
//        UPDATE public."order"
//        SET date    = '2022-05-17',
//            name    = 'щкаф 2х',
//            fio     = 'Иванов АСрр',
//            tel     = '8-896-895',
//            surname = 'Петров',
//            address = 'Москва'
//        WHERE id LIKE '235-548' ESCAPE '#';
    }
}

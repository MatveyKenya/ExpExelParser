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
        return getOrderList("select * from \"order\";");
    }

    public Order getOrderById(String id){
        String query = "select * from \"order\" where id = '" + id + "';";
        List<Order> list = getOrderList(query);
        return list.size() > 0 ?
                list.get(0) : null;
    }

    public int save(Order order){
        return save(order, false);
    }

    /**
     * Сохраняет заказы после парсинга в базе
     * поэтому если в базе есть уже заказ, то обновляется только дата доставки!!!
     * @param orderList
     * @return
     */
    public int saveAfterParsing(List<Order> orderList){
        int result = 0;
        for (Order order: orderList){
            if (save(order, true) > 0){
                result++;
            }
        }
        return result;
    }

    //-----------PRIVATE TERRITORY--------------------------

    /**
     * Универсально сохраняет с проверкой уникальности
     * переносит картинки на сервере с 1 локации в другую - по датам доставки
     * @param order - insert новый или update существующий
     * @return 1 - успешно занесен новый или обновлен существующий заказ; -1 ошибка
     */
    private int save(Order order, boolean onlyDateUpdate){
        Order orderInDb = getOrderById(order.getNumberId());
        if (orderInDb == null){
            return insertNewOrder(order); // сохраняем новый заказ в базу
        }
        if (onlyDateUpdate){
            Order orderWithNewDate = orderInDb.clone();
            orderWithNewDate.setDate(order.getDate());
            order = orderWithNewDate;
        }
        // иначе обновляем запись в базе, но с учетом старого заказа в базе
        // если поменялась дата доставки, то нужно и картинки перенести в новую локацию
        return updateOrder(order, orderInDb);
    }

    private List<Order> getOrderList(String query){
        List<Order> list = new ArrayList<>();
        try (ResultSet rs = db.executeResult(query)){
            while(rs.next()){
                Order order = Order.builder()
                        .numberId(rs.getString("id"))
                        .date(rs.getDate("date"))
                        .dateGot(rs.getDate("date_got"))
                        .name(rs.getString("name"))
                        .fio1c(rs.getString("fio"))
                        .tel(rs.getString("tel"))
                        .surname(rs.getString("surname"))
                        .address(rs.getString("address"))
                        .type(Order.OrderType.valueOf(rs.getString("type")))
                        .stage(Order.OrderStage.valueOf(rs.getString("stage")))
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
                "INSERT INTO public.\"order\" (id, date, date_got, name, fio, tel, surname, address, type)\n" +
                        "VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'', ''{8}'');",
                order.getNumberId(),
                order.getDateToSQL(),
                order.getDateGotToSQL(),
                order.getName(),
                order.getFio1c(),
                order.getTel(),
                order.getSurname(),
                order.getAddress(),
                order.getType().toString());
        return db.execute(query);
    }

    private int updateOrder(Order order, Order oldOrderInDb){
        if (order.equals(oldOrderInDb)){
            return 0;
        }
        String query = MessageFormat.format("""
                                UPDATE public.\"order\"
                                SET date    = ''{0}'',
                                    date_got= ''{1}'',
                                    name    = ''{2}'',
                                    fio     = ''{3}'',
                                    tel     = ''{4}'',
                                    surname = ''{5}'',
                                    address = ''{6}'',
                                    type    = ''{7}'',
                                    stage   = ''{8}''
                                WHERE id LIKE ''{9}'' ESCAPE ''#'';
                                """,
                order.getDateToSQL(),
                order.getDateGotToSQL(),
                order.getName(),
                order.getFio1c(),
                order.getTel(),
                order.getSurname(),
                order.getAddress(),
                order.getType().toString(),
                order.getStage().toString(),
                order.getNumberId());
        //System.out.println(query);

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

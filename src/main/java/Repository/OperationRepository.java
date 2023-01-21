package Repository;

import Entity.Operation;
import Entity.Order;
import Util.DbConnector;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OperationRepository {

    final private DbConnector db;

    public OperationRepository(@NonNull DbConnector db) {
        this.db = db;
    }

    public List<Operation> getOperationByIdOrder(String idOrder){

        return null;
    }

    // Map   id Операции --- Фамилия исполнителя
    // соответсвенно правило
    // в пределах 1 заказа - 1 уникальная операция с 1м исполнителем
    public Map<Integer, String> getMapOperUser(String idOrder){
        String query = """
                        select top.id, u.surname from operation op
                        inner join "type_operation" top on top.id = op.id_type_operation
                        left join "user" u on u.id = id_user
                        where op."order" = '""" + idOrder + "';";
        HashMap<Integer, String> map = new HashMap<>();
        try (ResultSet rs = db.executeResult(query)){
            while(rs.next()){
                map.put(rs.getInt("id"),
                        rs.getString("surname") == null
                                ? "???" : rs.getString("surname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

}

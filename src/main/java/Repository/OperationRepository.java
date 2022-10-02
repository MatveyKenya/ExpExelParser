package Repository;

import Entity.Operation;
import Util.DbConnector;
import lombok.NonNull;

import java.util.List;

public class OperationRepository {

    final private DbConnector db;

    public OperationRepository(@NonNull DbConnector db) {
        this.db = db;
    }

    public List<Operation> getOperationByIdOrder(String idOrder){

        return null;
    }
}

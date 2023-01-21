package ui;

import Entity.Operation;
import Entity.Order;
import Repository.AppConfig;
import Repository.OperationRepository;
import Repository.OrderRepository;
import Util.MyParserOrder1cXLS;

import java.util.List;
import java.util.Map;


public class UiService {

    final private OrderRepository orderRepo;
    final private OperationRepository operationRepo;
    AppConfig cfg = AppConfig.getInstance();

    public UiService(OrderRepository orderRepo, OperationRepository operationRepo) {
        this.orderRepo = orderRepo;
        this.operationRepo = operationRepo;
    }

    public List<Order> getOrderAll(){
        return orderRepo.getOrderAll();
    }

    public int uploadFrom1C(){
        List<Order> orderList = MyParserOrder1cXLS.parse(cfg.getFile1Cxls());
        return orderRepo.saveAfterParsing(orderList);
    }

    public Order getOrder(String idOrder) {
        return orderRepo.getOrderById(idOrder);
    }

    public Map<Integer, String> getMapOperUser(String idOrder){
        return operationRepo.getMapOperUser(idOrder);
    }
}

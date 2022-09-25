package ui;

import Entity.Order;
import Repository.AppConfig;
import Repository.OrderRepository;
import Util.MyParserOrder1cXLS;

import java.util.List;


public class UiService {

    final private OrderRepository orderRepo;
    AppConfig cfg = AppConfig.getInstance();

    public UiService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> getOrderAll(){
        return orderRepo.getOrderAll();
    }

    public int uploadFrom1C(){
        List<Order> orderList = MyParserOrder1cXLS.parse(cfg.getFile1Cxls());
        return orderRepo.saveAfterParsing(orderList);
    }
}

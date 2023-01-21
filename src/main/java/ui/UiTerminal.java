package ui;

import Entity.Operation;
import Entity.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UiTerminal {
    final private UiService uis;
    private final Scanner scanner = new Scanner(System.in);
    private final String ZNAK_PUSTO = "-";

    public UiTerminal(UiService uis) {
        this.uis = uis;
    }

//    public static void main(String[] args) {
//        UiTerminal ui = new UiTerminal(new UiService());
//        ui.start();
//    }


    public void start(){
        System.out.println("*** БАЗА ЭЛЬФ ***");
        menuMain();
    }

    public void menuMain(){
        while (true){
            switch (
                    menuInput("""
                            
                            -----------------------
                            Введите номер действия:
                            0- Выход
                            1- Загрузить заказы с 1С
                            2- Заказы в базе (список)
                            3- Редактирование заказа
                            """)
            ){
                case "0" -> {
                    return;
                }
                case "1" -> {
                    System.out.println("Заказов загружено: " + uis.uploadFrom1C());
                }
                case "2" -> {
                    printTabOrder(uis.getOrderAll());
                }
                case "3" -> {
                    menuEditOrder();
                }
                default -> {
                    System.out.println("!!! ----введено неверное значение - повторите ввод-----");
                }
            }
        }


    }

    private String menuInput(String query){
        System.out.println(query);
        return scanner.nextLine().strip();
    }

    private void menuEditOrder() {
        while (true){
            String numberOrder = menuInput("Введите номер заказа (или 0 для выхода)");
            if (numberOrder.equals("0")){
                return;
            }
            if (!Order.isNumberOrder1cFormat(numberOrder)){
                System.out.println("!!! Формат номена заказа неверный --- введите заново");
                continue;
            }
            Order order = uis.getOrder(numberOrder);
            if (order == null){
                System.out.println("!!! Заказа с таким номером нет в базе --- введите заново");
                continue;
            }
            printTabOrder(order);
            switch (
                    menuInput("""
                            
                            -----------------------
                            Введите номер пункта редактирования:
                            0- Выход
                            1- Дата доставки
                            2- Фамилия
                            3- Операция (добавить/изменить/удалить)
                            """)
            ){
                case "0" -> {
                    return;
                }
                case "1" -> {
                    System.out.println("пункт 1 - на разработке");
                }
                case "2" -> {
                    System.out.println("пункт 2 - на разработке");
                }
                case "3" -> {
                    menuEditOperation(order);
                }
                default -> {
                    System.out.println("!!! ----введено неверное значение - повторите ввод-----");
                }
            }
        }

    }

    private void menuEditOperation(Order order) {
        Map<Integer, String> map = uis.getMapOperUser(order.getNumberId());
        while (true){
            String input = menuInput("""
                            
                            -----------------------
                            Введите номер пункта редактирования:
                            0- Выход       
                            1- деталировка
                            2- смета
                            3- мдф
                            4- замер
                            5- переработка
                            """);
            switch (input){
                case "0" -> {
                    return;
                }
                case "1", "2", "3", "4", "5" -> {
                    //uis.setUserToOperation(idOrder, idUser, idOperation);
                    System.out.println("1?2?3?4?5");
                }
                default -> {
                    System.out.println("!!! ----введено неверное значение - повторите ввод-----");
                }
            }
        }
    }

    //просто печать заказа на экран - в заданном формате
    private void printTabOrder(Order order) {
        printTabOrder(List.of(order));
    }

    private void printTabOrder(List<Order> list){
        if (list != null && list.size() > 0){
            //вывод шапки
            String format = "%10s|%10s|%10s|%10s|%10s|%10s|%10s|%n";
            String date = "";
            for (Order order: list){
                if (!order.getDateToSQL().equals(date)){
                    date = order.getDateToSQL();
                    System.out.println("---дата доставки---" + date);
                    System.out.printf(format, "№", "фамилия", "детал", "смета", "мдф", "замер", "перераб");
                }
                Map<Integer, String> map = uis.getMapOperUser(order.getNumberId());
                System.out.printf(
                        format,
                        order.getNumberId(),
                        order.getSurname(),
                        map.get(1) != null ? map.get(1) : ZNAK_PUSTO,
                        map.get(2) != null ? map.get(2) : ZNAK_PUSTO,
                        map.get(3) != null ? map.get(3) : ZNAK_PUSTO,
                        map.get(4) != null ? map.get(4) : ZNAK_PUSTO,
                        map.get(5) != null ? map.get(5) : ZNAK_PUSTO
                );


            }
        }
    }
}

package ui;

import Entity.Order;

import java.util.Scanner;

public class UiTerminal {
    final private UiService uis;
    private final Scanner scanner = new Scanner(System.in);

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
                    System.out.println("заказы в базе " + uis.getOrderAll());
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
                System.out.println("!!! Формат номена неверный --- введите заново");
                continue;
            }
            System.out.println(uis.getOrder(numberOrder));
            switch (
                    menuInput("""
                            
                            -----------------------
                            Введите номер пункта редактирования:
                            0- Выход
                            1- Дата доставки
                            2- Фамилия
                            3- ...
                            """)
            ){
                case "0" -> {
                    return;
                }
                case "1" -> {
                    System.out.println("пункт 1");
                }
                case "2" -> {
                    System.out.println("пункт 2");
                }
                case "3" -> {
                    System.out.println("пункт 3");
                }
                default -> {
                    System.out.println("!!! ----введено неверное значение - повторите ввод-----");
                }
            }
        }

    }
}

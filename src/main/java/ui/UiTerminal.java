package ui;

import java.util.Scanner;

public class UiTerminal {
    final private UiService uis;
    private Scanner scanner = new Scanner(System.in);

    public UiTerminal(UiService uis) {
        this.uis = uis;
    }

//    public static void main(String[] args) {
//        UiTerminal ui = new UiTerminal(new UiService());
//        ui.start();
//    }


    public void start(){
        System.out.println("*** БАЗА ЭЛЬФ ***");
        mainMenu();
    }

    public void mainMenu(){
        while (true){
            System.out.println("""
                            
                            -----------------------
                            Введите номер действия:
                            0- Выход
                            1- Загрузить заказы с 1С
                            2- Заказы в базе (список)
                            """);
            String input = scanner.nextLine();
            switch (input){
                case "0" -> {
                    return;
                }
                case "1" -> {
                    System.out.println("Заказов загружено: " + uis.uploadFrom1C());
                }
                case "2" -> {
                    System.out.println("заказы в базе " + uis.getOrderAll());
                }
            }
        }


    }
}

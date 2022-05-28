package Util;

import Entity.Order;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyParserOrder1cXLS {

    public static void main(String[] args) {
        MyParserOrder1cXLS.parse("отчеты 1с по июнь.xls").forEach(System.out::println);
    }

    public static List<Order> parse(String name) {

        final InputStream in;
        HSSFWorkbook wb = null;
        try {
            in = new FileInputStream(name);
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert wb != null;
        List<Order> list = new ArrayList<>();
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd.MM.yyyy");

        Sheet sheet = wb.getSheetAt(0);
        for (Row row : sheet) {
            //определяем это сама строчка заказа или строчка с датой
            Cell cell = row.getCell(1);
            if (cell != null){
                if (cell.getCellType() == CellType.STRING){
                    String text = cell.getStringCellValue();
                    String[] words = text.split(": ");
                    if (words[0].equals("Дата отгрузки")){
                        try {
                            date = dateFormat.parse(words[1]);
                        } catch (ParseException ex){
                            ex.printStackTrace();
                        }
                    }
                } else if (cell.getCellType() == CellType.NUMERIC){
                    cell = row.getCell(2);
                    if (cell != null
                            && cell.getCellType() == CellType.STRING
                            && Order.isNumberOrder1cFormat(cell.getStringCellValue())){
                        Order order = Order.builder()
                                .numberId(cell.getStringCellValue())
                                .date(date)
                                .name(getStringFromCell(row.getCell(3)))
                                .address(getStringFromCell(row.getCell(4)))
                                .tel(getStringFromCell(row.getCell(5)))
                                .fio1c(getStringFromCell(row.getCell(6)))
                                .surname(getStringFromCell(row.getCell(6)).split(" ")[0])
                                .build();

                        list.add(order);
                    } else {
                        System.out.println("Ошибка. Неверный формат номера в строке № " + row.getRowNum());
                    }

                }
            }


        }
        return list;
    }

    private static String getStringFromCell(Cell cell) {
        if (cell != null){
            if (cell.getCellType() == CellType.STRING){
                return cell.getStringCellValue();
            }
            if (cell.getCellType() == CellType.NUMERIC){
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "???";
    }

}

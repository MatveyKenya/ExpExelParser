package Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Заказ с уникальным номером и его параметры, даты
 */

@Builder
@Data
public class Order implements Cloneable{
    @NonNull
    private String numberId; // номер заказа типа String и формата isNumberOrder1cFormat !!!
    private Date date; // дата доставки/отгрузки
    private Date dateGot; //дата готовности
    private String name; // название изделий в заказе
    private String address; // адрес доставки
    private String tel; // телефоны заказчика
    private String fio1c; // Фамилия Заказчика или организации
    private String surname; // это должно быть проверенное значение для использования в имени файла!!!
    private OrderType type;
    private OrderStage stage;
    private boolean enable; // существует или удален TRUE or FALSE

    @Override
    public Order clone() {
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    //max 10 char
    public enum OrderType{
        CHEL, // Челябинск и Область
        NORTH // Северные регионы - Сургут, Нижневартовск
    }

    //max 20 char - стадия изготовления заказа
    public enum OrderStage {
        CREATED, // создан и находится в стадии документирования
        IN_WORK, // отдан в работу в цех
        CLOSED // отгружен на адрес
    }

    private static final SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM.yy");
    private static final SimpleDateFormat formatForDateDDMM = new SimpleDateFormat("dd.MM");
    private static final SimpleDateFormat formatForDate_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");


    public static boolean isNumberOrder1cFormat(@NonNull String number){
        return number.strip().matches("\\d{1,4}-\\d{3,4}(/\\d{1,2})?");
    }

    public String getDateToSQL(){
        return formatForDate_yyyyMMdd.format(date);
    }

    public String getDateGotToSQL(){
        return formatForDate_yyyyMMdd.format(dateGot.after(date) ? date : dateGot);
    }

    public String getFileJpgName(){
        return getNumberIdForFile() + " " + surname;
    }

    public String getNumberIdForFile(){
        String[] numbers = numberId.split("/");
        return numbers[0] + (numbers.length == 2 ? "-" + numbers[1] : "");
    }

    // совсем неуникальный номер, как раньше "18.05 155 Иванов"
    public String getOldFileJpgName(){
        return formatForDateDDMM.format(date) + " "
                + numberId.split("-")[0] + " "
                + (numberId.split("/").length == 2 ? "доз" + numberId.split("/")[1] + " " : "")
                + fio1c.split(" ")[0];
    }

    @Override
    public String toString(){
        return "\n" + getFileJpgName();
    }

    public String toStringAll(){
        int maxChars = 10;
        String separator = " | ";
        return formatForDate.format(date) + separator
                + numberId + separator
                + fio1c + separator
                + name.substring(0, Math.min(name.length(), maxChars)) + separator
                + address.substring(0, Math.min(address.length(), maxChars)) + separator
                + tel.substring(0, Math.min(tel.length(), maxChars));
    }


}

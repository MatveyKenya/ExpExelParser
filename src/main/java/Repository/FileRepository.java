package Repository;

import Entity.Order;
import org.apache.commons.io.FileUtils;

import javax.tools.FileObject;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс для работы с файлами Сканов формата name.jpg
 * отражающих набор сканов для заказа по определенному пути
 * где name формируется и проверяется по определенным правилам
 * жестко зависит от класса Order и обслуживает его
 */

public class FileRepository {

    final private String GLOBAL_PATH_TO_JPG;

    public FileRepository(){
        AppConfig cfg = AppConfig.getInstance();
        assert cfg != null;
        GLOBAL_PATH_TO_JPG = cfg.getGlobalPathToScan();
    }

    public static void main(String[] args) {

        String path = "D:/elf/Сканы/2022/СЕВЕР/(29) отгруз 29.06/185-7";
        File file = new File(path).getParentFile();

        FileFilter filter = path1 -> path1.getName().startsWith("185-7");

        System.out.println(Arrays.toString(file.listFiles(filter)));

    }

     /**
     * Перемещение файлов jpg по датам
     * делаем допущение, что имя файла .jpg сформировано этой программой
     * и поэтому распознано оно тоже должно быть исходя из правил именования
     */
    public int removeJpg(Order newOrder, Order oldOrderInDb) {
        if (newOrder.getDate().compareTo(oldOrderInDb.getDate()) == 0
                && newOrder.getType().equals(oldOrderInDb.getType())
                && newOrder.getSurname().equals(oldOrderInDb.getSurname())){
            return 0;
        }
        int result = 0;
        String newName = getFullFileJpgName(newOrder);
        String oldName = getFullFileJpgName(oldOrderInDb);
        File oldDir = new File(oldName).getParentFile();
        FileFilter filter = pathname -> pathname.getName().startsWith(oldOrderInDb.getNumberIdForFile());
        FileFilter filterOld = pathname -> pathname.getName().startsWith(oldOrderInDb.getOldFileJpgName());

        File[] files = oldDir.listFiles(filter);
        if (files == null || files.length == 0){ // если не нашли jpg по новому имени - ищем по старому
            files = oldDir.listFiles(filterOld);
        }

        if (files != null){
            int i=1;
            for (File file: files){
                String iString = iToString(i);
                File newFile = new File(newName + iString + ".jpg");
                if (!newFile.exists()){
                    if (moveFile(file, newFile)){
                        result++;
                    }
                    i++;
                } else {
                    break;
                }
            }
        }
        return result;
    }

    //---private territory-----------------------------------

    private String iToString(int i){
        if (i <= 0){
            return "";
        }
        return i < 10 ? "-0" + i : "-" + i;
    }

    public String getFullFileJpgName(Order order){
        String[] dates = order.getDateToSQL().split("-");
        return order.getType() == Order.OrderType.CHEL
                ?
                GLOBAL_PATH_TO_JPG + "/" +
                dates[0] + "/" +
                getMonthName(dates[1]) + "/" +
                dates[2] + "/" +
                order.getFileJpgName()
                :
                GLOBAL_PATH_TO_JPG + "/" +
                dates[0] + "/СЕВЕР/" +
                "(" + dates[1] + ") отгруз " +
                dates[2] + "." + dates[1] + "/" +
                order.getFileJpgName();
    }

    private String getMonthName(String numberOfMonth) {
        switch (numberOfMonth){
            case "01" -> {return "(01) январь";}
            case "02" -> {return "(02) февраль";}
            case "03" -> {return "(03) март";}
            case "04" -> {return "(04) апрель";}
            case "05" -> {return "(05) май";}
            case "06" -> {return "(06) июнь";}
            case "07" -> {return "(07) июль";}
            case "08" -> {return "(08) август";}
            case "09" -> {return "(09) сентябрь";}
            case "10" -> {return "(10) октябрь";}
            case "11" -> {return "(11) ноябрь";}
            case "12" -> {return "(12) декабрь";}
        }
        return null;
    }

    private static boolean moveFile(File oldFile, File newFile){
        try {
            FileUtils.moveFile(oldFile, newFile);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

package Repository;

import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 04/06/2022
 * Класс хранит все настройки программы.
 * Считывает их с диска с файла app-properties.xml - который должен быть!!
 */

@Data
public class AppConfig {

    private AppConfig(){}

    private static final String FILE_PROPERTIES = "app-properties.xml";

    private static AppConfig cfg;
    private static FileInputStream fis;
    private static Properties property = new Properties();

    private String dbUser;
    private String dbPassword;
    private String dbUrl;
    private String globalPathToScan; // путь до папки Сканы, где папки года 2022, 2023...
    private String file1Cxls;
    private String[] listNorthKeyWords;

    public static AppConfig getInstance(){
        if (cfg != null){
            return cfg;
        }
        try {
            fis = new FileInputStream(FILE_PROPERTIES);
            property.loadFromXML(fis);
        } catch (IOException e) {
            System.err.println("Error! file properties not found");
            return null;
        }
        cfg = new AppConfig();

        cfg.dbUser = property.getProperty("dbUser");        //cfg.dbUser = "postgres";
        cfg.dbPassword =property.getProperty("dbPassword");        //cfg.dbPassword ="0123";
        cfg.dbUrl = property.getProperty("dbUrl");        //cfg.dbUrl = "jdbc:postgresql://localhost:5432/elf";
        cfg.globalPathToScan = property.getProperty("globalPathToScan");        //cfg.globalPathToScan = "D:/elf/Сканы";
        cfg.file1Cxls = property.getProperty("file1Cxls");        //cfg.file1Cxls = "D:/elf/Отчеты 1С/отчет 1с.xls";
        cfg.listNorthKeyWords = property.getProperty("listNorthKeyWords").split(",");        //cfg.listNorthKeyWords = new String[]{"Сургут", "Нижневартовск"};

        return cfg;
    }

}

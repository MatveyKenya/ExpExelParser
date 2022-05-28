package arhiv;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Parser {

    public static String parse(String name) {

//        StringBuilder result = new StringBuilder();
//        InputStream in;
//        HSSFWorkbook wb = null;
//        try {
//            in = new FileInputStream(name);
//            wb = new HSSFWorkbook(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        assert wb != null;
//        Sheet sheet = wb.getSheetAt(0);
//        for (Row row : sheet) {
//            for (Cell cell : row) {
//                switch (cell.getCellType()) {
//                    case BLANK -> result.append("[x]");
//                    case STRING -> result.append(cell.getStringCellValue()).append("=");
//                    case NUMERIC, FORMULA -> result.append("[").append(cell.getNumericCellValue()).append("]");
//                    default -> result.append("|");
//                }
//            }
//            result.append("\n");
//        }

        return null;
    }

}

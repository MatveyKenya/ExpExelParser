package Repository;

import Entity.Order;

public class FileRerository {

    final private String GLOBAL_PATH_TO_JPG;

    public FileRerository(String GlobalPathToJpg) {
        GLOBAL_PATH_TO_JPG = GlobalPathToJpg;
    }

    public boolean renameJpgFiles(Order newOrder, Order oldOrder){
        String newName = newOrder.getFileJpgName();
        String oldName = oldOrder.getFileJpgName();



        return true;
    }
}

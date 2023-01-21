package Entity;

import lombok.Data;
import lombok.NonNull;

/**
 *  список работ для заказа
 */

@Data
public class Operation {

    private long id;
    private int idOrder; // не может null
    private int idTypeOperation; // не может быть null
    private int idUser; // может быть null -- т.е. исполнитель не назначен
    private int cost; // стоимость/трудозатраты в каких то единицах - (в нормоминутах например).

}

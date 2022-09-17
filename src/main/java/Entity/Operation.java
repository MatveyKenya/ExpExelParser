package Entity;

import lombok.Data;

/**
 *  список работ для заказа
 */

@Data
public class Operation {

    private long id;
    private int idOrder;
    private int idUser;
    private int cost; // стоимость/трудозатраты в каких то единицах

}

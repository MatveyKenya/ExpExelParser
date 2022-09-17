package Entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {

    private String name;
    private String surname;
    private Date birthday;
    private String telephone;
    private String address;
    private boolean enable;


}

package pojo;

import java.util.List;
import lombok.Data;

@Data
public class CreateOrderRequest {
    public enum Color {
        BLACK,
        GREY
    }

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<Color> color;
}

package Model;

import lombok.Getter;

@Getter
public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;


    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}


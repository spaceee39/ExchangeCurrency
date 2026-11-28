package Model;

import DTO.CurrencyRequestDto;
import DTO.CurrencyResponseDto;
import lombok.Getter;

@Getter
public class Currency {
    private Long id;
    private String code;
    private String name;
    private String sign;


    public Currency(Long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        name = fullName;
        this.sign = sign;
    }

    public Currency(CurrencyResponseDto currencyResponseDto) {
    id = currencyResponseDto.id();
    code = currencyResponseDto.code();
    name = currencyResponseDto.name();
    sign = currencyResponseDto.sign();
    }

    public Currency(CurrencyRequestDto currencyRequestDtoDto) {
        code = currencyRequestDtoDto.code();
        name = currencyRequestDtoDto.fullName();
        sign = currencyRequestDtoDto.sign();
    }

}


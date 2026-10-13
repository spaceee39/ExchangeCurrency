package Model;

import DTO.CurrencyRequestDto;
import DTO.CurrencyResponseDto;
import lombok.Getter;

@Getter
public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private String sign;


    public Currency(Long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(CurrencyResponseDto currencyResponseDto) {
    id = currencyResponseDto.id();
    code = currencyResponseDto.code();
    fullName = currencyResponseDto.fullName();
    sign = currencyResponseDto.sign();
    }

    public Currency(CurrencyRequestDto currencyRequestDtoDto) {
        code = currencyRequestDtoDto.code();
        fullName = currencyRequestDtoDto.fullName();
        sign = currencyRequestDtoDto.sign();
    }

}


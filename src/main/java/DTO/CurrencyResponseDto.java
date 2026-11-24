package DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","name","code","sign"})
public record CurrencyResponseDto(
        Long id,
        String name,
        String code,
        String sign
) {}

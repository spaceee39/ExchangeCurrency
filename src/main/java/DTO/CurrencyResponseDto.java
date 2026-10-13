package DTO;

public record CurrencyResponseDto(
        Long id,
        String fullName,
        String code,
        String sign
) {}

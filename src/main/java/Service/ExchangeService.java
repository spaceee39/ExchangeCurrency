package Service;

import DAO.ExchangeRatesDao;
import DTO.ExchangeRequestDto;
import DTO.ExchangeResponseDto;
import Exceptions.ExchangeRateNotFoundException;
import Model.Exchange;

public class ExchangeService {

    ExchangeRatesDao exchangeRatesDao;

    public ExchangeResponseDto makeExchange(ExchangeRequestDto exchangeRequestDto) {
        try {
            Exchange exchange = new Exchange(exchangeRatesDao.findByCodes(exchangeRequestDto.baseCurrencyCode(), exchangeRequestDto.targetCurrencyCode()), exchangeRequestDto.amount());
            return new ExchangeResponseDto(exchange.getExchangeRate(), exchange.getAmount(), exchange.getConvertedAmount());
        } catch (ExchangeRateNotFoundException e){
            Exchange exchange = new Exchange(exchangeRatesDao.findByCodes(exchangeRequestDto.targetCurrencyCode(), exchangeRequestDto.baseCurrencyCode()), exchangeRequestDto.amount());
            return new ExchangeResponseDto(exchange.getExchangeRate(), exchange.getAmount(), exchange.getConvertedAmount());
        }
    }

    public ExchangeService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }
}

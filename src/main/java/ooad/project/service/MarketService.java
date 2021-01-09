package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service

public class MarketService {
    @Autowired
    private MarketRepository marketRepository;

    public Market searchMarket(String marketName){
        return marketRepository.findMarketByMarketName(marketName);
    }

}

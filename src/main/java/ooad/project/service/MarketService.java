package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.regulatoryTask.CheckResult;
import ooad.project.domain.regulatoryTask.SelfCheckTask;
import ooad.project.domain.regulatoryTask.SpotCheckTask;
import ooad.project.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service

public class MarketService {
    @Autowired
    private MarketRepository marketRepository;

    public Market searchMarket(String marketName){
        return marketRepository.findMarketByMarketName(marketName);
    }

}

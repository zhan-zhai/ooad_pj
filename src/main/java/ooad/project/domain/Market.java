package ooad.project.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MARKET")

public class Market implements Serializable {

    private static final long serialVersionUID = 2257836055582296567L;

    @Id
    @Column(name = "MARKET_ID")
    private int marketId;

    @Column(name = "MARKET_NAME")
    private String marketName;

    @Column(name = "PERSONLIABLE_NAME")
    private String personLiableName;

    public Market() {
    }

    public Market(int marketId, String marketName, String personLiableName) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.personLiableName = personLiableName;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getPersonLiableName() {
        return personLiableName;
    }

    public void setPersonLiableName(String personLiableName) {
        this.personLiableName = personLiableName;
    }
}

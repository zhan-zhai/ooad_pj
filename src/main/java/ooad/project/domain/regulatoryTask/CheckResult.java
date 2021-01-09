package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;

import java.util.Date;

/**
 * 对应于每一个产品类别的检查结果
 */
public class CheckResult {
    private Market market;
    private ProductsType productsType;
    private int nonconforming;
    private Date checkTime;

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public ProductsType getProductsType() {
        return productsType;
    }

    public void setProductsType(ProductsType productsType) {
        this.productsType = productsType;
    }

    public int getNonconforming() {
        return nonconforming;
    }

    public void setNonconforming(int nonconforming) {
        this.nonconforming = nonconforming;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public CheckResult() {
    }

    public CheckResult(Market market, ProductsType productsType, int nonconforming, Date checkTime) {
        this.market = market;
        this.productsType = productsType;
        this.nonconforming = nonconforming;
        this.checkTime = checkTime;
    }
}

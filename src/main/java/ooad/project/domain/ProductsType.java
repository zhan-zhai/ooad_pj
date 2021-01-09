package ooad.project.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "PRODUCTSTYPE")

public class ProductsType implements Serializable {
    private static final long serialVersionUID = 8583126211079429855L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCTSTYPE_ID")
    private int productsTypeId;

    @Column(name = "PRODUCTSTYPE_NAME")
    private String productsTypeName;

    public ProductsType() {
    }

    public ProductsType(String productsTypeName) {
        this.productsTypeName = productsTypeName;
    }

    public int getProductsTypeId() {
        return productsTypeId;
    }

    public void setProductsTypeId(int productsTypeId) {
        this.productsTypeId = productsTypeId;
    }

    public String getProductsTypeName() {
        return productsTypeName;
    }

    public void setProductsTypeName(String productsTypeName) {
        this.productsTypeName = productsTypeName;
    }
}

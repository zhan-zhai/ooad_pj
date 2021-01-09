package ooad.project.repository;

import ooad.project.domain.ProductsType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductsTypeRepository extends CrudRepository<ProductsType,Long> {
    ProductsType findProductsTypeByProductsTypeName(String productsTypeName);
}

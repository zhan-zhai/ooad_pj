package ooad.project.service;

import ooad.project.domain.ProductsType;
import ooad.project.repository.ProductsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service

public class ProductsTypeService {
    @Autowired
    private ProductsTypeRepository productsTypeRepository;

    public ProductsType searchProductsType(String productsTypeName){
        return productsTypeRepository.findProductsTypeByProductsTypeName(productsTypeName);
    }
}

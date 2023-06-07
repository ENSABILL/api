package ma.ensa.ebanking.services;


import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ProductDto;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.Product;
import ma.ensa.ebanking.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<ProductDto> getAllProducts(){

        return repository
                .findAll()
                .stream()
                .map(
                      product -> ProductDto.builder()
                              .id(product.getId())
                              .name(product.getName())
                              .price(product.getPrice())
                              .agencyName(product.getAgency().getName())
                              .qte(product.getQte())
                              .build()
                ).toList();
    }

    public String addProduct(ProductDto dto) throws Exception{

        Agent agent = AuthService.Auths.getAgent();

        Product product = Product.builder()
                .name(dto.getName())
                .qte(dto.getQte())
                .price(dto.getPrice())
                .agency(agent.getAgency())
                .build();

        return repository
                .save(product) // save the product object
                .getId();      // return the id

    }

    public void deleteProduct(String productId) throws Exception{

        Agent agent = AuthService.Auths.getAgent();

        Product product = repository
                .findById(productId)
                .orElseThrow(
                        () -> new RecordNotFoundException("product not found")
                );

        if(!product.getAgency().getImm().equals(agent.getAgency().getImm())){
            throw new PermissionException();
        }

        repository.delete(product);

    }

}
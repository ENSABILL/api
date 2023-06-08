package ma.ensa.ebanking.services;


import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ProductDto;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.Product;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<ProductDto> getAllProducts(String imm){

        // check the authentication
        AuthService.Auths.getUser();

        List<Product> products = imm == null ?
                repository.findAll() :
                repository.findAllByAgency_Imm(imm);

        return products
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

    public String addProduct(ProductDto dto){

        Agent agent = AuthService.Auths.getAgent();

        Product product = Product.builder()
                .name(dto.getName())
                .qte(dto.getQte())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .agency(agent.getAgency())
                .build();

        return repository
                .save(product) // save the product object
                .getId();      // return the id

    }

    public void deleteProduct(String productId){

        Product product = repository
                .findById(productId)
                .orElseThrow(
                        () -> new RecordNotFoundException("product not found")
                );

        String agencyImm = product.getAgency().getImm();

        if(AuthService.Auths.checkAgentBelongsToAgency(agencyImm)){
            throw new PermissionException();
        }

        repository.delete(product);

    }


    public ProductDto getProduct(String productId) {

        AuthService.Auths.getUser();

        Product product = repository
                .findById(productId)
                .orElseThrow(
                        () -> new RecordNotFoundException("product not found")
                );

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .agencyName(product.getAgency().getName())
                .qte(product.getQte())
                .build();

    }
}
package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ProductDto;
import ma.ensa.ebanking.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProduct(@RequestParam(required = false) String imm){
        return service.getAllProducts(imm);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable String productId){
        return service.getProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addProduct(@RequestBody ProductDto dto){

        String productId = service.addProduct(dto);

        return String.format("""
                product created successfully
                product id: %s
                """, productId
        );
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteProduct(@PathVariable String productId) throws Exception{

        service.deleteProduct(productId);

        return String.format("""
                product %s deleted successfully
                """, productId
        );
    }


}
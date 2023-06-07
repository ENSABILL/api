package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.OrderDto;
import ma.ensa.ebanking.enums.OrderStatus;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.PaymentAccount;
import ma.ensa.ebanking.models.Product;
import ma.ensa.ebanking.models.ProductOrder;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.OrderRepository;
import ma.ensa.ebanking.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final PaymentService paymentService;

    public String placeOrder(OrderDto dto){

        Client client = AuthService.Auths.getClient();

        Product product = productRepository
                .findById(dto.getProductId())
                .orElseThrow(
                        () -> new RecordNotFoundException("product not found")
                );

        if(product.getQte() < dto.getOrderQte()){
            throw new RuntimeException("out of stock");
        }

        paymentService.transfer(
                product.getAgency(),
                dto.getOrderQte() * product.getPrice()
        );

        productRepository.decrementQte(
                product.getId(),
                dto.getOrderQte()
        );

        ProductOrder order = ProductOrder.builder()
                .client(client)
                .product(product)
                .qte(dto.getOrderQte())
                .build();

        return orderRepository
                .save(order)
                .getId();

    }

    public void updateOrderStatus(OrderDto dto) {

        // check auth
        ProductOrder order = orderRepository
                .findById(dto.getOrderId())
                .orElseThrow(
                        () -> new RecordNotFoundException("order not found")
                );

        AuthService.Auths.checkAgentBelongsToAgency(
                order.getProduct().getAgency().getImm()
        );


        OrderStatus status = Enum.valueOf(
                OrderStatus.class,
                dto.getStatus().toUpperCase()
        );

        orderRepository.updateState(
                dto.getOrderId(),
                status
        );

    }
}
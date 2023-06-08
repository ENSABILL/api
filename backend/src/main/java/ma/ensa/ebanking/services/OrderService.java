package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ClientDto;
import ma.ensa.ebanking.dto.OrderDto;
import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.enums.OrderStatus;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.Product;
import ma.ensa.ebanking.models.ProductOrder;
import ma.ensa.ebanking.models.user.Admin;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.OperationRepository;
import ma.ensa.ebanking.repositories.OrderRepository;
import ma.ensa.ebanking.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final PaymentService paymentService;

    private final OperationRepository operationRepository;

    public List<String> placeOrders(List<OrderDto> dtoList){

        final Client client = AuthService.Auths.getClient();

        double totalAmount = 0;

        for(OrderDto dto: dtoList){

            Product product = productRepository
                    .findById(dto.getProductId())
                    .orElseThrow(
                            () -> new RecordNotFoundException("product not found")
                    );

            if(product.getQte() < dto.getOrderQte()){
                throw new RuntimeException("out of stock");
            }

            totalAmount += dto.getOrderQte() * product.getPrice();
        }

        paymentService.checkBalanceAndLimit(totalAmount);

        // TODO: need an optimization for the performance
        return dtoList.stream().map(
            dto -> {
                Product product = productRepository
                        .findById(dto.getProductId())
                        .orElseThrow(
                                () -> new RecordNotFoundException("product not found")
                        );

                double amount = dto.getOrderQte() * product.getPrice();

                paymentService.transfer(
                        product.getAgency(),
                        amount
                );

                Operation operation = Operation.builder()
                        .client(client)
                        .product(product)
                        .amount(amount)
                        .operationStatus(OperationStatus.PAID)
                        .build();

                operationRepository.save(operation);

                ProductOrder order = ProductOrder.builder()
                        .client(client)
                        .product(product)
                        .qte(dto.getOrderQte())
                        .build();

                return orderRepository
                        .save(order)
                        .getId();
            }
        ).toList();

    }

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

        double amount = dto.getOrderQte() * product.getPrice();

        paymentService.transfer(
                product.getAgency(),
                amount
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

        Operation operation = Operation.builder()
                .client(client)
                .amount(amount)
                .product(product)
                .operationStatus(OperationStatus.PAID)
                .build();

        operationRepository.save(operation);

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

    public List<OrderDto> getAllOrders() {

        AuthService.Auths.getUser();

        List<ProductOrder> orders;

        if(AuthService.Auths.checkAdmin()){
            orders = orderRepository.findAll();
        }else if(AuthService.Auths.checkAgent()){
            orders = orderRepository
                    .findAllByProduct_Agency(
                            AuthService.Auths.getAgent().getAgency()
                    );
        }else{
            orders = orderRepository.findAllByClient(
                    AuthService.Auths.getClient()
            );
        }

        return orders
                .stream()
                .map(
                        order -> OrderDto.builder()
                                .orderId(order.getId())
                                .orderQte(order.getQte())
                                .status(order.getOrderStatus().toString())
                                .productId(order.getProduct().getId())
                                .client(
                                        ClientDto.builder()
                                                .fullName(order.getClient().getFullName())
                                                .username(order.getClient().getUsername())
                                                .phoneNumber(order.getClient().getPhoneNumber())
                                                .build()
                                )
                                .build()
                ).toList();




    }
}
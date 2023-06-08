package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.OrderDto;
import ma.ensa.ebanking.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(){


        return service.getAllOrders();

    }

    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> placeOrders(@RequestBody List<OrderDto> dtoList){
        return service.placeOrders(dtoList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderDto dto){
        String orderId = service.placeOrder(dto);
        return String.format(
                """
                order placed successfully
                order id : %s
                """,
                orderId
        );
    }

    @PutMapping("/update_status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateStatus(
            @RequestBody OrderDto dto
    ){
        service.updateOrderStatus(dto);
        return "order status updated successfully";
    }

}
package com.example.tacocloud.tacos;

import com.example.tacocloud.jdbc.JdbcOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@RequestMapping("/orders")
@Controller
@Slf4j
@SessionAttributes("tacoOrder")
public class OrderController {

    private final JdbcOrderRepository jdbcOrderRepository;
    @Autowired
    public OrderController(JdbcOrderRepository jdbcOrderRepository){
        this.jdbcOrderRepository = jdbcOrderRepository;
    }
    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus){
        if(errors.hasErrors()){
            return "orderForm";
        }
        jdbcOrderRepository.save(order);
        log.info("Order submitted: {}", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}

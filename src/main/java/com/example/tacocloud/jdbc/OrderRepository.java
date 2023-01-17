package com.example.tacocloud.jdbc;

import com.example.tacocloud.tacos.TacoOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<TacoOrder,Long> {

}

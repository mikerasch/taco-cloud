package com.example.tacocloud.jdbc;

import com.example.tacocloud.tacos.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}

package com.example.tacocloud.jdbc;

import com.example.tacocloud.tacos.Ingredient;
import com.example.tacocloud.tacos.IngredientRef;
import com.example.tacocloud.tacos.Taco;
import com.example.tacocloud.tacos.TacoOrder;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository{
    private JdbcOperations jdbcOperations;
    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations){
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO Taco_Order "
                + "(delivery_name, delivery_street, delivery_city, "
                + "delivery_state, delivery_zip, cc_number, "
                + "cc_expiration, cc_cvv, placed_at)"
                + "VALUES (?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);

        tacoOrder.setPlacedAt(new Date());
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        tacoOrder.getDeliveryName(),
                        tacoOrder.getDeliveryStreet(),
                        tacoOrder.getDeliveryCity(),
                        tacoOrder.getDeliveryState(),
                        tacoOrder.getDeliveryZip(),
                        tacoOrder.getCcNumber(),
                        tacoOrder.getCcExpiration(),
                        tacoOrder.getCcCVV(),
                        tacoOrder.getPlacedAt()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc,keyHolder);
        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);
        List<Taco> tacos = tacoOrder.getTacos();

        int i = 0;
        for(Taco taco: tacos){
            saveTaco(orderId,i++,taco);
        }

        return tacoOrder;
    }

    private Taco saveTaco(long orderId, int orderKey, Taco taco) {
        taco.setDateCreated(new Date());

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO Taco "
                + "(name, created_at, taco_order, taco_order_key) "
                + "VALUES (?, ?, ?, ?)",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getDateCreated(),
                        orderId,
                        orderKey
                )
        );
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc,generatedKeyHolder);
        long tacoId = generatedKeyHolder.getKey().longValue();
        taco.setId(tacoId);
        saveIngredients(tacoId,taco.getIngredients());
        return taco;
    }

    private void saveIngredients(long tacoId, List<IngredientRef> ingredients) {
        int key = 0;
        for(IngredientRef ingredient: ingredients){
            jdbcOperations.update(
                    "INSERT INTO Ingredient_Ref (ingredient, taco, taco_key) "
                    + "VALUES (?,?,?)",
                    ingredient.getIngredient(), tacoId,key++
            );
        }
    }
}

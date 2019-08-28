package com.yupaits.sample.seata.order.controller;

import com.yupaits.sample.seata.order.model.Order;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author yupaits
 * @date 2019/8/28
 */
@Slf4j
@RestController
public class OrderController {
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static final String USER_ID = "U100001";
    private static final String COMMODITY_CODE = "C00321";

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderController(JdbcTemplate jdbcTemplate, RestTemplate restTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/order")
    public String order(@RequestParam String userId, @RequestParam String commodityCode, @RequestParam int orderCount) {
        log.info("Order Service Begin - xid: {}", RootContext.getXID());
        int orderMoney = calculate(commodityCode, orderCount);
        invokerAccountService(orderMoney);
        final Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(orderCount);
        order.setMoney(orderMoney);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(
                    "insert into order_tbl (user_id, commodity_code, count, money) values (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            pst.setObject(1, order.getUserId());
            pst.setObject(2, order.getCommodityCode());
            pst.setObject(3, order.getCount());
            pst.setObject(4, order.getMoney());
            return pst;
        }, keyHolder);
        order.setId(keyHolder.getKey().longValue());
        if (RandomUtils.nextBoolean()) {
            throw new RuntimeException("This is a mock Exception.");
        }
        log.info("Order service End - Created {}", order);
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

    private int calculate(String commodityId, int orderCount) {
        return 2 * orderCount;
    }

    private void invokerAccountService(int orderMoney) {
        String url = "http://127.0.0.1:8131/account";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", USER_ID);
        map.add("money", orderMoney + "");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    }
}

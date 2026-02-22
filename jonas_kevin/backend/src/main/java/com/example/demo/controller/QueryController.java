package com.example.demo.controller;

import com.example.demo.readmodel.ShiggyReadModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class QueryController {
    private final ShiggyReadModel readModel;

    public QueryController(ShiggyReadModel readModel) {
        this.readModel = readModel;
    }

    @GetMapping("/status")
    public ShiggyReadModel getStatus() {
        return readModel;
    }
}

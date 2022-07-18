package com.quconsumer.controller;

import com.quconsumer.Service.QuPostConsumerService;
import com.quconsumer.dto.user.response.QuGetAllPostResponseDTO;
import com.quconsumer.enumeration.PathConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class QuUserConsumer {
    @Autowired
    QuPostConsumerService quUserConsumerService;
    String resourceUrl = "http://localhost:8082/";

    @GetMapping(PathConstants.GET_POST_FROM_QU)
    public List<QuGetAllPostResponseDTO> createProductWithExchange(Integer pageNo) {
        return quUserConsumerService.quGetAllPost(resourceUrl + PathConstants.QU_GET_ALL_POST, pageNo);
    }
}

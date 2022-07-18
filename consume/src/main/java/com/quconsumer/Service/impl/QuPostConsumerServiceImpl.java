package com.quconsumer.Service.impl;

import com.quconsumer.Service.QuPostConsumerService;
import com.quconsumer.dto.user.response.QuGetAllPostResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class QuPostConsumerServiceImpl implements QuPostConsumerService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<QuGetAllPostResponseDTO> quGetAllPost(String pathUrl, Integer pageNo) {
        if (restTemplate == null) {
            System.out.println("restTemplate is null");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<String>(headers);
        List<QuGetAllPostResponseDTO> response = restTemplate.postForObject(pathUrl, request, List.class);
        return  response;
    }
}
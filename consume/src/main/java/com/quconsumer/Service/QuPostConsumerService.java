package com.quconsumer.Service;

import com.quconsumer.dto.user.response.QuGetAllPostResponseDTO;
import java.util.List;

public interface QuPostConsumerService {
    List<QuGetAllPostResponseDTO> quGetAllPost(String pathUrl, Integer pageNo);
}
package com.productdock.adapter.in.web;

import com.productdock.adapter.in.web.dto.GetBookDto;
import com.productdock.adapter.in.web.mapper.TopicDtoMapper;
import com.productdock.application.port.in.GetTopicQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api/catalog/topics")
record GetTopicApi(GetTopicQuery getTopicQuery, TopicDtoMapper topicDtoMapper) {

    @GetMapping
    public Collection<GetBookDto.TopicDto> getTopics() {
        log.debug("GET request received - api/catalog/topics");
        var topics = getTopicQuery.getAll();
        return topicDtoMapper.toDtoCollection(topics);
    }
}

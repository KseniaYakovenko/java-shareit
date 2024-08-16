package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Request;

import java.util.Collections;

public class RequestDtoMapper {
    private RequestDtoMapper() {
    }

    public static RequestInfoDto toRequestInfoDto(Request request) {
        return new RequestInfoDto(
                request.getId(),
                request.getDescription(),
                request.getRequestorId(),
                request.getCreatedDate(),
                Collections.emptyList()
        );
    }

    public static Request toRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        return request;
    }
}
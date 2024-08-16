package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;

import java.util.Collection;

public interface RequestService {

        RequestInfoDto create(RequestDto requestDto, long userId);

        RequestInfoDto getByRequestId(long itemId, long userId);

        Collection<RequestInfoDto> findAllRequestsByUserId(long userId);

        Collection<RequestInfoDto> findAll();
}
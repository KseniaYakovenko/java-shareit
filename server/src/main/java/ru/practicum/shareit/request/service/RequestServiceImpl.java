package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoMapper;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.request.dto.RequestDtoMapper.toRequest;
import static ru.practicum.shareit.request.dto.RequestDtoMapper.toRequestInfoDto;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestInfoDto create(RequestDto requestDto, long userId) {
        Request request = toRequest(requestDto);
        User requestor = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Нет пользователя с id = " + userId));
        request.setRequestorId(userId);
        request.setCreatedDate(LocalDateTime.now());
        return toRequestInfoDto(requestRepository.save(request));
    }

    @Override
    public RequestInfoDto getByRequestId(long requestId, long userId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Нет request с id = " + requestId));
        List<ItemInfoDto> items = itemRepository.findByRequestId(requestId)
                .stream().map(ItemDtoMapper::toItemInfoDto).toList();
        RequestInfoDto requestInfoDto = toRequestInfoDto(request);
        requestInfoDto.setItems(items);
        return requestInfoDto;
    }

    @Override
    public Collection<RequestInfoDto> findAllRequestsByUserId(long userId) {
        return requestRepository.findByRequestorId(userId).stream().map(RequestDtoMapper::toRequestInfoDto).toList();
    }

    @Override
    public Collection<RequestInfoDto> findAll() {
        return requestRepository.findAll().stream().map(RequestDtoMapper::toRequestInfoDto).toList();
    }
}
package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@SpringBootTest
public class RequestServiceImplITest {
    @Autowired
    private EntityManager em;
    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;

    @Test
    void create() {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("description");

        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest60@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto user = userService.create(userDto);
        Long userId = user.getId();
        RequestInfoDto createdRequest = requestService.create(requestDto, userId);

        Assertions.assertNotEquals(createdRequest.getId(), null);
        Assertions.assertEquals("description", createdRequest.getDescription());
    }

    @Test
    void getById() {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("description");

        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest80@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto user = userService.create(userDto);
        Long userId = user.getId();
        RequestInfoDto createdRequest = requestService.create(requestDto, userId);

        Long requestId = createdRequest.getId();
        RequestInfoDto request = requestService.getByRequestId(requestId, userId);
        Assertions.assertEquals(requestId, request.getId());
    }

    @Test
    void getByUserId() {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("description");

        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest70@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto user = userService.create(userDto);
        Long userId = user.getId();
        RequestInfoDto createdRequest = requestService.create(requestDto, userId);

        Long requestId = createdRequest.getId();
        Collection<RequestInfoDto> requests = requestService.findAllRequestsByUserId(userId);
        Assertions.assertTrue(requests.size() > 0);
    }

    @Test
    void getAll() {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("description");

        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest70@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto user = userService.create(userDto);
        Long userId = user.getId();
        RequestInfoDto createdRequest = requestService.create(requestDto, userId);

        Long requestId = createdRequest.getId();
        Collection<RequestInfoDto> requests = requestService.findAll();
        List<Object> requestList = em.createNativeQuery("select * from requests").getResultList();
        System.out.println(requestList);
        Assertions.assertEquals(requestList.size(), requests.size());
    }


}
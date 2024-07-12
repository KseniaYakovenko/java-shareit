package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long generatorId = 0L;

    public Long generateId() {
        return ++generatorId;
    }

    @Override
    public User create(User user) {
        checkEmail(user);
        Long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        return users.get(userId);
    }

    @Override
    public User update(User user) {
        checkEmail(user);
        Long id = user.getId();
        User oldUser = users.get(id);
        if (oldUser == null) {
            throw new NotFoundException("Нет пользователя с id = " + id);
        }
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User getById(long userId) {
        User user = users.get(userId);
        if (user == null) throw new NotFoundException("Нет пользователя с id = " + userId);
        return user;
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    public void checkEmail(User user) {
        String email = user.getEmail();
        Long id = user.getId();
        users.forEach((key, value) -> {
            if (value.getEmail().equals(email) && !key.equals(id)) {
                throw new EmailExistException("Email " + email + " уже существует");
            }
        });
    }
}

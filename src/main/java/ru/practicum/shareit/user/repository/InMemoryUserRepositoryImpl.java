package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private final HashSet<String> emails = new HashSet<>();
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
        emails.add(user.getEmail());
        return users.get(userId);
    }

    @Override
    public User update(User user) {
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
        String oldEmail = oldUser.getEmail();
        String newEmail = user.getEmail();
        if (!oldEmail.equals(newEmail)) {
            checkEmail(user);
            emails.remove(oldUser.getEmail());
            emails.add(user.getEmail());
        }
        users.put(id, user);

        return users.get(id);
    }

    @Override
    public User getById(long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        return user;
    }

    @Override
    public void delete(long userId) {
        User user = getById(userId);
        users.remove(userId);
        if (user != null) {
            emails.remove(user.getEmail());
        }
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    public void checkEmail(User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new EmailExistException("Email " + email + " уже существует");
        }
    }
}

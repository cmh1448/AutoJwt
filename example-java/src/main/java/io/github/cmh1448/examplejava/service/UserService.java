package io.github.cmh1448.examplejava.service;

import io.github.cmh1448.autojwt.model.AuthDetails;
import io.github.cmh1448.autojwt.service.UserLoadService;
import io.github.cmh1448.examplejava.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserLoadService {
    private final List<User> cachedUsers = new ArrayList<>();

    public void addUser(User user) {
        cachedUsers.add(user);
    }

    public Optional<User> findById(String id) {
        return cachedUsers.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @NotNull
    @Override
    public AuthDetails loadUserByKey(@NotNull String key) {
        return findById(key)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}

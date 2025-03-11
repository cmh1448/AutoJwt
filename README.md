# AutoJwt

[<u>English</u>](README.md) | [한국어](README-ko.md)

## TL;DR

AutoJwt is a library designed to quickly and easily apply JWT authentication to Spring Boot.  
Users can implement JWT authentication with simple configuration without worrying about complex filter structures or authentication processing.

## Requirements

- Spring Boot 3 or later
- Java 17 or later (depending on the Spring Boot version)

## Features

- Very short and concise implementation of JWT authentication
- Utility classes provided for JWT token generation, renewal, etc.
- Refresh Token handling using headers
- Boot-starter provided for automatic bean registration

## Getting Started

### 1. Add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.cmh1448:autojwt-spring-boot-starter:1.0.0'
}
```

### 2. Implement a model class to store authentication information:

```java
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class User extends AuthDetails {
    private String id;
    private String password;

    @NotNull
    @Override
    public String getKey() {
        return id;
    }
}
```

`AuthDetails` is an abstract class provided by the library to store user information.  
You can directly extend `AuthDetails` in your `User` object, or create a separate class like `UserDetails` to store user information independently from your entity.

### 3. Implement the `UserLoadService`:

```java
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
```

`UserLoadService` is an interface used by the library to load authentication information using a user key.  
In later configurations, this service is dependency-injected so the library can store it in the `SecurityContextHolder`.  
You can integrate it into a service like `UserService` or create a separate class like `CustomUserLoadService`.

## 4. Write the Security Configuration

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtConfigurerFactory jwtConfigurerFactory;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        jwtConfigurerFactory.createJwtAutoConfigurer(userService)
                .pathConfigure(it -> {
                    it.includeAll();
                    it.excludePattern("/register");
                    it.excludePattern("/login");
                })
                .configure(http);

        return http.build();
    }
}
```

- `JwtConfigurerFactory` takes a `UserLoadService` as input and creates a Configurer class for JWT authentication.
- `.pathConfigure()` sets the URL patterns that require authentication.
- `.configure()` adds the JWT authentication filter to the `SecurityFilterChain` based on the configured information.

## How To Use?

### JWT Authentication

- Simply applying the above configuration enables JWT authentication for the URL patterns specified in `.pathConfigure()`.
- When a request is sent to a URL path with JWT authentication enabled, the JWT Filter performs authentication and stores the information in the `SecurityContextHolder`.
- The stored authentication information can be injected into a controller using the `@AuthenticationPrincipal` annotation.

```java
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user) {
        return "You are " + user.getId();
    }
```

### JWT Token Issuance

- The library provides a `JwtTokenProvider` component for issuing or renewing JWT tokens.

```java
//JwtDto.TokenData tokenString = jwtTokenProvider.generate(AuthDetails authInfo, Long expireHours);

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public String login(String id, String password) {
        return userService.findById(id)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> jwtTokenProvider.generate(user, 24))
                .orElseThrow(() -> new RuntimeException("email or password is wrong"));
    }

    public void register(String id, String password) {
        User toSave = User.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .build();

        userService.addUser(toSave);
    }
}
```

`For more detailed implementations, refer to the example project inside the repository.`

### How to Reissue an Access Token with a Refresh Token (Important!)

- When using the AutoJWT library, you don’t need to implement a separate API to reissue an Access Token using a Refresh Token.
- When an API request is sent with a Refresh Token, it is processed as if it were an Access Token, and the response header includes data for the new Access Token.

`Example`

```text
Request Header

Authorization: Bearer {RefreshToken}
```

```text
Response Header

Refreshed-Access-Token: eyJhbGciOiJIUzI1NiJ9....
Refreshed-Access-Token-Expire: 2025-03-11T20:54:23.747967
```
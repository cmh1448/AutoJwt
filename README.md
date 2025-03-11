# AutoJwt

## TL;DR

AutoJwt는 JWT인증을 SpringBoot에 빠르고 쉽게 적용 가능하도록 설계된 라이브러리 입니다.  
사용자는 복잡한 필터구조와 인증처리를 신경쓰지 않고, 간단한 설정만으로 JWT인증을 적용할 수 있습니다.

## Requirements

- Spring Boot 3 or later
- Java 17 or later (according to Spring Boot version)

## Features

- 아주 짧고 간결하게 JWT 인증 구현 가능
- JWT 토큰 생성, 갱신 등을 위한 유틸리티 클래스 제공
- Header를 이용한 Refresh Token Handling
- 자동 빈 등록을 위한 boot-starter 제공

## Getting Started

### 1. build.gradle에 다음과 같이 의존성을 추가합니다.

```groovy
dependencies {
    implementation 'io.github.cmh1448:autojwt-spring-boot-starter:0.1.0'
}
```

### 2. 인증 정보를 저장할 모델 클래스를 구현합니다.

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

`AuthDetails`는 라이브러리에서 사용자 정보를 저장하기 위한 추상 클래스 입니다.  
User 객체에 직접 AuthDetails를 상속하여 사용하여도 되고,  
UserDetails와 같은 클래스를 생성하여 Entity와 별도로 유저 정보를 저장하여도 무방합니다.

### 3. UserLoadService를 구현합니다.

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

UserLoadService는 라이브러리가 user key를 이용해서 인증 정보를 로드하기 위한 인터페이스 입니다.  
추후 설정에서 이 서비스를 의존성 주입하여 라이브러리가 `SecurityContextHolder`에 저장합니다.  
마찬가지로 UserService와 같은 서비스에 통합하여 작성해도 되고,  
별도의 `CustomUserLoadService`와 같은 클래스를 생성하여 사용해도 무방합니다.

## 4. Security Configuration 작성

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

- `JwtConfigurerFactory`는 UserLoadService를 주입받아 JWT 인증을 위한 Configurer 클래스를 생성합니다.
- `.pathConfigure()`는 인증이 필요한 URL 패턴을 설정합니다.
- `.configure()`는 설정된 정보를 바탕으로 JWT인증 필터를 SecurityFilterChain에 추가합니다.

## How To Use?

### JWT 인증

- 위와 같은 설정을 적용하는것 만으로 `.pathConfigure()`에 설정된 URL 패턴에 대한 JWT 인증이 적용됩니다.  
- JWT 인증이 활성화된 URL 경로에 대한 요청을 보내면, JWT Filter를 통해 인증을 수행한 뒤 SecurityContextHolder에 인증 정보를 저장합니다.  
- 저장된 인증 정보는 `@AuthenticationPrincipal` 어노테이션을 통해 컨트롤러에서 주입받을 수 있습니다.

```java
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user) {
        return "You are " + user.getId();
    }
```

### JWT 토큰 발급
- JWT 토큰의 발급 또는 갱신을 위해 `JwtTokenProvider` 컴포넌트를 제공합니다.
```java
//String tokenString =  jwtTokenProvider.generate(AuthDetails 인증정보, Long expireHours);

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
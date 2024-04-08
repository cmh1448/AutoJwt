package io.github.cmh1448.examplejava.model;

import io.github.cmh1448.autojwt.model.AuthDetails;
import lombok.*;
import org.jetbrains.annotations.NotNull;

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

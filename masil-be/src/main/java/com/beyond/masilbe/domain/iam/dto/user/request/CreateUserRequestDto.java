package com.beyond.masilbe.domain.iam.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateUserRequestDto {

    @NotBlank
    private String userName;

    @NotBlank
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Integer age;
}

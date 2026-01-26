package com.beyond.masilbe.domain.iam.controller;

import com.beyond.masilbe.domain.iam.dto.user.request.CreateUserRequestDto;
import com.beyond.masilbe.domain.iam.dto.user.response.CreateUserResponseDto;
import com.beyond.masilbe.domain.iam.service.command.UserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    // -------------------------------------------
    // Command
    // -------------------------------------------
    // 사용자 생성
    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@Valid @RequestBody final CreateUserRequestDto request) {
        return ResponseEntity.ok(userCommandService.createUser(request));
    }
}

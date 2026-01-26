package com.beyond.masilbe.domain.iam.service.command;

import com.beyond.masilbe.domain.iam.dto.user.request.CreateUserRequestDto;
import com.beyond.masilbe.domain.iam.dto.user.response.CreateUserResponseDto;

public interface UserCommandService {

    CreateUserResponseDto createUser(final CreateUserRequestDto request);
}

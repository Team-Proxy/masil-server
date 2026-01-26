package com.beyond.masilbe.domain.iam.service.command;

import com.beyond.masilbe.domain.iam.dto.user.request.CreateUserRequestDto;
import com.beyond.masilbe.domain.iam.dto.user.response.CreateUserResponseDto;
import com.beyond.masilbe.domain.iam.entity.Users;
import com.beyond.masilbe.domain.iam.support.user.UserWriter;
import com.beyond.masilbe.security.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserWriter userWriter;

    private final PasswordEncoder passwordEncoder;

    // 사용자 생성
    @Override
    @Transactional
    public CreateUserResponseDto createUser(final CreateUserRequestDto request) {

        // 임시 비밀번호 생성
        final String tempPassword = PasswordGenerator.generate();
        final String encrypted = passwordEncoder.encode(tempPassword);

        Users users = Users.create(request, encrypted);
        Users saved = userWriter.save(users);

        return CreateUserResponseDto.fromEntity(saved);
    }
}

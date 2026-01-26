package com.beyond.masilbe.domain.iam.service.query;

import com.beyond.masilbe.domain.iam.support.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserReader userReader;
}

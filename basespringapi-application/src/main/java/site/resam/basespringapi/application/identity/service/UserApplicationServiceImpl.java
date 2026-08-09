package site.resam.basespringapi.application.identity.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import site.resam.basespringapi.application.identity.dto.CreateUserRequest;
import site.resam.basespringapi.application.identity.dto.GetUserRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersResponse;
import site.resam.basespringapi.application.identity.dto.UserResponse;
import site.resam.basespringapi.application.identity.mapper.UserDtoMapper;
import site.resam.basespringapi.domain.identity.User;
import site.resam.basespringapi.domain.identity.UserRepository;
import site.resam.basespringapi.shared.exception.ConflictException;
import site.resam.basespringapi.shared.exception.NotFoundException;
import site.resam.basespringapi.shared.exception.ValidationException;
import site.resam.basespringapi.shared.pagination.PageRequestDto;
import site.resam.basespringapi.shared.pagination.PageResponseDto;
import site.resam.basespringapi.shared.time.TimeProvider;

@Service
@Validated
@Transactional
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserRepository userRepository;
    private final TimeProvider timeProvider;

    public UserApplicationServiceImpl(UserRepository userRepository, TimeProvider timeProvider) {
        this.userRepository = userRepository;
        this.timeProvider = timeProvider;
    }

    @Override
    public UserResponse createUser(@Valid CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("USER_EMAIL_EXISTS", "Email already registered: " + request.email());
        }
        User user = User.create(request.email(), request.displayName(), timeProvider.now());
        return UserDtoMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(@Valid GetUserRequest request) {
        UUID id = parseId(request.id());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found: " + request.id()));
        return UserDtoMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ListUsersResponse listUsers(@Valid ListUsersRequest request) {
        PageRequestDto page = request.page() == null ? PageRequestDto.of(0, 20) : request.page();
        PageResponseDto<User> result = userRepository.findAll(page);
        List<UserResponse> items = result.items().stream().map(UserDtoMapper::toResponse).toList();
        return new ListUsersResponse(items, result.meta());
    }

    private static UUID parseId(String raw) {
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("USER_ID_INVALID", "Invalid user id: " + raw);
        }
    }
}

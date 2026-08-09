package site.resam.basespringapi.domain.identity;

import java.util.Optional;
import java.util.UUID;
import site.resam.basespringapi.shared.pagination.PageRequestDto;
import site.resam.basespringapi.shared.pagination.PageResponseDto;

/**
 * Domain port for user persistence (implemented in infrastructure).
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    PageResponseDto<User> findAll(PageRequestDto pageRequest);
}

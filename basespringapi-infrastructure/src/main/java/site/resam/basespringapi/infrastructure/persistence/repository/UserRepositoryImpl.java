package site.resam.basespringapi.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import site.resam.basespringapi.domain.identity.User;
import site.resam.basespringapi.domain.identity.UserRepository;
import site.resam.basespringapi.infrastructure.persistence.entity.UserJpaEntity;
import site.resam.basespringapi.infrastructure.persistence.mapper.UserPersistenceMapper;
import site.resam.basespringapi.shared.pagination.PageMetaDto;
import site.resam.basespringapi.shared.pagination.PageRequestDto;
import site.resam.basespringapi.shared.pagination.PageResponseDto;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = jpaRepository.save(UserPersistenceMapper.toEntity(user));
        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmailIgnoreCase(email).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public PageResponseDto<User> findAll(PageRequestDto pageRequest) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (pageRequest.sort() != null && !pageRequest.sort().isBlank()) {
            sort = Sort.by(pageRequest.sort().split(","));
        }
        Page<UserJpaEntity> page = jpaRepository.findAll(
                PageRequest.of(pageRequest.page(), pageRequest.size(), sort)
        );
        return new PageResponseDto<>(
                page.getContent().stream().map(UserPersistenceMapper::toDomain).toList(),
                PageMetaDto.of(page.getNumber(), page.getSize(), page.getTotalElements())
        );
    }
}

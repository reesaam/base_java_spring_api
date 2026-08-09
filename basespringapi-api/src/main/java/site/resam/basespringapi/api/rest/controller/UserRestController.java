package site.resam.basespringapi.api.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.resam.basespringapi.application.identity.dto.CreateUserRequest;
import site.resam.basespringapi.application.identity.dto.GetUserRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersResponse;
import site.resam.basespringapi.application.identity.dto.UserResponse;
import site.resam.basespringapi.application.identity.service.UserApplicationService;
import site.resam.basespringapi.shared.pagination.PageRequestDto;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserApplicationService userApplicationService;

    public UserRestController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userApplicationService.createUser(request));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable String id) {
        return userApplicationService.getUser(new GetUserRequest(id));
    }

    @GetMapping
    public ListUsersResponse list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort
    ) {
        return userApplicationService.listUsers(new ListUsersRequest(new PageRequestDto(page, size, sort)));
    }
}

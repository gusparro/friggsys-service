package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.facades.UserOperationsFacade;
import com.gusparro.friggsys.adapter.api.reponse.UserResponse;
import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.domain.repositories.pagination.PageOrder;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserOperationsFacade userOperationsFacade;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        var createdUser = userOperationsFacade.create(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.id())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        var updatedUser = userOperationsFacade.update(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<UserResponse> changePassword(@PathVariable UUID id,
                                                       @Valid @RequestBody ChangePasswordRequest request) {
        var updatedUser = userOperationsFacade.changePassword(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activate(@PathVariable UUID id) {
        var activatedUser = userOperationsFacade.activate(id);

        return ResponseEntity.ok(activatedUser);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivate(@PathVariable UUID id) {
        var deactivatedUser = userOperationsFacade.deactivate(id);

        return ResponseEntity.ok(deactivatedUser);
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<UserResponse> block(@PathVariable UUID id) {
        var blockedUser = userOperationsFacade.block(id);

        return ResponseEntity.ok(blockedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        userOperationsFacade.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "name") String orderBy,
                                                      @RequestParam(defaultValue = "ASC") PageOrder direction) {
        var parameters = PageParameters.builder()
                .page(page)
                .size(size)
                .orderBy(orderBy)
                .direction(direction)
                .build();

        var response = userOperationsFacade.findAll(parameters);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        var user = userOperationsFacade.findById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponse> findByEmail(@RequestParam String email) {
        var user = userOperationsFacade.findByEmail(email);

        return ResponseEntity.ok(user);
    }

}

package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.facades.UserOperationsFacade;
import com.gusparro.friggsys.adapter.api.response.UserResponse;
import com.gusparro.friggsys.adapter.api.request.ChangePasswordRequest;
import com.gusparro.friggsys.adapter.api.request.CreateUserRequest;
import com.gusparro.friggsys.adapter.api.request.UpdateUserRequest;
import com.gusparro.friggsys.domain.repositories.pagination.PageOrder;
import com.gusparro.friggsys.domain.repositories.pagination.PageParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserOperationsFacade userOperationsFacade;

    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
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

    @Operation(summary = "Update user", description = "Updates user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        var updatedUser = userOperationsFacade.update(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Change user password", description = "Changes the password for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<UserResponse> changePassword(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordRequest request) {
        var updatedUser = userOperationsFacade.changePassword(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Activate user", description = "Activates a deactivated user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activate(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id) {
        var activatedUser = userOperationsFacade.activate(id);

        return ResponseEntity.ok(activatedUser);
    }

    @Operation(summary = "Deactivate user", description = "Deactivates an active user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivate(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id) {
        var deactivatedUser = userOperationsFacade.deactivate(id);

        return ResponseEntity.ok(deactivatedUser);
    }

    @Operation(summary = "Block user", description = "Blocks a user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User blocked successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/block")
    public ResponseEntity<UserResponse> block(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id) {
        var blockedUser = userOperationsFacade.block(id);

        return ResponseEntity.ok(blockedUser);
    }

    @Operation(summary = "Delete user", description = "Permanently deletes a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id) {
        userOperationsFacade.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all users", description = "Returns a paginated list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name") @RequestParam(defaultValue = "name") String orderBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") PageOrder direction) {
        var parameters = PageParameters.builder()
                .page(page)
                .size(size)
                .orderBy(orderBy)
                .direction(direction)
                .build();

        var response = userOperationsFacade.findAll(parameters);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Find user by ID", description = "Returns a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @Parameter(description = "User ID", required = true) @PathVariable UUID id) {
        var user = userOperationsFacade.findById(id);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Find user by email", description = "Returns a user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/search")
    public ResponseEntity<UserResponse> findByEmail(
            @Parameter(description = "User email", required = true, example = "user@example.com")
            @RequestParam String email) {
        var user = userOperationsFacade.findByEmail(email);

        return ResponseEntity.ok(user);
    }

}
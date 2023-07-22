package com.harri.training1.controllers;

import com.harri.training1.models.dto.UserDto;
import com.harri.training1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPERUSER')")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<UserDto> users = userService.findAll();
        LOGGER.info("Get all users in the system.");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        UserDto user = userService.findById(id);
        LOGGER.info("Get user by id = " + id);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        userService.deleteById(id);
        LOGGER.info("Delete user by id = " + id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserDto user){
        userService.update(user);
        LOGGER.info("Update user with id = " + user.getId());
        return ResponseEntity.ok("User updated successfully!");
    }
}

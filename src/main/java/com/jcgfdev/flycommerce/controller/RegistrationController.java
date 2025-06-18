package com.jcgfdev.flycommerce.controller;

import com.jcgfdev.flycommerce.docs.UsersApiDocs;
import com.jcgfdev.flycommerce.dto.UsersDTO;
import com.jcgfdev.flycommerce.payload.request.CreateUserRequest;
import com.jcgfdev.flycommerce.service.IUsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController implements UsersApiDocs {
    private final IUsersService usersService;

    @PostMapping("/saveUser")
    @Override
    public ResponseEntity<UsersDTO> saveUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UsersDTO user = usersService.saveUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

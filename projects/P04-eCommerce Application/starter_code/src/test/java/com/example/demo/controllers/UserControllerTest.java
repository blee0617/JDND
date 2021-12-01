package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void create_user_negative_path() throws Exception {
        when(encoder.encode("test1")).thenReturn("test12");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test1");
        r.setConfirmPassword("test1");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void find_by_user_name_happy_path() throws Exception {
        User u = new User();
        u.setUsername("test");
        u.setPassword("testPassword");
        when(userRepo.findByUsername("test")).thenReturn(u);

        final ResponseEntity<User> findByUserNameResponse = userController.findByUserName("test");
        assertNotNull(findByUserNameResponse);
        assertEquals(200, findByUserNameResponse.getStatusCodeValue());
        assertEquals("test", findByUserNameResponse.getBody().getUsername());
        assertEquals("testPassword", findByUserNameResponse.getBody().getPassword());
    }

    @Test
    public void find_by_user_name_negative_path() throws Exception {
        User u = new User();
        u.setUsername("test");
        u.setPassword("testPassword");
        when(userRepo.findByUsername("test")).thenReturn(u);

        final ResponseEntity<User> findByUserNameResponse = userController.findByUserName("nonExistingUserName");
        assertNotNull(findByUserNameResponse);
        assertEquals(404, findByUserNameResponse.getStatusCodeValue());
    }

    @Test
    public void find_by_id_happy_path() throws Exception {
        User u = new User();
        u.setUsername("test");
        u.setPassword("testPassword");
        when(userRepo.findById(0L)).thenReturn(Optional.of(u));

        final ResponseEntity<User> findByIDResponse = userController.findById(0L);
        assertNotNull(findByIDResponse);
        assertEquals(200, findByIDResponse.getStatusCodeValue());
        assertEquals("test", findByIDResponse.getBody().getUsername());
        assertEquals("testPassword", findByIDResponse.getBody().getPassword());
    }

    @Test
    public void find_by_id_negative_path() throws Exception {
        User u = new User();
        u.setUsername("test");
        u.setPassword("testPassword");
        when(userRepo.findById(0L)).thenReturn(Optional.of(u));

        final ResponseEntity<User> findByIDResponse = userController.findById(2L);
        assertNotNull(findByIDResponse);
        assertEquals(404, findByIDResponse.getStatusCodeValue());
    }

}

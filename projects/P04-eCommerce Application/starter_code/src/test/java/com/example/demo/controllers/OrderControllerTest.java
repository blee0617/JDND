package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submit_happy_path() {
        Item item = createItem(1L, "LEGO", BigDecimal.valueOf(5), "Colored blocks that can be attached to each other.");
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        Cart cart = createCart(1L, items, null);
        User user = createUser(1L, "Brian", "TWN!sH0t", cart);
        cart.setUser(user);

        when(userRepo.findByUsername("Brian")).thenReturn(user);

        final ResponseEntity<UserOrder> submitResponse = orderController.submit("Brian");

        assertNotNull(submitResponse);
        assertEquals(200, submitResponse.getStatusCodeValue());

        UserOrder order = submitResponse.getBody();
        assertEquals(user, order.getUser());
        assertEquals(items, order.getItems());
        assertEquals(BigDecimal.valueOf(5), order.getTotal());
    }

    @Test
    public void get_orders_for_user_happy_path() {
        Item item = createItem(1L, "LEGO", BigDecimal.valueOf(10), "Colored blocks that can be attached to each other.");
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        Cart cart = createCart(1L, new ArrayList<>(), null);
        User user = createUser(1L, "Brian", "TWN!sH0t", null);
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        orderController.submit("Brian");
        when(userRepo.findByUsername("Brian")).thenReturn(user);

        final ResponseEntity<List<UserOrder>> getOrdersForUserResponse = orderController.getOrdersForUser("Brian");

        assertNotNull(getOrdersForUserResponse);
        assertEquals(200, getOrdersForUserResponse.getStatusCodeValue());

        List<UserOrder> orders = getOrdersForUserResponse.getBody();
        assertNotNull(orders);
    }

    @Test
    public void submit_negative_path() {
        when(userRepo.findByUsername("Brian")).thenReturn(null);
        final ResponseEntity<UserOrder> submitResponse = orderController.submit("Brian");
        assertEquals(404, submitResponse.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_negative_path() {
        when(userRepo.findByUsername("Brian")).thenReturn(null);
        final ResponseEntity<List<UserOrder>> getOrdersForUserResponse = orderController.getOrdersForUser("Brian");
        assertEquals(404, getOrdersForUserResponse.getStatusCodeValue());
    }

    /**
     * Create user
     *
     * @param userId
     * @param username
     * @param password
     * @param cart
     * @return newUser
     */
    public User createUser(long userId, String username, String password, Cart cart) {
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCart(cart);
        return newUser;
    }

    /**
     * Create item
     *
     * @param id
     * @param name
     * @param price
     * @param description
     * @return newItem
     */
    public Item createItem(Long id, String name, BigDecimal price, String description) {
        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(name);
        newItem.setPrice(price);
        newItem.setDescription(description);
        return newItem;
    }

    /**
     * Create Cart
     *
     * @param cartId
     * @param items
     * @param user
     * @return newCart
     */
    public Cart createCart(long cartId, ArrayList<Item> items, User user) {
        Cart newCart = new Cart();
        newCart.setId(cartId);
        newCart.setItems(items);
        newCart.setUser(user);
        newCart.setTotal(BigDecimal.valueOf(5));
        return newCart;
    }

}

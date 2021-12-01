package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest newCartRequest = createCartRelatedInfo();
        final ResponseEntity<Cart> response = cartController.addTocart(newCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(3, cart.getItems().size());
    }

    @Test
    public void remove_from_cart_happy_path() {
        ModifyCartRequest newCartRequest = createCartRelatedInfo();
        final ResponseEntity<Cart> response = cartController.removeFromcart(newCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void add_to_cart_no_user_negative_path() {
        ModifyCartRequest newCartRequest = createCartRequest(1L, 10, "Brian");

        when(userRepo.findByUsername("Brian")).thenReturn(null);
        when(itemRepo.findById(anyLong())).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.addTocart(newCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_no_item_negative_path() {
        Cart newCart = new Cart();
        User newUser = createUser(1l, "Brian", "TWN!sH0t", newCart);
        ModifyCartRequest newCartRequest = createCartRequest(1L, 5, "Brian");

        when(userRepo.findByUsername("Brian")).thenReturn(newUser);
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(null));

        final ResponseEntity<Cart> response = cartController.addTocart(newCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_no_user_negative_path() {
        ModifyCartRequest newCartRequest = createCartRequest(1L, 5, "Brian");

        when(userRepo.findByUsername("Brian")).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.removeFromcart(newCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_no_item_negative_path() {
        Cart newCart = new Cart();
        User newUser = createUser(1l, "Brian", "TWN!sH0t", newCart);
        ModifyCartRequest newCartRequest = createCartRequest(1L, 5, "Brian");

        when(userRepo.findByUsername("Brian")).thenReturn(newUser);
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(null));

        final ResponseEntity<Cart> response = cartController.removeFromcart(newCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Create cart, user and item
     *
     * @return
     */
    public ModifyCartRequest createCartRelatedInfo() {
        Cart newCart = new Cart();
        User newUser = createUser(1l, "Brian", "TWN!sH0t", newCart);
        Item newItem = createItem(1L, "LEGO", new BigDecimal("10"), "Colored Blocks");
        ModifyCartRequest newCartRequest = createCartRequest(1L, 3, "Brian");
        ArrayList<Item> listOfItems = new ArrayList<Item>();
        listOfItems.add(newItem);
        createCart(1l, listOfItems, newUser);

        when(userRepo.findByUsername("Brian")).thenReturn(newUser);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(newItem));
        return newCartRequest;
    }

    /**
     * Create cart request
     *
     * @param itemId
     * @param quantity
     * @param username
     * @return cartRequest
     */
    public ModifyCartRequest createCartRequest(long itemId, int quantity, String username) {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(quantity);
        cartRequest.setUsername(username);
        return cartRequest;
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
        return newCart;
    }

}

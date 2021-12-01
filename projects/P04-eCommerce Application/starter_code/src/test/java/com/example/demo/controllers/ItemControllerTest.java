package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void find_all_items_happy_path() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        Item it = new Item();
        it.setName("Round Widget");
        it.setPrice(BigDecimal.valueOf(2.99));
        it.setDescription("A widget that is round");
        itemList.add(it);
        when(itemRepo.findAll()).thenReturn(itemList);

        final ResponseEntity<List<Item>> findItemsByItemNameResponse = itemController.getItems();
        assertNotNull(findItemsByItemNameResponse);
        assertEquals(200, findItemsByItemNameResponse.getStatusCodeValue());
        assertEquals("Round Widget", findItemsByItemNameResponse.getBody().get(0).getName());
    }

    @Test
    public void find_item_by_id_happy_path() throws Exception {
        Item it = new Item();
        it.setName("Round Widget");
        it.setPrice(BigDecimal.valueOf(2.99));
        it.setDescription("A widget that is round");
        when(itemRepo.findById(0L)).thenReturn(Optional.of(it));

        final ResponseEntity<Item> findItemByIDResponse = itemController.getItemById(0L);
        assertNotNull(findItemByIDResponse);
        assertEquals(200, findItemByIDResponse.getStatusCodeValue());
        assertEquals("Round Widget", findItemByIDResponse.getBody().getName());
    }

    @Test
    public void find_item_by_id_negative_path() throws Exception {
        Item it = new Item();
        it.setName("Round Widget");
        it.setPrice(BigDecimal.valueOf(2.99));
        it.setDescription("A widget that is round");
        when(itemRepo.findById(0L)).thenReturn(Optional.of(it));

        final ResponseEntity<Item> findItemByIDResponse = itemController.getItemById(2L);
        assertNotNull(findItemByIDResponse);
        assertEquals(404, findItemByIDResponse.getStatusCodeValue());
    }

    @Test
    public void find_items_by_item_name_happy_path() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        Item it = new Item();
        it.setName("Round Widget");
        it.setPrice(BigDecimal.valueOf(2.99));
        it.setDescription("A widget that is round");
        itemList.add(it);
        when(itemRepo.findByName("Round Widget")).thenReturn(itemList);

        final ResponseEntity<List<Item>> findItemsByItemNameResponse = itemController.getItemsByName("Round Widget");
        assertNotNull(findItemsByItemNameResponse);
        assertEquals(200, findItemsByItemNameResponse.getStatusCodeValue());
        assertEquals("Round Widget", findItemsByItemNameResponse.getBody().get(0).getName());
    }

    @Test
    public void find_items_by_item_name_negative_path() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        Item it = new Item();
        it.setName("Round Widget");
        it.setPrice(BigDecimal.valueOf(2.99));
        it.setDescription("A widget that is round");
        itemList.add(it);
        when(itemRepo.findByName("Round Widget")).thenReturn(itemList);

        final ResponseEntity<List<Item>> findItemsByItemNameResponse = itemController.getItemsByName("Non-existing Item");
        assertNotNull(findItemsByItemNameResponse);
        assertEquals(404, findItemsByItemNameResponse.getStatusCodeValue());
    }

}

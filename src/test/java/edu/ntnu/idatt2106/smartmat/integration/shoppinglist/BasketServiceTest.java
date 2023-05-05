package edu.ntnu.idatt2106.smartmat.integration.shoppinglist;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.BasketItem;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.BasketItemRepository;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.BasketRepository;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.BasketService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.BasketServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BasketServiceTest {

  @TestConfiguration
  static class BasketServiceTestConfiguration {

    @Bean
    public BasketService basketService() {
      return new BasketServiceImpl();
    }
  }

  @Autowired
  private BasketService basketService;

  @MockBean
  private BasketRepository basketRepository;

  @MockBean
  private BasketItemRepository basketItemRepository;

  private Basket basket;

  private final UUID basketId = UUID.randomUUID();

  private final UUID shoppingListId = UUID.randomUUID();

  private ShoppingList shoppingList;

  @Before
  public void setUp() {
    shoppingList = ShoppingList.builder().id(shoppingListId).build();
    basket = Basket.builder().id(basketId).shoppingList(shoppingList).build();
    shoppingList.setBasket(basket);
  }

  @Test
  public void testExistsWhenBasketExists() {
    when(basketRepository.existsById(basketId)).thenReturn(true);
    assertTrue(basketService.basketExists(basketId));
  }

  @Test
  public void testExistsWhenBasketDoesNotExist() {
    when(basketRepository.existsById(basketId)).thenReturn(false);
    assertTrue(!basketService.basketExists(basketId));
  }

  @Test
  public void testExistsThrowsNullPointerException() {
    when(basketRepository.existsById(basketId)).thenThrow(NullPointerException.class);
    assertThrows(NullPointerException.class, () -> basketService.basketExists(basketId));
  }

  @Test
  public void testGetBasketWhenBasketExists() throws Exception {
    when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));
    assertTrue(basketService.getBasketById(basketId).equals(basket));
  }

  @Test
  public void testGetBasketWhenBasketDoesNotExist() throws Exception {
    when(basketRepository.findById(basketId)).thenReturn(Optional.empty());
    assertThrows(BasketNotFoundException.class, () -> basketService.getBasketById(basketId));
  }

  @Test
  public void testGetBasketThrowsNullPointerException() {
    when(basketRepository.findById(basketId)).thenThrow(NullPointerException.class);
    assertThrows(NullPointerException.class, () -> basketService.getBasketById(basketId));
  }

  @Test
  public void testCreateBasketNonExistingBasket() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(false);
    when(basketRepository.save(basket)).thenReturn(basket);
    assertTrue(basketService.createBasket(basket).equals(basket));
  }

  @Test
  public void testCreateBasketExistingBasket() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(true);
    when(basketRepository.save(basket)).thenReturn(basket);
    assertThrows(BasketAlreadyExistsException.class, () -> basketService.createBasket(basket));
  }

  @Test
  public void testCreateBasketThrowsNullPointerException() {
    when(basketRepository.existsById(basketId)).thenThrow(NullPointerException.class);
    assertThrows(NullPointerException.class, () -> basketService.createBasket(basket));
  }

  @Test
  public void testUpdateBasketWhenBasketExists() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(true);
    when(basketRepository.save(basket)).thenReturn(basket);
    assertTrue(basketService.updateBasket(basket).equals(basket));
  }

  @Test
  public void testUpdateBasketWhenBasketDoesNotExist() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(false);
    when(basketRepository.save(basket)).thenReturn(basket);
    assertThrows(BasketNotFoundException.class, () -> basketService.updateBasket(basket));
  }

  @Test
  public void testUpdateBasketThrowsNullPointerException() {
    when(basketRepository.existsById(basketId)).thenThrow(NullPointerException.class);
    assertThrows(NullPointerException.class, () -> basketService.updateBasket(basket));
  }

  @Test
  public void testDeleteBasketByIdWhenBasketExists() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(true);
    doNothing().when(basketRepository).deleteById(basketId);
    assertDoesNotThrow(() -> basketService.deleteBasket(basketId));
  }

  @Test
  public void testDeleteBasketByIdWhenBasketDoesNotExist() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(false);
    doNothing().when(basketRepository).deleteById(basketId);
    assertThrows(BasketNotFoundException.class, () -> basketService.deleteBasket(basketId));
  }

  @Test
  public void testDeleteBasketByIdThrowsNullPointerException() {
    when(basketRepository.existsById(basketId)).thenThrow(NullPointerException.class);
    doNothing().when(basketRepository).deleteById(basketId);
    assertThrows(NullPointerException.class, () -> basketService.deleteBasket(basketId));
  }

  @Test
  public void testDeleteBasketWhenBasketExists() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(true);
    doNothing().when(basketRepository).delete(basket);
    assertDoesNotThrow(() -> basketService.deleteBasket(basket));
  }

  @Test
  public void testDeleteBasketWhenBasketDoesNotExist() throws Exception {
    when(basketRepository.existsById(basketId)).thenReturn(false);
    doNothing().when(basketRepository).delete(basket);
    assertThrows(BasketNotFoundException.class, () -> basketService.deleteBasket(basket));
  }

  @Test
  public void testDeleteBasketThrowsNullPointerException() {
    when(basketRepository.existsById(basketId)).thenThrow(NullPointerException.class);
    doNothing().when(basketRepository).delete(basket);
    assertThrows(NullPointerException.class, () -> basketService.deleteBasket(basket));
  }

  @Test
  public void testGetByShoppingListIdWhenBasketExists() throws Exception {
    when(basketRepository.findByShoppingListId(shoppingListId)).thenReturn(Optional.of(basket));
    assertTrue(basketService.getBasketByShoppingListId(shoppingListId).equals(basket));
  }

  @Test
  public void testGetByShoppingListIdWhenBasketDoesNotExist() throws Exception {
    when(basketRepository.findByShoppingListId(shoppingListId)).thenReturn(Optional.empty());
    assertThrows(
      BasketNotFoundException.class,
      () -> basketService.getBasketByShoppingListId(shoppingListId)
    );
  }

  @Test
  public void testGetByShoppingListIdThrowsNullPointerException() {
    when(basketRepository.findByShoppingListId(shoppingListId))
      .thenThrow(NullPointerException.class);
    assertThrows(
      NullPointerException.class,
      () -> basketService.getBasketByShoppingListId(shoppingListId)
    );
  }

  @Test
  public void testDeleteBasketItemWhenBasketItemExists() throws Exception {
    BasketItem basketItem = BasketItem.builder().id(UUID.randomUUID()).build();
    when(basketItemRepository.existsById(basketItem.getId())).thenReturn(true);
    doNothing().when(basketItemRepository).deleteById(basketItem.getId());
    assertDoesNotThrow(() -> basketService.deleteBasketItem(basketItem.getId()));
  }

  @Test
  public void testDeleteBasketItemWhenBasketItemDoesNotExist() throws Exception {
    BasketItem basketItem = BasketItem.builder().id(UUID.randomUUID()).build();
    when(basketItemRepository.existsById(basketItem.getId())).thenReturn(false);
    doNothing().when(basketItemRepository).deleteById(basketItem.getId());
    assertThrows(
      BasketNotFoundException.class,
      () -> basketService.deleteBasketItem(basketItem.getId())
    );
  }
}

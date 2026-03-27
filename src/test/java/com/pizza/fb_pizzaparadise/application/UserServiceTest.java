package com.pizza.fb_pizzaparadise.application;

import com.pizza.fb_pizzaparadise.domain.model.Pizza;
import com.pizza.fb_pizzaparadise.domain.model.User;
import com.pizza.fb_pizzaparadise.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserRejectsBlankName() {
        User user = new User("", "mail@test.com", "secret");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

        assertEquals("Name cannot be empty", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void createUserRejectsDuplicateEmail() {
        User user = new User("Nick", "mail@test.com", "secret");
        when(userRepository.findByEmail("mail@test.com")).thenReturn(new User());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

        assertEquals("A user with that email already exists", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void createUserEncodesPasswordResetsPointsAndSaves() {
        User user = new User("Nick", "mail@test.com", "secret");
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");

        userService.createUser(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("encoded-secret", savedUser.getPassword());
        assertEquals(0, savedUser.getPoints());
    }

    @Test
    void discountAndPointsHelpersUseExpectedConversions() {
        assertEquals(20, userService.calculateDiscount(250));
        assertEquals(200, userService.calculatePointsSpentFromDiscount(20));
        assertEquals(13, userService.calculatePointsEarned(135));
    }

    @Test
    void delegatesFavoritePizzaOperationsToRepository() {
        Pizza pizza = new Pizza();
        List<Pizza> favorites = List.of(pizza);
        when(userRepository.findFavoritePizzasByUserId(4)).thenReturn(favorites);

        userService.saveFavoritePizza(4, pizza);
        List<Pizza> result = userService.findFavoritePizzasByUserId(4);

        verify(userRepository).saveFavoritePizza(4, pizza);
        assertSame(favorites, result);
    }
}

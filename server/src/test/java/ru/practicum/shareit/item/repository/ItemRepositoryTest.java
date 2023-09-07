package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.PaginationMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertThat(entityManager, notNullValue());
    }

    @Test
    void search_shouldReturnListItem() {
        String text = "Name";
        Pageable page = PaginationMapper.toPage(0, 10);

        User user = new User(1L, "userName", "test@gmail.com");

        userRepository.save(user);

        Item item1 = new Item(1L, "itemName1", "description1", true, user.getId(), null);
        Item item2 = new Item(2L, "item", "description2", true, user.getId(), null);
        Item item3 = new Item(3L, "itemName3", "description3", true, user.getId(), null);

        assertEquals(0, itemRepository.search("%" + text.toLowerCase() + "%", page).size());

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        assertEquals(2, itemRepository.search("%" + text.toLowerCase() + "%", page).size());
    }

}
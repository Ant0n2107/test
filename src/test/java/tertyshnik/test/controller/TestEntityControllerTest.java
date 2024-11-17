package tertyshnik.test.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import tertyshnik.test.dto.TestEntityDto;
import tertyshnik.test.model.TestEntity;
import tertyshnik.test.repository.TestEntityRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TestEntityControllerTest {

    private static final String ENTITY_API_URL = "/entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TestEntityRepository repository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    void create() throws Exception {
        TestEntityDto createDto = new TestEntityDto();
        createDto.setName("test");

        assertEquals(repository.findAll().size(), 0);

        mockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(createDto))
            )
            .andExpect(status().isCreated());

        List<TestEntity> allEntities = repository.findAll();
        assertEquals(allEntities.size(), 1);
        assertEquals(allEntities.get(0).getName(), createDto.getName());
    }

    @Test
    void update() throws Exception {
        TestEntity entityToSave = new TestEntity();
        entityToSave.setName("test");
        repository.save(entityToSave);

        TestEntityDto updateDto = new TestEntityDto();
        updateDto.setName("updatedName");

        List<TestEntity> beforeUpdate = repository.findAll();
        assertEquals(beforeUpdate.size(), 1);
        assertEquals(beforeUpdate.get(0).getName(), entityToSave.getName());

        mockMvc
            .perform(
                put(ENTITY_API_URL_ID, entityToSave.getId())
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateDto))
            )
            .andExpect(status().isOk());

        List<TestEntity> afterUpdate = repository.findAll();
        assertEquals(afterUpdate.size(), 1);
        assertEquals(afterUpdate.get(0).getName(), updateDto.getName());
    }

    @Test
    void deleteEntity() throws Exception {
        TestEntity entityToSave = new TestEntity();
        entityToSave.setName("test");
        repository.save(entityToSave);

        assertEquals(repository.findAll().size(), 1);

        mockMvc
            .perform(delete(ENTITY_API_URL_ID, entityToSave.getId()))
            .andExpect(status().isNoContent());

        assertEquals(repository.findAll().size(), 0);
    }

    @Test
    void findByd() throws Exception {
        TestEntity entityToSave = new TestEntity();
        entityToSave.setName("test");
        entityToSave = repository.save(entityToSave);

        assertEquals(repository.findAll().size(), 1);

        mockMvc
            .perform(get(ENTITY_API_URL_ID, entityToSave.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entityToSave.getId().toString()))
            .andExpect(jsonPath("$.name").value(entityToSave.getName()));
    }

    @Test
    void search() throws Exception {
        TestEntity entityToSave = new TestEntity();
        entityToSave.setName("test");

        TestEntity entityToSave2 = new TestEntity();
        entityToSave2.setName("test2");

        repository.saveAll(List.of(entityToSave, entityToSave2));

        assertEquals(repository.findAll().size(), 2);

        mockMvc
            .perform(get(ENTITY_API_URL + "/search"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[*].name").value(hasItem(entityToSave.getName())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(entityToSave2.getName())));
    }
}
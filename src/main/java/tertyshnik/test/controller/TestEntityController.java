package tertyshnik.test.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tertyshnik.test.dto.TestEntityDto;
import tertyshnik.test.service.TestEntityService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/entities")
public class TestEntityController {

    private final TestEntityService service;

    public TestEntityController(TestEntityService service) {
        this.service = service;
    }

    @Operation(summary = "Создание записи")
    @PostMapping
    public ResponseEntity<TestEntityDto> create(@RequestBody TestEntityDto dto) throws URISyntaxException {

        TestEntityDto result = service.create(dto);

        return ResponseEntity.created(new URI("/api/entities" + result.getId())).body(result);
    }

    @Operation(summary = "Изменение записи")
    @PutMapping("/{id}")
    public ResponseEntity<TestEntityDto> update(@PathVariable Long id, @RequestBody TestEntityDto dto) {

        return ResponseEntity.of(service.update(id, dto));
    }

    @Operation(summary = "Удаление записи")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    @Operation(summary = "Получение записи по id")
    @GetMapping("/{id}")
    public ResponseEntity<TestEntityDto> findByd(@PathVariable Long id) {

        return ResponseEntity.of(service.findOne(id));
    }

    @Operation(summary = "Получение списка записей")
    @GetMapping("/search")
    public ResponseEntity<List<TestEntityDto>> search(Pageable pageable) {

        return ResponseEntity.ok(service.search(pageable));
    }
}

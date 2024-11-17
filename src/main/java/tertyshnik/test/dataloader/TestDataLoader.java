package tertyshnik.test.dataloader;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tertyshnik.test.model.TestEntity;
import tertyshnik.test.repository.TestEntityRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Profile("!test")
public class TestDataLoader {

    private static final int LIMIT_ENTITIES = 1000000;
    private final TestEntityRepository repository;

    public TestDataLoader(TestEntityRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void loadData() {
        int amount = (int) (LIMIT_ENTITIES - repository.count());
        createTestEntities(amount);
    }

    private void createTestEntities(int amount) {
        List<TestEntity> entities = IntStream.range(0, amount)
            .mapToObj(i -> new TestEntity("Name #" + i))
            .collect(Collectors.toList());

        repository.saveAll(entities);

        System.out.println("Миллион записей добавлен в БД!");
    }
}

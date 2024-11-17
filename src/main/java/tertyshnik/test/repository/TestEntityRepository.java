package tertyshnik.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tertyshnik.test.model.TestEntity;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
}

package tertyshnik.test.service;

import org.springframework.data.domain.Pageable;
import tertyshnik.test.dto.TestEntityDto;

import java.util.List;
import java.util.Optional;

public interface TestEntityService {

    TestEntityDto create(TestEntityDto testEntityDto);

    Optional<TestEntityDto> update(Long id, TestEntityDto testEntityDto);

    Optional<TestEntityDto> findOne(Long id);

    void delete(Long id);

    List<TestEntityDto> search(Pageable pageable);
}

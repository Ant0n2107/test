package tertyshnik.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tertyshnik.test.dto.TestEntityDto;
import tertyshnik.test.mapper.TestEntityMapper;
import tertyshnik.test.model.TestEntity;
import tertyshnik.test.repository.TestEntityRepository;
import tertyshnik.test.service.TestEntityService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestEntityServiceImpl implements TestEntityService {

    private final TestEntityRepository repository;
    private final TestEntityMapper mapper;


    @Override
    public TestEntityDto create(TestEntityDto testEntityDto) {

        TestEntity entityToSave = mapper.toEntity(testEntityDto);
        entityToSave = repository.save(entityToSave);
        return mapper.toDto(entityToSave);
    }

    @Override
    public Optional<TestEntityDto> update(Long id, TestEntityDto testEntityDto) {
        return repository.findById(id)
            .map(entityToUpdate -> {
                mapper.update(entityToUpdate, testEntityDto);
                entityToUpdate = repository.save(entityToUpdate);
                return Optional.of(mapper.toDto(entityToUpdate));
            }).orElseGet(Optional::empty);
    }

    @Override
    public Optional<TestEntityDto> findOne(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<TestEntityDto> search(Pageable pageable) {
        return repository.findAll(pageable).stream().map(mapper::toDto).toList();
    }
}

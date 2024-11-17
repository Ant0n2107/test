package tertyshnik.test.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tertyshnik.test.dto.TestEntityDto;
import tertyshnik.test.model.TestEntity;

@Mapper(componentModel = "spring")
public interface TestEntityMapper {

    TestEntityDto toDto(TestEntity testEntity);

    @Mapping(target = "id", ignore = true)
    TestEntity toEntity(TestEntityDto testEntityDto);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget TestEntity testEntity, TestEntityDto testEntityDto);
}

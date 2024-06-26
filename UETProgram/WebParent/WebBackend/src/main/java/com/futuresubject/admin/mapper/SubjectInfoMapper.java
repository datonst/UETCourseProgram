package com.futuresubject.admin.mapper;

import com.futuresubject.admin.dto.search.SubjectInfoDto;
import com.futuresubject.common.entity.Entity.Subject;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubjectInfoMapper {
    SubjectInfoMapper INSTANCE = Mappers.getMapper(SubjectInfoMapper.class);

    @Mapping(target = "prerequisiteSubjectId",expression="java(subject.getPrerequisiteSubjectId())")
    SubjectInfoDto toDto(Subject subject);



    List<SubjectInfoDto> toDtoList(List<Subject> subjectList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Subject partialUpdate(SubjectInfoDto subjectInfoDto, @MappingTarget Subject subject);
}
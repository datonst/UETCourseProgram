package com.futuresubject.admin.service;

import com.futuresubject.admin.dto.NotFoundDataExeption;
import com.futuresubject.admin.dto.StudentDto;
import com.futuresubject.admin.dto.SubjectDto;
import com.futuresubject.admin.mapper.SubjectMapper;
import com.futuresubject.admin.repository.SubjectRepository;
import com.futuresubject.common.entity.Subject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<SubjectDto> findAll() {
        return SubjectMapper.INSTANCE.toDtoList((List<Subject>) subjectRepository.findAll());
    }

    public SubjectDto get(String subjectid) throws NotFoundDataExeption {
        try {
            Subject subject =subjectRepository.findById(subjectid).get();
            SubjectDto subjectDto = SubjectMapper.INSTANCE.toDto(subject);
            for (String t : subjectDto.getPrerequisiteSubjectId()) {
                System.out.println(t);
            }
//            if (subject.getPrerequisiteSubject()!=null) {
//                List<String> list = new ArrayList<>();
//                System.out.println(subject.getSubjectName() + " : " + subject.getPrerequisiteSubject().size());
//                for (Subject s : subject.getPrerequisiteSubject()) {
//                    String subjectSubjectid = subject.getSubjectid();
//                    list.add(subjectSubjectid);
//                }
//                subjectDto.setPrerequisiteSubjectId(list);
//            }
            return subjectDto;
        } catch (NoSuchElementException ex){
            throw new NotFoundDataExeption("Could not find any subject with ID " + subjectid);
        }
    }
    public List<String> listOfSubjectId() {
        return subjectRepository.listOfSubjectId();
    }
    public Subject insert(SubjectDto subjectDto) {
        Subject subject = SubjectMapper.INSTANCE.toEntity(subjectDto);
        for (String s : subjectDto.getPrerequisiteSubjectId()) {
            Optional<Subject> pre_student = subjectRepository.findById(s);
            pre_student.ifPresent(subject::addPrerequisite);

        }
        return subjectRepository.save(subject);
    }

    public void deleteBySubjectid(String subjectid) throws NotFoundDataExeption {
        Long countById=subjectRepository.countBySubjectid(subjectid);// can use findById ==Null
        if(countById==null || countById==0){

            throw new NotFoundDataExeption("Could not find any user with ID " +subjectid );
        }
        subjectRepository.deleteById(subjectid);
    }

    public boolean isExist(SubjectDto subjectDto) {
        return subjectRepository.existsById(subjectDto.getSubjectid());
    }

    public void updateFromDto(SubjectDto subjectDto) {
        Subject subject = SubjectMapper.INSTANCE.toEntity(subjectDto);
        for (String s : subjectDto.getPrerequisiteSubjectId()) {
            Optional<Subject> pre_student = subjectRepository.findById(s);
            pre_student.ifPresent(subject::addPrerequisite);

        }
        subjectRepository.save(subject);
    }
}

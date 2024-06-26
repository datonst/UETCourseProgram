package com.futuresubject.admin.service;

import com.futuresubject.admin.dto.MarkSubjectDto;
import com.futuresubject.admin.dto.NotFoundDataExeption;
import com.futuresubject.admin.dto.search.MarkDto;
import com.futuresubject.admin.mapper.MarkSubjectMapper;
import com.futuresubject.admin.repository.MarkSubjectRepository;
import com.futuresubject.admin.repository.ProgramRepository;
import com.futuresubject.admin.repository.StudentRepository;
import com.futuresubject.admin.repository.SubjectRepository;
import com.futuresubject.common.entity.Enum.RoleType;
import com.futuresubject.common.entity.Entity.MarkSubject;
import com.futuresubject.common.entity.Entity.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MarkSubjectService {
    @Autowired
    private MarkSubjectRepository markSubjectRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ProgramRepository programRepository;

    public List<MarkSubjectDto> getAllMarkSubject(Pageable pagination) {
        Page<MarkSubject> markSubjectList = markSubjectRepository
                .findAll(pagination);
        List<MarkSubjectDto> markSubjectDtoList = new ArrayList<>();
        for (MarkSubject markSubject : markSubjectList) {
            MarkSubjectDto dto =  MarkSubjectMapper.INSTANCE
                    .toDto(markSubject);
            markSubjectDtoList.add(dto);
        }
        return markSubjectDtoList;
    }
    public MarkSubject insert(MarkSubjectDto markSubjectDto) throws NotFoundDataExeption {
        String studentId = markSubjectDto.getStudentId();
        String subjectId = markSubjectDto.getSubjectId();
        if (studentId==null || subjectId ==null) {
            throw new NotFoundDataExeption("subjectId and studentId  is null");
        }
        MarkSubject markSubject = MarkSubjectMapper.INSTANCE.toEntity(markSubjectDto);
        markSubject.setStudent(studentRepository.findById(studentId).get());
        markSubject.setSubject(subjectRepository.findById(subjectId).get());
        Integer id = markSubjectRepository.findId(studentId,subjectId);
        if (id!=null) {
            markSubject.setId(id);
        }
        return markSubjectRepository.save(markSubject);
    }

    public boolean isExist(MarkSubjectDto markSubjectDto) {
        String studentId = markSubjectDto.getStudentId();
        String subjectId = markSubjectDto.getSubjectId();
        if (studentId == null || subjectId==null) {
            return false;
        }
        Integer id = markSubjectRepository.findId(studentId,subjectId);
        return id != null;
    }

    public MarkSubjectDto get(String studentId, String subjectId) throws NotFoundDataExeption {
        if (studentId==null || subjectId ==null) {
            throw new NotFoundDataExeption("subjectId and studentId  is null");
        }
        try {
            MarkSubject markSubject = markSubjectRepository.findMarkSubject(studentId,subjectId);
            MarkSubjectDto markSubjectDto = MarkSubjectMapper.INSTANCE.toDto(markSubject);
            return markSubjectDto;
        } catch (NoSuchElementException ex){
            throw new NotFoundDataExeption("Could not find any mark with subjectId and studentId "
                    + studentId + " " + "subjectId" );
        }
    }
    public void deleteMark(String studentId, String subjectId) {
        Integer id = markSubjectRepository.findId(studentId,subjectId);
        if (id!=null && id!=0) {
            markSubjectRepository.deleteById(id);
        }
    }

    public void updateMark(String studentId, String subjectId, Double mark) throws NotFoundDataExeption {
        if (studentId==null || subjectId ==null || mark==null) {
            throw new NotFoundDataExeption("subjectId or studentId  or mark  is null");
        }
        markSubjectRepository.updateMark(studentId,subjectId,mark);
    }

    public Double sumMark(String studentId, String programFullCode) {
        Integer programId = programRepository.findId(programFullCode);
        return markSubjectRepository.sumMark(studentId,programId);
    }

    public void sumMarkOfStudentList() {
//        List<SearchMark> t = markSubjectRepository.val();
//        for (SearchMark x : t) {
//            System.out.println(x.getStudentId() + " : " + x.getProgramName() + "- "+ x.getSumMark());
//        }





        Program program = programRepository.findByProgramCodeAndAndPeriod("cn8-2019");
        List<MarkDto> markDtos = markSubjectRepository.getMarkByRole("22028245",program.getId(), RoleType.MANDATORY);
        markDtos.sort(Comparator.comparing(MarkDto::getMark).reversed());
        for (MarkDto markDto : markDtos) {
            System.out.println(markDto.getMark() + " has : " + markDto.getCredit());
        }



//        List<Object[]> t = (List<Object[]>) markSubjectRepository.val();
//        for (Object[] x : t) {
//            System.out.println((String)x[0] + ":" + (String) x[1] + " - " + (Double) x[2]);
//        }




    }

    public List<MarkSubjectDto> findMarkSubjectByStudentId(String studentId) {
        List<MarkSubject> markSubjectList = markSubjectRepository.findMarkSubjectByStudentId(studentId);
        List<MarkSubjectDto> markSubjectDtoList = new ArrayList<>();
        for (MarkSubject m : markSubjectList) {
            markSubjectDtoList.add(MarkSubjectMapper.INSTANCE.toDto(m));
        }
        return markSubjectDtoList;
    }
}

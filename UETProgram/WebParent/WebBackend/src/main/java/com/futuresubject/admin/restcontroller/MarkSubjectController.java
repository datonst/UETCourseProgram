package com.futuresubject.admin.restcontroller;

import com.futuresubject.admin.dto.MarkSubjectDto;
import com.futuresubject.admin.dto.NotFoundDataExeption;
import com.futuresubject.admin.service.MarkSubjectService;
import com.futuresubject.admin.service.StudentService;
import com.futuresubject.admin.service.SubjectService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class MarkSubjectController {

    @Autowired
    MarkSubjectService markSubjectService;

    @Autowired
    StudentService studentService;

    @Autowired
    SubjectService subjectService;


    @GetMapping("/marksubjects")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({"ROLE_ADMIN"})
//    @PreAuthorize("hasRole('MODERATOR')")
    public List<MarkSubjectDto> getAllMarkSubject(
            @RequestParam(value = "page", defaultValue = "0")  int page
            , @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Pageable pagination = PageRequest.of(page, size);
        return markSubjectService.getAllMarkSubject(pagination);
    }

    @GetMapping("/marksubjects/new")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({"ROLE_ADMIN"})
    public MarkSubjectDto createMarkSubject() {
        MarkSubjectDto markSubjectDto = new MarkSubjectDto();
        markSubjectDto.setListOfStudentId(studentService.listOfStudentId());
        markSubjectDto.setListOfSubjectId(subjectService.listOfSubjectId());
        return markSubjectDto;
    }

    @GetMapping("/marksubjects/search/{studentId}")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({"ROLE_ADMIN"})
    public List<MarkSubjectDto> getAllMarkByStudentId(
            @PathVariable("studentId") String studentId) {
        return markSubjectService.findMarkSubjectByStudentId(studentId);
    }

    @PostMapping("/marksubjects/new")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({"ROLE_ADMIN"})
    public void saveMarkSubject(@RequestBody MarkSubjectDto markSubjectDto) throws NotFoundDataExeption {
//        if (markSubjectDto.getSubjectId() == null ||
//        markSubjectDto.getStudentId() == null ||
//        markSubjectDto.getMark() == null) {
//            throw new NotFoundDataExeption("Not found - mark subject contain null");
//        }
         markSubjectService.insert(markSubjectDto);

    }

    @GetMapping("/marksubjects/edit/{element}")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({"ROLE_ADMIN"})
    public MarkSubjectDto getEditMark(@PathVariable(name = "element")
                                          String element) throws NotFoundDataExeption {
        String[] arrOfStr = element.split("&", 2);
        String studentId = arrOfStr[0];
        String subjectId = arrOfStr[1];
        MarkSubjectDto markSubjectDto =  markSubjectService.get(studentId,subjectId);
        markSubjectDto.setListOfStudentId(studentService.listOfStudentId());
        markSubjectDto.setListOfSubjectId(subjectService.listOfSubjectId());
        return markSubjectDto;
    }

    @PutMapping("/marksubjects/edit/save")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({"ROLE_ADMIN"})
    public String putEditMark(@RequestBody MarkSubjectDto markSubjectDto) throws NotFoundDataExeption {
        String studentId = markSubjectDto.getStudentId();
        String subjectId = markSubjectDto.getSubjectId();
        Double mark = markSubjectDto.getMark();
        markSubjectService.updateMark(studentId,subjectId,mark);
        return "successs";
    }

    @DeleteMapping("/marksubjects/delete/{element}")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({"ROLE_ADMIN"})
    public void deleteEditStudent(@PathVariable(name = "element") String element) {
        String[] arrOfStr = element.split("&", 2);
        String studentid = arrOfStr[0];
        String subjectid = arrOfStr[1];
        markSubjectService.deleteMark(studentid,subjectid);
    }

}

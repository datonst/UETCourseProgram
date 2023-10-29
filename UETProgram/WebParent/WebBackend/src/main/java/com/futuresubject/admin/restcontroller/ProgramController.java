package com.futuresubject.admin.restcontroller;
import com.futuresubject.admin.dto.ClassroomDto;
import com.futuresubject.admin.dto.MarkSubjectDto;
import com.futuresubject.admin.dto.NotFoundDataExeption;
import com.futuresubject.admin.dto.ProgramDto;
import com.futuresubject.admin.service.FacultyService;
import com.futuresubject.admin.service.ProgramService;
import com.futuresubject.common.entity.MarkSubject;
import com.futuresubject.common.entity.Program;
import com.futuresubject.common.entity.ProgramType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProgramController {
    @Autowired
    private ProgramService programService;
    @Autowired
    private FacultyService facultyService;

    @GetMapping("/programs")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public List<ProgramDto> getAllProgram() {
        return programService.findAll();
    }


    @PostMapping("/programs/new")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CREATED)
    public Program saveProgram(@RequestBody ProgramDto programDto) throws NotFoundDataExeption {
//        if (programDto == null ||programDto.getProgramCode() ==null||programDto.getProgramName()==null) {
//            throw new NotFoundDataExeption("Not found - program contain null");
//        }
        return programService.insert(programDto);


    }

    @GetMapping("/programs/new")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ProgramDto createProgram() {
        ProgramDto programDto = new ProgramDto();
        programDto.setListOfFacultyName(facultyService.listOfFacultyName());
        programDto.setProgramTypeList(Arrays.asList(ProgramType.values()));
        return programDto;
    }


    @GetMapping("/programs/edit/{programFullName}")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ProgramDto getProgram(@PathVariable(name = "programFullName") String programFullName) throws NotFoundDataExeption {
        ProgramDto programDto = programService.get(programFullName);
        programDto.setListOfFacultyName(facultyService.listOfFacultyName());
        programDto.setProgramTypeList(Arrays.asList(ProgramType.values()));
        return programDto;
    }
    @PutMapping("/programs/edit/{programFullName}")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public Program putEditProgram(@PathVariable(name = "programFullName") String programFullName,
                                      @RequestBody ProgramDto programDto) {
        return programService.updateFromDto(programDto);
    }

    @DeleteMapping("/programs/delete/{programFullCode}")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProgram(@PathVariable(name = "programFullCode") String programFullCode) {
        programService.deleteByProgramFullCode(programFullCode);
    }

}

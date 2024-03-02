package com.example.demo.serviceIml;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.controller.BodyResponses;
import com.example.demo.controller.CommonResponses;
import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.TeacherDTO;
import com.example.demo.entity.Course;
import com.example.demo.entity.Teacher;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TeacherService;
import com.example.demo.utils.Utils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    static Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    private final TeacherMapper teacherMapper;

    @Override
    public String addNewTeacher(TeacherDTO teacherDTO) {
        String teacherEmail = teacherDTO.getEmail();
        if (teacherRepository.isExistByEmail(teacherEmail)) {
            String errorMsg = Utils.stringMerger(teacherExistMsg, "E-MAIL: ", teacherEmail);
            log.error(errorMsg);
            throw new BadRequestException(errorMsg);
        }

        // check if mail is valid
        if (!Utils.isMailValid(teacherDTO.getEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        if (teacherDTO.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        teacherRepository.save(teacher);
        log.info("New teacher saved: {}", teacher.getId());

        return BodyResponses.CREATED;
    }

    @Override
    public String updateTeacherById(String teacherEmail, TeacherDTO teacherDTO) {
        Teacher currentTeacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(teacherNotExistMsg, "E-MAIL: ", teacherEmail);
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        // Teacher name processing
        if (teacherDTO.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        currentTeacher.setName(teacherDTO.getName());

        // Check teacher email if valid
        if (!Utils.isMailValid(teacherDTO.getEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        // check if e-mail taken
        if (!teacherEmail.equals(teacherDTO.getEmail())) {
            if (teacherRepository.isExistByEmail(teacherDTO.getEmail())) {
                log.error(CommonResponses.emailTakenMsg);
                throw new BadRequestException(CommonResponses.emailTakenMsg);
            }
            currentTeacher.setEmail(teacherDTO.getEmail());
        }

        // no need process for the DOB becase it's @NonNull in the entity class
        currentTeacher.setDob(teacherDTO.getDob());
        teacherRepository.save(currentTeacher);
        log.info("Teacher updated: {}", currentTeacher.toString());
        return BodyResponses.UPDATED;
    }

    @Override
    public String deleteTeacherById(int teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            String errorMsg = Utils.stringMerger(teacherNotExistMsg, "ID: ", String.valueOf(teacherId));
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        }
        teacherRepository.deleteById(teacherId);
        log.info(teacherSuccessfullyDeleteMsg, "ID: ", String.valueOf(teacherId));
        return BodyResponses.DELETED;
    }

    @Override
    public String deleteByEmail(String teacherEmail) {
        if (!teacherRepository.isExistByEmail(teacherEmail)) {
            String errorMsg = Utils.stringMerger(teacherNotExistMsg, "E-MAIL: ", teacherEmail);
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        }
        teacherRepository.deleteByEmail(teacherEmail);
        log.info(teacherSuccessfullyDeleteMsg, "E-MAIL: ", teacherEmail);
        return BodyResponses.DELETED;
    }

    @Override
    public TeacherDTO getTeacherById(int teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(teacherNotExistMsg, "ID: ", String.valueOf(teacherId));
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        log.info(Utils.stringMerger(teacherSuccessfullyFound, String.valueOf(teacherId)));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    public TeacherDTO getTeacherByEmail(String teacherEmail) {
        Teacher teacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(teacherNotExistMsg, "E-MAIL: ", teacherEmail);
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        log.info(Utils.stringMerger(teacherSuccessfullyFound, teacherEmail));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    public List<TeacherDTO> getTeachers() {
        log.info("getTeachers: All teachers are called.");
        return teacherRepository.findAll().stream().map(t -> teacherMapper.toDTO(t)).toList();
    }

    @Override
    public String enrollCourse(int teacherId, Set<CourseDTO> courses) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(teacherNotExistMsg, "ID: ", String.valueOf(teacherId));
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        for (CourseDTO courseDTO : courses) {
            Course course = courseRepository.findByCourseNameAndIsActiveTrue(courseDTO.getCourseName())
                    .orElseThrow(() -> {
                        String errorMsg = "Course is not exist: ".concat(courseDTO.getCourseName());
                        log.error(errorMsg);
                        throw new NotFoundException(errorMsg);
                    });
            teacher.getTeachingCourses().add(course);
        }

        teacherRepository.save(teacher);
        return BodyResponses.ENROLLED;
    }

}

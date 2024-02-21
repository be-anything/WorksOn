package com.sh.workson.common.controller;

import com.sh.workson.auth.vo.EmployeeDetails;
import com.sh.workson.cherry.entity.Cherry;
import com.sh.workson.cherry.repository.CherryRepository;
import com.sh.workson.dailywork.entity.DailyWork;
import com.sh.workson.dailywork.entity.DailyWorkListDto;
import com.sh.workson.dailywork.repository.DailyWorkRepository;
import com.sh.workson.dailywork.service.DailyWorkService;
import com.sh.workson.employee.entity.Employee;
import com.sh.workson.employee.repository.EmployeeRepository;
import com.sh.workson.employee.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class IndexController {

    @Autowired
    private DailyWorkService dailyWorkService;
    @Autowired
    private DailyWorkRepository dailyWorkRepository;
    @Autowired
    private CherryRepository cherryRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("")
    public String index(
            Authentication authentication,
            @AuthenticationPrincipal EmployeeDetails employeeDetails,
            Model model,  @PageableDefault(value = 10, page = 0) Pageable pageable
    ){
        if(employeeDetails == null){
            return "/auth/login";
        }
        else {

//          민준
            Long id = employeeDetails.getEmployee().getId();

            int totalSeedCount = employeeDetails.getEmployee().getSeed();
            int totalCherryCount = employeeDetails.getEmployee().getCherry();

            Page<DailyWorkListDto> dailyWorkListDtoPage = dailyWorkService.findAll(pageable, id);

            model.addAttribute("totalSeedCount", String.valueOf(totalSeedCount));
            model.addAttribute("totalCherryCount", String.valueOf(totalCherryCount));
            model.addAttribute("dailyworks", dailyWorkListDtoPage.getContent());
            model.addAttribute("totalCount", dailyWorkListDtoPage.getTotalPages());

            log.debug("dailyworks = {}", dailyWorkListDtoPage.getContent());















//            민정

















//            재준










//            준희








//          무진







//          우진







            return "index";
        }
    }

    @Transactional
    @PostMapping("/indexCherry.do")
    public String indexCherry(
            @AuthenticationPrincipal EmployeeDetails employeeDetails,
            @RequestParam("createEmp") List<Long> employees,
            @RequestParam("contentGift") String contentGift,
            @RequestParam("workLog") String workLog,
            @RequestParam("praise") String praise
    ){
        Long id = employeeDetails.getEmployee().getId();
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        DailyWork dailyWork = DailyWork.builder()
                .content(workLog)
                .createdAt(LocalDateTime.now())
                .cherryCount(employees.size() * Integer.parseInt(praise))
                .employee(employeeDetails.getEmployee())
                .build();

        int totalUsedSeedCount = employees.size() * Integer.parseInt(praise);
        int currentSeedCount = employee.getSeed();
        int updatedSeedCount = Math.max(currentSeedCount - totalUsedSeedCount, 0);
        employee.setSeed(updatedSeedCount);

        int totalReceivedCherry = Integer.parseInt(praise);
        employee.updateCherry(totalReceivedCherry);
        employeeRepository.save(employee);


        // Save DailyWork entity
        dailyWorkRepository.save(dailyWork);

        for (Long receivingEmployeeId : employees) {
            Employee receivingEmployee = employeeRepository.findById(receivingEmployeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + receivingEmployeeId));

            Cherry cherry = Cherry.builder()
                    .getCherry(Integer.parseInt(praise))
                    .cherryContent(contentGift)
                    .employee(receivingEmployee)  // cherry를 받는 사람의 아이디로 설정
                    .dailyWork(dailyWork)
                    .build();



            // Save Cherry entity
            cherryRepository.save(cherry);
            log.debug("cherry = {}", cherry);
        }

        return "redirect:/";
    }

}
//            receivingEmployee.updateCherry(Integer.parseInt(praise));
//            employeeRepository.save(receivingEmployee);
package project.task_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.task_app.dto.TaskResponseDto;
import project.task_app.entity.Member;
import project.task_app.entity.Task;
import project.task_app.service.MemberService;
import project.task_app.service.TaskService;

@Controller
@RequiredArgsConstructor
public class HomeController {


    @GetMapping("/")
    public String home(Model model) {

        //현재 로그인된 username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("signedMember", authentication.getName());

        return "index";
    }
}

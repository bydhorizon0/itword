package org.news.itword.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MemberDTO;
import org.news.itword.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Log4j2
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(MemberDTO memberDTO, Model model) {
        model.addAttribute("memberDTO", memberDTO);
        return "/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberDTO memberDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "/signup";
        }

        if (!memberDTO.getPassword().equalsIgnoreCase(memberDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", null, "패스워드가 일치하지 않습니다.");
            return "/signup";
        }

        memberService.join(memberDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

}

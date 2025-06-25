package com.algonet.algonetapi.controllers.mvc;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.ProblemService;
import com.algonet.algonetapi.services.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller("mvcHomeController")
@AllArgsConstructor
@Slf4j
public class MvcHomeController {
    
    private final ProblemService problemService;
    private final TagService tagService;

    @GetMapping("/")
    public String home(Model model, @GetAuthUser User user) {
        log.info("Accessing home page, user: {}", user != null ? user.getUsername() : "anonymous");
        
        // Get recent problems
        Page<Problem> recentProblems = problemService.getAllPaginatedWithAuthor(
            PageRequest.of(0, 6, Sort.by("createdAt").descending())
        );
        log.debug("Retrieved {} recent problems", recentProblems.getContent().size());
        
        // Get popular tags
        List<Tag> popularTags = tagService.getAll();
        log.debug("Retrieved {} tags", popularTags.size());
        
        model.addAttribute("problems", recentProblems.getContent());        model.addAttribute("tags", popularTags);
        model.addAttribute("user", user);

        String view = user != null ? "dashboard" : "home";
        log.info("Rendering view: {} for user: {}", view, user != null ? user.getUsername() : "anonymous");
        return view;
    }

    @GetMapping("/profile")
    public String profile(Model model, @GetAuthUser User user) {
        log.info("Accessing profile page, user: {}", user != null ? user.getUsername() : "anonymous");
        
        if (user == null) {
            log.warn("Unauthorized access to profile page, redirecting to login");
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        log.info("Rendering profile page for user: {}", user.getUsername());
        return "profile";
    }
}

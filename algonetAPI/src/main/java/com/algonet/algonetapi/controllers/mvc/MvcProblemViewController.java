package com.algonet.algonetapi.controllers.mvc;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.problemDTOs.ProblemCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.models.entities.Tag;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.ProblemService;
import com.algonet.algonetapi.services.SolutionService;
import com.algonet.algonetapi.services.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.List;

@Controller("mvcProblemViewController")
@AllArgsConstructor
@RequestMapping("/problems")
@Slf4j
public class MvcProblemViewController {
    
    private final ProblemService problemService;
    private final SolutionService solutionService;
    private final TagService tagService;    @GetMapping("/{id}")
    public String viewProblem(@PathVariable Integer id, Model model, @GetAuthUser User user) {
        log.info("Viewing problem with id: {}, user: {}", id, user != null ? user.getUsername() : "anonymous");
        
        try {
            Problem problem = problemService.getWithAuthor(id);
            log.debug("Retrieved problem: {}", problem.getTitle());
            
            List<Solution> solutions = null;
            if (user != null) {
                solutions = solutionService.getByUserAndProblemId(user, id);
                log.debug("Retrieved {} solutions for user: {}", 
                    solutions != null ? solutions.size() : 0, user.getUsername());
            }
            
            model.addAttribute("problem", problem);
            model.addAttribute("solutions", solutions);
            model.addAttribute("user", user);
            
            log.info("Successfully loaded problem view for id: {}", id);
            return "problems/view";
        } catch (Exception e) {
            log.error("Error viewing problem with id: {}, error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/create")
    public String createProblemForm(Model model, @GetAuthUser User user) {
        log.info("Accessing problem creation form, user: {}", user != null ? user.getUsername() : "anonymous");
        
        if (user == null) {
            log.warn("Unauthenticated user trying to access problem creation form");
            return "redirect:/login";
        }
        
        // Check if user has UPLOADER or ADMIN role
        if (!hasUploaderRole(user)) {
            log.warn("User {} without proper permissions trying to access problem creation", user.getUsername());
            return "redirect:/?error=unauthorized";
        }
        
        List<Tag> tags = tagService.getAll();
        log.debug("Retrieved {} tags for problem creation form", tags.size());
        
        model.addAttribute("tags", tags);
        model.addAttribute("problemCreationDTO", new ProblemCreationDTO());
        
        log.info("Displaying problem creation form for user: {}", user.getUsername());
        return "problems/create";
    }

    @PostMapping("/create")
    public String createProblem(@GetAuthUser User user, 
                               @ModelAttribute ProblemCreationDTO problemCreationDTO,
                               RedirectAttributes redirectAttributes) {
        log.info("Processing problem creation request from user: {}, title: {}", 
            user != null ? user.getUsername() : "anonymous", problemCreationDTO.getTitle());
        
        if (user == null) {
            log.warn("Unauthenticated user trying to create problem");
            return "redirect:/login";
        }
        
        // Check if user has UPLOADER or ADMIN role
        if (!hasUploaderRole(user)) {
            log.warn("User {} without proper permissions trying to create problem", user.getUsername());
            redirectAttributes.addFlashAttribute("error", "You don't have permission to create problems.");
            return "redirect:/";
        }
        
        try {
            Problem createdProblem = problemService.create(user, problemCreationDTO, Instant.now());
            log.info("Problem created successfully with id: {}, title: {}, by user: {}", 
                createdProblem.getId(), createdProblem.getTitle(), user.getUsername());
            redirectAttributes.addFlashAttribute("success", "Problem created successfully!");
            return "redirect:/problems/" + createdProblem.getId();
        } catch (Exception e) {
            log.error("Error creating problem for user: {}, error: {}", user.getUsername(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error creating problem: " + e.getMessage());            return "redirect:/problems/create";
        }
    }

    private boolean hasUploaderRole(User user) {
        String role = user.getRole();
        boolean hasRole = "UPLOADER".equals(role) || "ADMIN".equals(role);
        log.debug("User {} has role: {}, has uploader permissions: {}", user.getUsername(), role, hasRole);
        return hasRole;
    }

    @GetMapping("/{id}/edit")
    public String editProblemForm(@PathVariable Integer id, Model model, @GetAuthUser User user) {
        if (user == null) {
            return "redirect:/login";
        }
        
        Problem problem = problemService.get(id);
        List<Tag> tags = tagService.getAll();
        
        model.addAttribute("problem", problem);
        model.addAttribute("tags", tags);
          return "problems/edit";
    }

    @GetMapping("")
    public String problems(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "createdAt") String sortBy,
                          @RequestParam(defaultValue = "desc") String sortDir,
                          @RequestParam(required = false) String title) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Page<Problem> problems;
        if (title != null && !title.trim().isEmpty()) {
            problems = problemService.searchByTitleWithAuthor(title, PageRequest.of(page, size, sort));
        } else {
            problems = problemService.getAllPaginatedWithAuthor(PageRequest.of(page, size, sort));
        }
        
        model.addAttribute("problems", problems);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", problems.getTotalPages());
        model.addAttribute("searchTitle", title);
        
        return "problems/list";
    }
}

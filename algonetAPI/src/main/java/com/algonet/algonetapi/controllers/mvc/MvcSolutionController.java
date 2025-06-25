package com.algonet.algonetapi.controllers.mvc;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.services.SolutionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;

@Controller("mvcSolutionController")
@AllArgsConstructor
@RequestMapping("/solutions")
@Slf4j
public class MvcSolutionController {
    
    private final SolutionService solutionService;    @PostMapping("")
    public String submitSolution(@GetAuthUser User user,
                                @RequestParam Integer problemId,
                                @RequestParam String sourceCode,
                                RedirectAttributes redirectAttributes) {
        log.info("Solution submission attempt for problem: {}, user: {}", 
            problemId, user != null ? user.getUsername() : "anonymous");
        
        if (user == null) {
            log.warn("Unauthenticated user trying to submit solution for problem: {}", problemId);
            return "redirect:/login";
        }
        
        try {
            SolutionCreationDTO solutionDTO = new SolutionCreationDTO();
            solutionDTO.setCode(sourceCode);
            
            solutionService.create(user, problemId, solutionDTO, Instant.now());
            log.info("Solution submitted successfully for problem: {}, user: {}", problemId, user.getUsername());
            redirectAttributes.addFlashAttribute("success", "Solution submitted successfully!");
        } catch (Exception e) {
            log.error("Error submitting solution for problem: {}, user: {}, error: {}", 
                problemId, user.getUsername(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error submitting solution: " + e.getMessage());
        }
        
        return "redirect:/problems/" + problemId;
    }
}

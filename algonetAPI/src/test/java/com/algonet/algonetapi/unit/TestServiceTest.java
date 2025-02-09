package com.algonet.algonetapi.unit;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.testDTOs.TestCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Test;
import com.algonet.algonetapi.repositories.TestRepository;
import com.algonet.algonetapi.services.TestService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Create test successfully")
    void createTestSuccessfully() {
        Problem problem = new Problem();
        problem.setId(1);
        TestCreationDTO testCreationDTO = new TestCreationDTO("inputData", "outputData");

        when(entityManager.getReference(Problem.class, problem.getId())).thenReturn(problem);
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Test createdTest = testService.create(1, problem.getId(), testCreationDTO);

        assertEquals(testCreationDTO.getInput(), createdTest.getInput());
        assertEquals(testCreationDTO.getOutput(), createdTest.getOutput());
        assertEquals(problem, createdTest.getProblem());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Get test successfully")
    void getTestSuccessfully() {
        Test test = new Test();
        test.setId(1);

        when(testRepository.findById(1)).thenReturn(Optional.of(test));

        Test gottenTest = testService.get(1,1);

        assertEquals(test, gottenTest);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Get test throws NotFoundException")
    void getTestThrowsNotFoundException() {
        when(testRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> testService.get(1,1));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Update test successfully")
    void updateTestSuccessfully() {
        Test test = new Test();
        test.setId(1);
        test.setInput("oldInput");
        test.setOutput("oldOutput");
        TestCreationDTO testCreationDTO = new TestCreationDTO("newInput", "newOutput");

        when(testRepository.findById(1)).thenReturn(Optional.of(test));
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Test updatedTest = testService.update(1,1, testCreationDTO);

        assertEquals(testCreationDTO.getInput(), updatedTest.getInput());
        assertEquals(testCreationDTO.getOutput(), updatedTest.getOutput());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Update test throws NotFoundException")
    void updateTestThrowsNotFoundException() {
        TestCreationDTO testCreationDTO = new TestCreationDTO("input", "output");
        when(testRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> testService.update(1,1, testCreationDTO));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Delete test successfully")
    void deleteTestSuccessfully() {
        doNothing().when(testRepository).deleteById(1);

        assertDoesNotThrow(() -> testService.delete(1,1));
        verify(testRepository, times(1)).deleteById(1);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Get all tests by problem ID successfully")
    void getAllTestsByProblemIdSuccessfully() {
        Problem problem = new Problem();
        problem.setId(1);
        Test test1 = new Test();
        test1.setId(1);
        Test test2 = new Test();
        test2.setId(2);
        List<Test> tests = List.of(test1, test2);

        when(testRepository.findAllByProblem_id(problem.getId())).thenReturn(tests);

        List<Test> gottenTests = testService.getAll(1, problem.getId());

        assertEquals(2, gottenTests.size());
        assertTrue(gottenTests.contains(test1));
        assertTrue(gottenTests.contains(test2));
    }
}

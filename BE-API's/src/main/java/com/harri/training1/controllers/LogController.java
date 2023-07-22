package com.harri.training1.controllers;

import com.harri.training1.models.dto.LogDto;
import com.harri.training1.services.LogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The LogController class is a REST controller that handles log-related requests.
 */
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPERUSER')")
public class LogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);
    private final LogService logService;

    /**
     * Handles the request to retrieve a paginated list of logs.
     *
     * @param pageNumber   the page number to retrieve (default: 0)
     * @return ResponseEntity containing a Page object with the list of logs
     */
    @GetMapping("/paging")
    public ResponseEntity<?> findAllLogsPaging(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        Page<LogDto> logs = logService.findAllLogsPaging(pageNumber);
        LOGGER.info("Get 10 logs for the page number: " + pageNumber);
        return ResponseEntity.ok(logs);
    }

    /**
     * Handles the request to retrieve a list of all logs.
     *
     * @return ResponseEntity containing a list of all logs
     */
    @GetMapping
    public ResponseEntity<?> findAllLogs() {
        List<LogDto> logs = logService.findAll();
        LOGGER.info("Get all logs in the system.");
        return ResponseEntity.ok(logs);
    }

    /**
     * Handles the request to retrieve a log by ID.
     *
     * @param id   the ID of the log to retrieve
     * @return ResponseEntity containing the retrieved log
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getLogById(@PathVariable Long id) {
        LogDto log = logService.findById(id);
        LOGGER.info("Get log with id = " + id);
        return ResponseEntity.ok(log);
    }

    /**
     * Handles the request to delete a log by ID.
     *
     * @param id   the ID of the log to delete
     * @return ResponseEntity with a success message if the log is deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLogById(@PathVariable Long id) {
        logService.deleteById(id);
        LOGGER.info("Delete log with id = " + id);
        return ResponseEntity.ok("Log deleted successfully!");
    }

    /**
     * Handles the request to update a log.
     *
     * @param log   the Log object representing the updated log
     * @return ResponseEntity with a success message if the log is updated successfully
     */
    @PutMapping
    public ResponseEntity<?> updateLog(@RequestBody LogDto log) {
        logService.update(log);
        LOGGER.info("Update log with id = " + log.getId());
        return ResponseEntity.ok("Log updated successfully!");
    }
}
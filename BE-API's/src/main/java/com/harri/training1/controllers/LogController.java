package com.harri.training1.controllers;

import com.harri.training1.models.entities.Log;
import com.harri.training1.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/paging")
    public ResponseEntity<?> findAllLogsPaging(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber){
        Page<Log> logs = logService.findAllLogsPaging(pageNumber);

        if (logs.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No any logs in the system!");

        return ResponseEntity.ok(logs);
    }

    @GetMapping
    public ResponseEntity<?> findAllLogs(){
        List<Log> logs = logService.findAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLogById(@PathVariable Long id){
        Log log = logService.getLogById(id);

        return ResponseEntity.ok(log);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLogById(@PathVariable Long id){
        logService.deleteLogById(id);

        return ResponseEntity.ok("Log deleted successfully!");
    }

    @PutMapping
    public ResponseEntity<?> updateLog(@RequestBody Log log){
        logService.updateLog(log);

        return ResponseEntity.ok("Log updated successfully!");
    }
    @GetMapping("/add")
    public ResponseEntity<?> findAllAddLogs(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber){
        Page<Log> logs = logService.findAllAddLogs(pageNumber);

        if (logs.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No any logs with add action in the system!");

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/update")
    public ResponseEntity<?> findAllUpdateLogs(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber){
        Page<Log> logs = logService.findAllUpdateLogs(pageNumber);

        if (logs.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No any logs with update action in the system!");

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> findAllDeleteLogs(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber){
        Page<Log> logs = logService.findAllDeleteLogs(pageNumber);

        if (logs.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No any logs with delete action in the system!");

        return ResponseEntity.ok(logs);
    }

}

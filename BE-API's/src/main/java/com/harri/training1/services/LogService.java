package com.harri.training1.services;

import com.harri.training1.exceptions.LogsException;
import com.harri.training1.models.entities.Log;
import com.harri.training1.models.entities.User;
import com.harri.training1.models.enums.Action;
import com.harri.training1.repositories.LogsRepository;
import com.harri.training1.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogsRepository logsRepository;

    public void createAddLog() {
        Log log = createLog(Action.ADD, "Add new invoice from user with name: ");
        try {
            logsRepository.save(log);
        }
        catch (Exception e){
            throw new LogsException("Something went wrong: " + e.getMessage());
        }
    }

    public void createUpdateLog(){
        Log log = createLog(Action.UPDATE, "Update invoice from user with name: ");
        try {
            logsRepository.save(log);
        }
        catch (Exception e){
            throw new LogsException("Something went wrong: " + e.getMessage());
        }
    }

    public void createDeleteLog(){
        Log log = createLog(Action.DELETE, "Delete invoice from user with name: ");
        try {
            logsRepository.save(log);
        }
        catch (Exception e){
            throw new LogsException("Something went wrong: " + e.getMessage());
        }    }

    private Log createLog(Action action, String description){
        User user = JwtUtils.getUserFromAuth();
        Log log = new Log();
        log.setUser(user);
        log.setAction(action);
        log.setDescription(description + user.getUsername());

        return log;
    }

    public Page<Log> findAllLogsPaging(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Log> logs = logsRepository.findAll(pageable);

        if (!logs.hasContent())
            throw new LogsException("No any logs available!");

        return logs;
    }

    public Page<Log> findAllAddLogs(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Log> logsPage = logsRepository.findAll(pageable);

        List<Log> filteredLogs = logsPage.getContent().stream()
                .filter(log -> log.getAction().equals(Action.ADD))
                .collect(Collectors.toList());

        if (filteredLogs.isEmpty())
            throw new LogsException("No any add logs available!");

        return new PageImpl<>(filteredLogs, pageable, filteredLogs.size());
    }

    public Page<Log> findAllUpdateLogs(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Log> logsPage = logsRepository.findAll(pageable);

        List<Log> filteredLogs = logsPage.getContent().stream()
                .filter(log -> log.getAction().equals(Action.UPDATE))
                .collect(Collectors.toList());

        if (filteredLogs.isEmpty())
            throw new LogsException("No any update logs available!");

        return new PageImpl<>(filteredLogs, pageable, filteredLogs.size());
    }

    public Page<Log> findAllDeleteLogs(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Log> logsPage = logsRepository.findAll(pageable);

        List<Log> filteredLogs = logsPage.getContent().stream()
                .filter(log -> log.getAction().equals(Action.DELETE))
                .collect(Collectors.toList());

        if (filteredLogs.isEmpty())
            throw new LogsException("No any delete logs available!");

        return new PageImpl<>(filteredLogs, pageable, filteredLogs.size());
    }

    public List<Log> findAllLogs() {
        List<Log> logs = logsRepository.findAll();

        if (logs.isEmpty())
            throw new LogsException("No any logs in the system!");

        return logs;
    }

    public void deleteLogById(Long id){
        if(!logsRepository.existsById(id))
            throw new LogsException("Log with id: " + id + " Not exist!");

        logsRepository.deleteById(id);
    }

    public Log getLogById(Long id){
        Optional<Log> log = logsRepository.findById(id);

        if(log.isEmpty())
            throw new LogsException("Log with id: " + id + " Not exist!");

        return log.get();
    }

    public void updateLog(Log log) {
        try {
            logsRepository.save(log);
        }
        catch (Exception e){
            throw new LogsException("Cannot update log please try latter!");
        }
    }
}

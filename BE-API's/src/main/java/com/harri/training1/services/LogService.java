package com.harri.training1.services;

import com.harri.training1.exceptions.LogsException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.dto.LogDto;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.entities.Log;
import com.harri.training1.models.entities.User;
import com.harri.training1.models.enums.Action;
import com.harri.training1.repositories.LogsRepository;
import com.harri.training1.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * The LogService class provides log-related services.
 */
@Service
@RequiredArgsConstructor
public class LogService implements BaseService<LogDto, Long>{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);
    private final LogsRepository logsRepository;
    private final AutoMapper<Log, LogDto> mapper;
    /**
     * Creates a log for adding an invoice.
     *
     * @param invoice   the Invoice object representing the added invoice
     */
    public void createAddLog(Invoice invoice) {
        Log log = createLog(invoice, Action.ADD, "Add new invoice from user with name: ");
        try {
            logsRepository.save(log);
            LOGGER.info("Create new log with action: (" + log.getAction() + ")" );
        } catch (Exception e) {
            LOGGER.error("Something went wrong when create add log: (" + e.getMessage() + ")");
            throw new LogsException("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Creates a log for updating an invoice.
     *
     * @param invoice   the Invoice object representing the updated invoice
     */
    public void createUpdateLog(Invoice invoice) {
        Log log = createLog(invoice, Action.UPDATE, "Update invoice from user with name: ");
        try {
            logsRepository.save(log);
            LOGGER.info("Create new log with action: (" + log.getAction() + ")" );
        } catch (Exception e) {
            LOGGER.error("Something went wrong when create update log: (" + e.getMessage() + ")");
            throw new LogsException("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Creates a log for deleting an invoice.
     *
     * @param invoice   the Invoice object representing the deleted invoice
     */
    public void createDeleteLog(Invoice invoice) {
        Log log = createLog(invoice, Action.DELETE, "Delete invoice from user with name: ");
        try {
            logsRepository.save(log);
            LOGGER.info("Create new log with action: (" + log.getAction() + ")" );
        } catch (Exception e) {
            LOGGER.error("Something went wrong when create new delete log: (" + e.getMessage() + ")");
            throw new LogsException("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Creates a log object based on the provided invoice, action, and description.
     *
     * @param invoice     the Invoice object associated with the log
     * @param action      the Action representing the log action
     * @param description the description to be included in the log
     * @return the created Log object
     */
    private Log createLog(Invoice invoice, Action action, String description) {
        User user = JwtUtils.getUserFromAuth();
        Log log = new Log();
        log.setInvoice(invoice);
        log.setUser(user);
        log.setAction(action);
        log.setDescription(description + user.getUsername());

        return log;
    }

    /**
     * Retrieves a paginated list of logs.
     *
     * @param pageNumber   the page number to retrieve
     * @return a Page object with the list of logs
     * @throws LogsException if there are no logs available
     */
    public Page<LogDto> findAllLogsPaging(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Log> logs = logsRepository.findAll(pageable);

        if (!logs.hasContent()) {
            LOGGER.error("No and logs for page number: " + pageNumber);
            throw new LogsException("No logs available!");
        }

        return logs.map(log -> mapper.toDto(log, LogDto.class));
    }

    /**
     * Retrieves a list of all logs.
     *
     * @return a list of all logs
     * @throws LogsException if there are no logs in the system
     */
    public List<LogDto> findAll() {
        List<Log> logs = logsRepository.findAll();

        if (logs.isEmpty())
            throw new LogsException("No logs in the system!");

        return logs.stream()
                .map(log -> mapper.toDto(log, LogDto.class)).toList();
    }

    /**
     * Deletes a log by its ID.
     *
     * @param id   the ID of the log to delete
     * @throws LogsException if the log does not exist
     */
    public void deleteById(Long id) {
        if (!logsRepository.existsById(id)) {
            LOGGER.error("Log with id: " + id + " does not exist!");
            throw new LogsException("Log with id: " + id + " does not exist!");
        }

        logsRepository.deleteById(id);
    }

    /**
     * Retrieves a log by its ID.
     *
     * @param id   the ID of the log
     * @return the retrieved log
     * @throws LogsException if the log does not exist
     */
    public LogDto findById(Long id) {
        Optional<Log> log = logsRepository.findById(id);

        if (log.isEmpty()) {
            LOGGER.error("Log with id: " + id + " does not exist!");
            throw new LogsException("Log with id: " + id + " does not exist!");
        }
        return mapper.toDto(log.get(), LogDto.class);
    }

    /**
     * Updates a log.
     *
     * @param logDto  the LogDto object representing the updated log
     * @throws LogsException if the log cannot be updated
     */
    public void update(LogDto logDto) {
        try {
            Log log = mapper.toModel(logDto, Log.class);
            logsRepository.save(log);
        } catch (Exception e) {
            LOGGER.error("Something went wrong when try to update invoice: (" + e.getMessage() + ")");
            throw new LogsException("Something went wrong when try to update invoice: (" + e.getMessage() + ")");
        }
    }

    /**
     * Retrieve the logs with specific action
     *
     * @param action The type of action
     * @return the list of logs based of this action
     */
    public List<LogDto> findByActionType(Action action){
        List<Log> logs = logsRepository.findByActionType(action);

        if(logs.isEmpty())
            throw new LogsException("Logs with type: " + action + " not exist!");

        return logs.stream()
                .map(log -> mapper.toDto(log, LogDto.class))
                .toList();
    }
}
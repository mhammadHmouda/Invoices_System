package com.harri.training1.services;

import com.harri.training1.exceptions.UserFoundException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.dto.UserDto;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.entities.Role;
import com.harri.training1.models.entities.User;
import com.harri.training1.repositories.InvoiceRepository;
import com.harri.training1.repositories.RoleRepository;
import com.harri.training1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * The UserService class provides user-related services.
 */

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserDto, Long>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AutoMapper<User, UserDto> mapper;
    private final RoleRepository rolesRepository;
    private final InvoiceRepository invoiceRepository;

    /**
     * Get all the users in system
     *
     * @return a list of users
     */
    @Override
    public List<UserDto> findAll(){
        List<User> users = userRepository.findAll();

        if(users.isEmpty()) {
            LOGGER.error("No any user exist in the system!");
            throw new UserFoundException("No any user in users table!");
        }

        return users.stream()
                .filter(user -> !user.isDeleted())
                .map(user -> mapper.toDto(user, UserDto.class)).toList();
    }

    /**
     * Get user from repository by user id
     *
     * @param id user id
     * @return a user
     */
    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            LOGGER.error("This user with id = " + id + " not exist!");
            throw new UserFoundException("This user with id = " + id + " not exist!");
        }

        return mapper.toDto(user, UserDto.class);
    }

    /**
     * delete user from repository by user id
     *
     * @param id user id
     */
    @Override
    public void deleteById(Long id){
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            LOGGER.error("This user with id = " + id + " not exist!");
            throw new UserFoundException("User not found!");
        }
        List<Invoice> invoices = user.getInvoices();

        if(invoices != null) {
            LOGGER.info("Delete all invoices associated with this user.");
            invoices.forEach(invoice -> invoice.setDeleted(true));
            invoiceRepository.saveAll(invoices);
        }

        userRepository.softDeleteById(user.getId());
    }

    /**
     * Update the user details and save it user
     *
     * @param userDto the user with the new data
     */
    @Override
    public void update(UserDto userDto){
        Optional<User> userOptional = userRepository.findById(userDto.getId());

        if(userOptional.isEmpty()) {
            LOGGER.error("User with id = " + userDto.getId() + " Not exist!");
            throw new UserFoundException("User with id = " + userDto.getId() + " Not exist!");
        }

        userOptional.get()
                .setRole(new Role(
                        rolesRepository.findIdByRoleName(userDto.getRole().getName())
                ));

        userRepository.save(userOptional.get());
    }

    /**
     * Retrieve the roles exist in the system as list of string
     *
     * @return the list of roles name
     */
    public List<String> findAllRoles(){
        return rolesRepository.findAll()
                .stream().map(Role::getName).toList();
    }

    /**
     * Retrieve the user with specific name
     * @param name the username I want to search based on it
     * @return the list of users
     */
    public UserDto findByUsername(String name){
        Optional<User> user = userRepository.findByUsername(name);

        if (user.isEmpty())
            throw new UserFoundException("User not exist with name: " + name);

        return mapper.toDto(user.get(), UserDto.class);
    }

    /**
     * Retrieve the users with specific role
     *
     * @param roleName the role
     * @return a list of user with specific role
     */
    public List<UserDto> findByRole(String roleName){
        List<User> users = userRepository.findByRole(roleName);

        if (users.isEmpty())
            throw new UserFoundException("User not exist with role: " + roleName);

        return users.stream()
                .map(user -> mapper.toDto(user, UserDto.class))
                .toList();
    }
}

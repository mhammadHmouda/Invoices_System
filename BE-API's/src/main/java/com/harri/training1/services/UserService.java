package com.harri.training1.services;

import com.harri.training1.exceptions.UserFoundException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.dto.UserDto;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.entities.User;
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
            invoices.clear();
        }

        userRepository.delete(user);
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

        userOptional.get().setRole(userDto.getRole());
        userRepository.save(userOptional.get());
    }

}

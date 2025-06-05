package com.pm.unitalk.Service.ServiceImpl;


import com.pm.unitalk.DTOs.UserDTO;
import com.pm.unitalk.Exceptions.ResourceAlreadyExist;
import com.pm.unitalk.Exceptions.ResourceNotFoundException;
import com.pm.unitalk.Model.LocalUser;
import com.pm.unitalk.Repository.LocalUserRepo;
import com.pm.unitalk.Service.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserServices {
    @Autowired
    private LocalUserRepo localUserRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO){

        List<LocalUser> userList=localUserRepo.findByEmailIgnoreCase(userDTO.getEmail());
        if(userList.isEmpty()){
            LocalUser localUser=modelMapper.map(userDTO, LocalUser.class);
            // Encode the password before saving
            localUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            LocalUser savedUser=localUserRepo.save(localUser);
            return modelMapper.map(savedUser,UserDTO.class);
        }
        else {
            log.error("User already exist exception has been thrown, User Registration request:"+ userDTO);
            throw new ResourceAlreadyExist(userDTO.getFirstName()+" "+userDTO.getLastName(),userList.get(0).getId());

        }

    }





    @Override
    public List<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sort;
        if(direction.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else{
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<LocalUser> localUsers=localUserRepo.findAll(pageable);
        List<UserDTO> userDTOS=localUsers.stream().map((localUser -> modelMapper.map(localUser,UserDTO.class))).collect(Collectors.toUnmodifiableList());

        return userDTOS;
    }
    @Override
    public List<UserDTO> searchByUserName(String userName, int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sort;
        if(direction.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<LocalUser> localUsers=localUserRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(userName,userName,pageable);
        List<UserDTO> userDTOS=localUsers.stream().map((user)-> modelMapper.map(user,UserDTO.class)).collect(Collectors.toUnmodifiableList());

        return userDTOS;
    }






                                                     //Project

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
        localUser.setFirstName(userDTO.getFirstName());
        localUser.setLastName(userDTO.getLastName());
        localUser.setEmail(userDTO.getEmail());
        LocalUser savedLocalUser=localUserRepo.save(localUser);
        return modelMapper.map(savedLocalUser,UserDTO.class);
    }

    @Override
    public UserDTO getUser(Long userId) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));

        return modelMapper.map(localUser,UserDTO.class);
    }

    @Override
    public boolean deleteUser(Long userId) {
        LocalUser localUser=localUserRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
        try {
            localUserRepo.deleteById(userId);
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
return true;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        List<LocalUser> users = localUserRepo.findByEmailIgnoreCase(email);
        if (users.isEmpty()) {
            return null; // Return null if no user is found with the given email
        }
        return modelMapper.map(users.get(0), UserDTO.class);
    }
}

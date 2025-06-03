package com.pm.unitalk.Service;


import com.pm.unitalk.DTOs.UserDTO;

import java.util.List;

public interface UserServices {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    UserDTO getUser(Long userId);
    boolean deleteUser(Long userId);
    List<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String direction);
    List<UserDTO> searchByUserName(String userName,int pageNumber, int pageSize, String sortBy, String direction);
    UserDTO findUserByEmail(String email);
}

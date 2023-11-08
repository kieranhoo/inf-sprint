package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.UsersRepository;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.OneUserResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService extends SupportFunction {
    private final UsersRepository usersRepository;

    public ListResponse getAllUsers() {
        List<UsersEntity> users = usersRepository.findAllUsers();
        return new ListResponse(users);
    }

    public UsersEntity getUserById(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        return usersRepository.findUserById(id);
    }

    public OneUserResponse getOneUser(String accessToken) {
        String[] chunks = accessToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            int userId = jsonNode.get("userId").asInt();

            JsonNode rolesArray = jsonNode.get("roles");
            String[] roles = new String[rolesArray.size()];
            for (int i = 0; i < rolesArray.size(); i++) {
                roles[i] = rolesArray.get(i).asText();
            }

            UsersEntity usersEntity = usersRepository.findUserById(userId);
            if (usersEntity == null) {
                throw new NotFoundException("User doesn't exist.");
            }
            return new OneUserResponse(usersEntity.getId(),usersEntity.getUsername(),usersEntity.getEmail(),usersEntity.getDepartmentId());

        } catch (Exception e){
            throw new NotFoundException("Not found information from access token");
        }
    }
}

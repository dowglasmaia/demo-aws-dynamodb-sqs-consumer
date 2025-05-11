package com.dowglasmaia.sqssnsconsumer.service;

import com.dowglasmaia.sqssnsconsumer.model.UserData;
import com.dowglasmaia.sqssnsconsumer.repositoy.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void save(UserData data) {
        repository.save(data);
    }

}

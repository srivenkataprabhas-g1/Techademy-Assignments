package com.prabhas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhas.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

}

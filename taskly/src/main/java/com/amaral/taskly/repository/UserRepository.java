package com.amaral.taskly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amaral.taskly.model.User;

import jakarta.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
}

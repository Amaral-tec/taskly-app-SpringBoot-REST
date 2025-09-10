package com.amaral.taskly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amaral.taskly.model.UserProfile;

import jakarta.transaction.Transactional;

@Transactional
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

}

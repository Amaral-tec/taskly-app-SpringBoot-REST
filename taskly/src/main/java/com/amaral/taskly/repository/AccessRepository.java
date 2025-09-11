package com.amaral.taskly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amaral.taskly.model.Access;

import jakarta.transaction.Transactional;

@Transactional
public interface AccessRepository extends JpaRepository<Access, Long> {

    List<Access> findByName(String name);
}

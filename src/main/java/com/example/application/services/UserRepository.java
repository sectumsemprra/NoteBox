package com.example.application.services;

import com.example.application.data.Userr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Userr, Integer> {
     Userr getByUsername(String username);
}

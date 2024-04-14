package com.example.application.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
<<<<<<< HEAD
<<<<<<< HEAD

import java.util.List;

=======
=======
>>>>>>> e2c7e117f1dea66fcb6273f02b5df204e34136d5
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
<<<<<<< HEAD
>>>>>>> e2c7e11 (added primary login page)
=======
>>>>>>> e2c7e117f1dea66fcb6273f02b5df204e34136d5
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select c from Contact c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Contact> search(@Param("searchTerm") String searchTerm);
}

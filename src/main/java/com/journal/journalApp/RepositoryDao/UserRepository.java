package com.journal.journalApp.RepositoryDao;

import com.journal.journalApp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Change the method name to match the property name in the User entity
    User findByUsername(String username);

    void deleteByUsername(String username);


    @Query(value = "SELECT * FROM users u WHERE u.sentimental_analysis = true AND u.email IS NOT NULL AND u.email != '' AND u.email REGEXP '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$'", nativeQuery = true)
    List<User> findUserWhoSelectedSA();
}

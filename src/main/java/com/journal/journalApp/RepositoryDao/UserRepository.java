package com.journal.journalApp.RepositoryDao;

import com.journal.journalApp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Change the method name to match the property name in the User entity
    User findByUsername(String username);

    void deleteByUsername(String username);
}

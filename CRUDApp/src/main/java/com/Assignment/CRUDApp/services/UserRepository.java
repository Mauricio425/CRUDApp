
package com.Assignment.CRUDApp.services;

import com.Assignment.CRUDApp.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Mauricio
 */

//JPARepoInterface requires a type, which will be User
//and a primary key which for us is integer
public interface UserRepository extends JpaRepository<User, Integer> {
    //SpringJPA implements this interface for us
    //What you need next is a controller to perform the CRUD operations on Users
}

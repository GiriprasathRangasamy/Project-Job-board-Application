package com.example.projectspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectspring.Model.docverifyuser;

public interface DocverifyUserRepositor extends JpaRepository<docverifyuser, Long>{
    docverifyuser findByUsername(String username);

}

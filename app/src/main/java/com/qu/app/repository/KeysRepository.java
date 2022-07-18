package com.qu.app.repository;

import com.qu.app.entity.Keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KeysRepository extends JpaRepository<Keys, Long> {
    Keys findByName(String name);
}


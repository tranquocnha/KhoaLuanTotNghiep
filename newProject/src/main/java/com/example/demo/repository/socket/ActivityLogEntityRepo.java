package com.example.demo.repository.socket;

import com.example.demo.model.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogEntityRepo extends JpaRepository<ActivityLogEntity,Long> {

}

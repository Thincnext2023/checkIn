package com.example.Checkin.repository;

import com.example.Checkin.entity.CheckInDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInDetailsRepo extends JpaRepository<CheckInDetails,Long> {
    @Query("SELECT c FROM CheckInDetails c WHERE c.stat = 'checkIn'")
    List<CheckInDetails> findByStat();

}

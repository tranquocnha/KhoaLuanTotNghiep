package com.example.demo.repository.tempAuctionRepo;

import com.example.demo.model.TempAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempAuctionRepository extends JpaRepository<TempAuction,Integer> {
}

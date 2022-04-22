package com.example.demo.repository.auctionRepository;

import com.example.demo.model.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    Auction findByProduct_StatusAndProduct_IdProduct(String status,int id);

    Page<Auction> findAllByAuctionTimeContains(String auctionTime, Pageable pageable);
}

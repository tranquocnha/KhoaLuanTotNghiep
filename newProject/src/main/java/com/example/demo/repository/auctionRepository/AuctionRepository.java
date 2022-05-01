package com.example.demo.repository.auctionRepository;

import com.example.demo.model.Auction;
import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    Auction findByProduct_StatusAndProduct_IdProduct(String status,int id);

    Page<Auction> findAllByAuctionTimeContains(String auctionTime, Pageable pageable);

    @Query(value = "SELECT a.* " +
            "FROM product as p " +
            "   inner join auction as a on p.id_product = a.product_id_product\n" +
            "WHERE a.start_date >= ?1 and  a.end_date <= ?2 ",nativeQuery = true)
    List<Auction> findBySearchDayStartAndDayEnd(String dayStart, String dayEnd );

    @Query(value = "SELECT * " +
            "FROM product as p " +
            "   inner join auction as a on p.id_product = a.product_id_product\n" +
            "WHERE a.start_date >= ?1 ",nativeQuery = true)
    List<Auction> findBySearchDayStart(String dayStart);

    @Query(value = "SELECT a.* " +
            "FROM product as p " +
            "   inner join auction as a on p.id_product = a.product_id_product\n" +
            "WHERE a.end_date <= ?1 ",nativeQuery = true)
    List<Auction> findBySearchDayEnd( String dayEnd);



}

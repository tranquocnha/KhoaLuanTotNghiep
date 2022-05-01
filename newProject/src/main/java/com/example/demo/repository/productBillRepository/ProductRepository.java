package com.example.demo.repository.productBillRepository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

//    List<Product> findByStatusAndCategory_IdCategoryOrderByPrice(String status , int category);

    // comment - product inner join
    @Query(value = "select p from  Product p inner join Comment cm on cm.product.idProduct=p.idProduct")
    Page<Product> findProductAndComment(Pageable pageable);

    @Query("select p from  Product p where p.accounts.idAccount= ?1")
    List<Product> findProduct(String idAccount);

    List<Product> findAllByStatusContaining(String status);

    List<Product> findByStatus(String status);

    Page<Product> findByProductNameContains(String nameProduct,Pageable pageable);

    List<Product> findByStatusAndProductNameContains(String status,String nameProduct);

    @Query("select p " +
            "from  Product p " +
            "where p.status = ?1 and p.category.idCategory = ?2 and p.productName like %?3%")
    List<Product> findByStatusAndCategory_IdCategoryAndProductNameContainsOrderByPrice(String status, Integer idCategory, String nameProduct);

    @Query("select p " +
            "from  Product p " +
            "where p.status = ?1 and p.category.idCategory = ?2 ")
    List<Product> findByStatusAndCategory_IdCategoryOrderByPrice(String status, Integer idCategory);

    @Query("select p " +
            "from  Product p " +
            "where p.status = ?1")
    List<Product> findAllApproved(String status);

    @Query("select p " +
            "from Product p " +
            "where p.status = ?1 and p.accounts.idAccount = ?2")
    List<Product> findAllByNotApprovedYet(String status , String idAccount);

    @Query("select p " +
            "from  Product p " +
            "where p.accounts.idAccount= ?1")
    List<Product> findAccount(String idAccount);

    @Query("select p " +
            "from  Product p " +
            "where p.status = ?1  and p.accounts.idAccount = ?2 and p.productName like %?3%")
    List<Product> findByProductNameNotApprovedYet(String status, String idAccount, String nameProduct);

    @Query("select p " +
            "from  Product p " +
            "where p.accounts.idAccount= ?1 and p.productName like %?2%")
    List<Product> findByMyNameProduct(String idAccount, String nameProduct);

    @Query("select p " +
            "from  Product p inner join Auction a on p.idProduct=a.product.idProduct " +
            "   where p.status = ?1 " +
            "   and p.category.idCategory = ?2 " +
            "   and p.auction.startDate <= current_date and p.auction.endDate >= current_date and p.auction.auctionTime >= current_time ")
    List<Product> findByStatusAndCategory_IdCategoryAndAuction_IdProductOrderByPrice(String status, Integer idCategory);

    @Query(value = "select * from  product as p\n" +
            "    inner join auction a on p.id_product = a.product_id_product\n" +
            "    inner join category c on p.id_category = c.id_category\n" +
            "where p.status like ?1\n" +
            "    and c.id_category = ?2\n" +
            "    and a.start_date <= CURDATE() and a.end_date >= CURDATE() and a.auction_time >= CURTIME()",nativeQuery = true)
    List<Product> findByAuction(String status, Integer idCategory);

    @Query("select p " +
            "from  Product p,Auction a " +
            "where p.category.idCategory = ?1 and p.idProduct=a.product.idProduct and p.idProduct=a.product.idProduct ")
    List<Product> findAllByCategory_IdCategory(int idCategory);

    //page
    @Query("select p " +
            "from Product p inner join Color c on p.idProduct=c.product.idProduct " +
            "where p.status = 'Đã duyệt' " +
            "   and p.productName like %?1% " +
            "   or c.color like %?1% " +
            "   or p.datePost like %?1% " +
            "   or p.category.categoryName like %?1% " +
            "group by p.idProduct")
    Page<Product> findAllSearch(String nameProduct,Pageable pageable);

    Page<Product> findByStatus(String status,Pageable pageable);

    @Query("select p " +
            "from Product p inner join Color c on p.idProduct=c.product.idProduct " +
            "where p.status = 'Đã duyệt' " +
            "group by p.idProduct order by c.price asc ")
    Page<Product> findByStatusOrderByProductNameColorAsc(Pageable pageable);


    @Query("select p " +
            "from Product p inner join Color c on p.idProduct=c.product.idProduct " +
            "where p.status = 'Đã duyệt' " +
            "group by p.idProduct order by c.price desc ")
    Page<Product> findByStatusOrderByProductNameColorDesc(Pageable pageable);
    @Query("select p " +
            "from Product p inner join Color c on p.idProduct=c.product.idProduct " +
            "where p.status = 'Đã duyệt' " +
            "group by p.idProduct order by p.idProduct desc ")
    Page<Product> findByStatusOrderByProductNameIdProductDesc(Pageable pageable);

    @Query("select p " +
            "from Product p inner join Color c on p.idProduct=c.product.idProduct " +
            "where p.status = 'Đã duyệt' " +
            "and c.price >= ?1 and c.price <= ?2 " +
            "group by p.idProduct")
    Page<Product> findByStatusAndSearchPrice(Double min ,Double max , Pageable pageable);


    Page<Product> findByStatusAndProductNameContains(String status,String nameProduct,Pageable pageable);

    Product findByStatusAndIdProduct(String status, int idProduct);
}

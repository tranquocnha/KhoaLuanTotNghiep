package com.example.demo.repository.colorRepository;

import com.example.demo.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    List<Color> findAllByProduct_IdProduct(int idProduct);

    Color findByProduct_IdProduct(int idProduct);

    List<Color> findAllByProduct_Accounts_IdAccount(String idAccount);

    List<Color> findAllByProduct_StatusAndProduct_Accounts_IdAccount(String status, String idAccount);

    Page<Color> findAllByProduct_StatusAndProduct_Accounts_IdAccount(String status, String idAccount, Pageable pageable);

    Page<Color> findAllByProduct_Accounts_UserIdUser(int idAccount, Pageable pageable);

    Page<Color> findAllByProduct_Accounts_IdAccount(String idAccount, Pageable pageable);

    //list
    @Query(value = "select * from color group by color", nativeQuery = true)
    List<Color> findListColorByColor();

    @Query(value = "select * from color where color=:color", nativeQuery = true)
    Page<Color> findByColor(String color, Pageable pageable);

    @Query(value = "select  * from color c inner join product p on p.id_product=c.id_product inner join category ct on ct.id_category=p.id_category where ct.id_category=:categoryId", nativeQuery = true)
    Page<Color> findColorByCategoryId(int categoryId, Pageable pageable);

    @Query(value = "select  * from color c inner join product p on p.id_product=c.id_product inner join category ct on ct.id_category=p.id_category where ct.id_category=:categoryId and c.color=:colorName", nativeQuery = true)
    Page<Color> findColorByCategoryIdAndColorName(int categoryId, String colorName,Pageable pageable);

    @Query("select c , min(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct")
    List<Color> findByProduct_StatusAndProduct_Category_IdCategory(String status, Integer category);

    @Query("select c , min(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct " +
            "order by min(c.price) desc ")
    List<Color> findByProduct_StatusAndProduct_Category_IdCategoryDesc(String status, Integer category);

    @Query("select c , min(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct " +
            "order by min(c.price) asc ")
    List<Color> findByProduct_StatusAndProduct_Category_IdCategoryAsc(String status, Integer category);


    @Query("select c , min(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2  and p.productName like %?3%" +
            "group by c.product.idProduct")
    List<Color> findByProduct_StatusAndProduct_Category_IdCategoryAndProduct_ProductName(String status, Integer category, String nameProduct);


    @Query("select c , min(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct " +
            "order by c.product.idProduct desc")
    List<Color> findByProduct_StatusAndProduct_Category_IdCategoryOrderByProductDesc(String status, Integer category);

    List<Color> findByProduct_Status(String status);


    @Query("select c , max(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 " +
            "group by c.product.idProduct " +
            "order by min(c.price) asc")
    List<Color> findByProduct_StatusAscLIMIT5(String status, Pageable pageable);

    @Query("select c , min(c.price) as min1 " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 " +
            "group by c.product.idProduct " +
            "order by min(c.price) desc ")
    List<Color> findByProduct_StatusDescLIMIT5(String status, Pageable pageable);

    //page
    @Query("select c , min(c.price) as price " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct")
    Page<Color> findByProduct_StatusAndProduct_Category_IdCategory(String status, Integer category, Pageable pageable);

    @Query("select c , min(c.price) as price " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2  and p.productName like %?3%" +
            "group by c.product.idProduct")
    Page<Color> findByProduct_StatusAndProduct_Category_IdCategoryAndProduct_ProductName(String status, Integer category, String nameProduct, Pageable pageable);

    @Query("select c , min(c.price) as price " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct " +
            "order by min(c.price) desc ")
    Page<Color> findByProduct_StatusAndProduct_Category_IdCategoryDesc(String status, Integer category, Pageable pageable);

    @Query("select c , min(c.price) as price " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct " +
            "order by min(c.price) asc ")
    Page<Color> findByProduct_StatusAndProduct_Category_IdCategoryAsc(String status, Integer category, Pageable pageable);

    @Query("select c , min(c.price) as price " +
            "from Color c inner join Product p on p.idProduct = c.product.idProduct " +
            "where p.status = ?1 and p.category.idCategory = ?2 " +
            "group by c.product.idProduct " +
            "order by c.product.idProduct desc")
    Page<Color> findByProduct_StatusAndProduct_Category_IdCategoryOrderByProductDesc(String status, Integer category, Pageable pageable);
}

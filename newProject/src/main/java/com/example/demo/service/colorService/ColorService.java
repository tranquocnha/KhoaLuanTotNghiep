package com.example.demo.service.colorService;

import com.example.demo.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ColorService {
    List<Color> findAll();

    Color findById(int idColor);

    void save(Color color);

    void delete(int idColor);

    List<Color> findByIdProduct(int id);

    Page<Color> findAllPage(Pageable pageable);
    Page<Color> findAllByProduct(String idAccount, Pageable pageable);

    List<Color> findAllProduct(String idAccount);

    List<Color> findAllApprovedProduct(String status , String idAccount);

    Page<Color> findAllApprovedProduct(String status , String idAccount,Pageable pageable);
    Page<Color> findAllPageById(int idAccount,Pageable pageable);
    List<Color> findProduct(String status , Integer idCategory);
    List<Color> findProduct(String status , Integer idCategory,String nameProduct);
    List<Color> findByProduct_Status(String status);
    List<Color> findProductOderByDesc(String status , Integer idCategory);
    List<Color> findProductOderByAsc(String status , Integer idCategory);
    List<Color> findProductOderByProductDesc(String status, Integer idCategory);

    List<Color> findProductOderByLIMIT5(String status);
    List<Color> findProductOderByDescLIMIT5(String status);
    //page
    Page<Color> findProduct(String status , Integer idCategory,Pageable pageable);
    Page<Color> findProduct(String status , Integer idCategory,String nameProduct,Pageable pageable);
    Page<Color> findProductOderByDesc(String status , Integer idCategory,Pageable pageable);
    Page<Color> findProductOderByAsc(String status , Integer idCategory,Pageable pageable);
    Page<Color> findProductOderByProductDesc(String status, Integer idCategory,Pageable pageable);
}

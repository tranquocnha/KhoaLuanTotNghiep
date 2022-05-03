package com.example.demo.service.colorService;

import com.example.demo.model.Color;
import com.example.demo.repository.colorRepository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    ColorRepository colorRepository;

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public Color findById(int idColor) {
        return colorRepository.findById(idColor).orElse(null);
    }

    @Override
    public void save(Color color) {
        colorRepository.save(color);
    }

    @Override
    public void delete(int idColor) {
        colorRepository.deleteById(idColor);
    }


    @Override
    public List<Color> findByIdProduct(int idProduct) {
        return colorRepository.findAllByProduct_IdProduct(idProduct);
    }

    @Override
    public Page<Color> findAllPage(String idAccount,Pageable pageable) {
        return colorRepository.findAllByProduct_Accounts_IdAccount(idAccount,pageable);
    }

    @Override
    public Page<Color> findAllPageById(int idAccount, Pageable pageable) {
        return colorRepository.findAllByProduct_Accounts_UserIdUser(idAccount,pageable);
    }

    @Override
    public List<Color> findAllProduct(String idAccount) {
        return colorRepository.findAllByProduct_Accounts_IdAccount(idAccount);
    }

    @Override
    public Page<Color> findAllApprovedProduct(String status, String idAccount,Pageable pageable) {
        return colorRepository.findAllByProduct_StatusAndProduct_Accounts_IdAccount(status , idAccount,pageable);
    }

    @Override
    public List<Color> findProduct(String status, Integer idCategory) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategory(status, idCategory);
    }

    @Override
    public List<Color> findProduct(String status, Integer idCategory, String nameProduct) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryAndProduct_ProductName(status,idCategory,nameProduct);
    }

    @Override
    public List<Color> findByProduct_Status(String status) {
        return colorRepository.findByProduct_Status(status);
    }

    @Override
    public List<Color> findProductOderByDesc(String status, Integer idCategory) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryDesc(status,idCategory);
    }

    @Override
    public List<Color> findProductOderByAsc(String status, Integer idCategory) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryAsc(status,idCategory);
    }

    @Override
    public List<Color> findProductOderByProductDesc(String status, Integer idCategory) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryOrderByProductDesc(status,idCategory);
    }
    // Page
    @Override
    public Page<Color> findProduct(String status, Integer idCategory, Pageable pageable) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategory(status, idCategory,pageable);
    }

    @Override
    public Page<Color> findProduct(String status, Integer idCategory, String nameProduct, Pageable pageable) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryAndProduct_ProductName(status,idCategory,nameProduct,pageable);
    }

    @Override
    public Page<Color> findProductOderByDesc(String status, Integer idCategory, Pageable pageable) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryDesc(status,idCategory,pageable);
    }

    @Override
    public Page<Color> findProductOderByAsc(String status, Integer idCategory, Pageable pageable) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryAsc(status,idCategory,pageable);
    }

    @Override
    public Page<Color> findProductOderByProductDesc(String status, Integer idCategory, Pageable pageable) {
        return colorRepository.findByProduct_StatusAndProduct_Category_IdCategoryOrderByProductDesc(status,idCategory,pageable);
    }
}

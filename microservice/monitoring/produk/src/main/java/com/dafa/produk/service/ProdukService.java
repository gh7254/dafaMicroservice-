package com.dafa.produk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dafa.produk.model.Produk;
import com.dafa.produk.repository.ProdukRepository;

@Service
public class ProdukService {
  @Autowired
  private ProdukRepository produkRepository;

  public List<Produk> getAllProduks() {
    return produkRepository.findAll();
  }

  public Produk getProdukById(Long Id) {
    return produkRepository.findById(Id).orElse(null);
  }

  public Produk createProduk(Produk produk) {
    return produkRepository.save(produk);
  }

  public void deleteProduk(Long id) {
    produkRepository.deleteById(id);
  }
}

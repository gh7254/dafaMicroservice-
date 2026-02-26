package com.dafa.produk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dafa.produk.model.Produk;
import com.dafa.produk.service.ProdukService;

@RestController
@RequestMapping("/api/produk")
public class ProdukController {
  @Autowired
  private ProdukService produkService;

  @GetMapping()
  public List<Produk> getAllProduk() {
    return produkService.getAllProduks();
  }

  @GetMapping("/id")
  public ResponseEntity<Produk> getProdukById(@PathVariable Long id) {
    Produk produk = produkService.getProdukById(id);
    return produk != null ? ResponseEntity.ok(produk) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public Produk createProduk(@RequestBody Produk produk) {
    return produkService.createProduk(produk);
  }

  @DeleteMapping("/id")
  public ResponseEntity<?> deleteProduk(@PathVariable Long id) {
    return ResponseEntity.ok().build();
  }

}

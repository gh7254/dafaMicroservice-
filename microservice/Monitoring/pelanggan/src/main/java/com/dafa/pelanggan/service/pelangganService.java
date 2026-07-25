package com.dafa.pelanggan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dafa.pelanggan.model.pelanggan;
import com.dafa.pelanggan.repository.pelangganRepository;

@Service
public class pelangganService {
    @Autowired
    private pelangganRepository pelangganRepository;

    public List<pelanggan> getAllPelanggan() {
        return pelangganRepository.findAll();
    }

    public pelanggan getPelangganById(Long id) {
        return pelangganRepository.findById(id).orElse(null);
    }

    public pelanggan savePelanggan(pelanggan p) {
        return pelangganRepository.save(p);
    }

    public pelanggan createPelanggan(pelanggan p) {
        return pelangganRepository.save(p);
    }

    public void deletePelanggan(Long id) {
        pelangganRepository.deleteById(id);
    }
}
package pe.edu.utp.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.utp.biblioteca.model.Categoria;
import pe.edu.utp.biblioteca.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }


    public Categoria guardar(Categoria categoria) {
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }


}
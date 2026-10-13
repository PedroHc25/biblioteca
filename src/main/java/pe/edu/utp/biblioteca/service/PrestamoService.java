package pe.edu.utp.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.utp.biblioteca.model.Prestamo;
import pe.edu.utp.biblioteca.repository.PrestamoRepository;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;

    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }


    public Prestamo guardar(Prestamo prestamo) {
        prestamo.setId(null);
        return prestamoRepository.save(prestamo);
    }


}
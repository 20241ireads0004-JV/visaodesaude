package br.com.ifba.alerta.service;

import br.com.ifba.alerta.entity.Alerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertaIService {

    /*
     * Salva um novo alerta no banco de dados.
     */
    Alerta save(Alerta alerta);

    /*
     * Retorna uma lista com todos
     * os alerta cadastrados usando paginação.
     */
    Page<Alerta> findAll(Pageable pageable);

    /*
     * Retorna o alerta que contém
     * o ID informado
     */
    Alerta findById(Long id);

    /*
     * Remove um alerta do banco
     * através do ID informado.
     */
    void delete(Long id);

    /*
     * Atualiza os dados de um alerta.
     */
    Alerta update(Long id, Alerta alerta);
}

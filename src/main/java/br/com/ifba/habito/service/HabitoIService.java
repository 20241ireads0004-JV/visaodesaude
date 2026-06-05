package br.com.ifba.habito.service;

import br.com.ifba.habito.entity.Habito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HabitoIService {

    /*
     * Salva um novo habito no banco de dados.
     */
    Habito save(Habito habito);

    /*
     * Retorna uma lista com todos
     * os habito cadastrados usando paginação.
     */
    Page<Habito> findAll(Pageable pageable);

    /*
     * Retorna o habito que contém
     * o ID informado
     */
    Habito findById(Long id);

    /*
     * Remove um habito do banco
     * através do ID informado.
     */
    void delete(Long id);

    /*
     * Atualiza os dados de um habito.
     */
    Habito update(Long id, Habito habito);
}

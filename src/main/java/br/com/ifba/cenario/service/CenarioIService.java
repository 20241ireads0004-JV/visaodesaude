package br.com.ifba.cenario.service;

import br.com.ifba.cenario.entity.Cenario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CenarioIService {

    /*
     * Salva um novo cenario no banco de dados.
     */
    Cenario save(Cenario cenario);

    /*
     * Retorna uma lista com todos
     * os cenarios cadastrados usando paginação.
     */
    Page<Cenario> findAll(Pageable pageable);

    /*
     * Retorna o cenario que contém
     * o ID informado
     */
    Cenario findById(Long id);

    /*
     * Remove um cenario do banco
     * através do ID informado.
     */
    void delete(Long id);

    /*
     * Atualiza os dados de um cenario.
     */
    Cenario update(Long id, Cenario cenario);
}

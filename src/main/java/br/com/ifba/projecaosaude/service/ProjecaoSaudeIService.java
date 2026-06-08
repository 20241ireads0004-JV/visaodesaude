package br.com.ifba.projecaosaude.service;

import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjecaoSaudeIService {

    /*
     * Salva uma novo projeção de saúde no banco de dados.
     */
    ProjecaoSaude save(ProjecaoSaude projecaoSaude);

    /*
     * Retorna uma lista com todas
     * as projeções de saúde cadastradas usando paginação.
     */
    Page<ProjecaoSaude> findAll(Pageable pageable);

    /*
     * Retorna a projeção de saúde que contém
     * o ID informado
     */
    ProjecaoSaude findById(Long id);

    /*
     * Remove uma projeção de saúde do banco
     * através do ID informado.
     */
    void delete(Long id);

    /*
     * Atualiza os dados de uma projeção de saúde.
     */
    ProjecaoSaude update(Long id, ProjecaoSaude projecaoSaude);
}

package br.com.ifba.meta.service;

import br.com.ifba.meta.entity.Meta;

import java.util.List;

public interface MetaIService {

    // Cadastrar
    Meta save(Long usuarioId, Meta meta);

    // Editar
    Meta update(Long id, Long usuarioId, Meta meta);

    // Excluir
    void delete(Long id, Long usuarioId);

    // Listar por Usuário
    List<Meta> listarPorUsuario(Long usuarioId);

    // Buscar por ID
    Meta findById(Long id);
}

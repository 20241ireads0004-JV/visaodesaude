package br.com.ifba.gestorcorporativo.service;

import br.com.ifba.gestorcorporativo.entity.GestorCorporativo;

import java.util.List;
import java.util.Map;

public interface GestorCorporativoIService {

    GestorCorporativo cadastrar (GestorCorporativo gestor, Long idEmpresa);

    GestorCorporativo editar (Long id, GestorCorporativo gestor, Long idEmpesa);

    void excluir (Long id);

    List<GestorCorporativo> listar();

    GestorCorporativo buscarPorId(Long id);

    Map<String, Object> acessarPainel(Long idUsuarioGestor);
}

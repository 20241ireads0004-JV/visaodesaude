package br.com.ifba.usuario.service;

import br.com.ifba.usuario.entity.Usuario;

import java.util.List;

public interface UsuarioIService {

    //Cadastrar
    Usuario cadastrar(Usuario usuario, Long idEmpresa);

    //Editar
    Usuario editar(Long id, Usuario usuario, Long idEmpresa);

    //Excluir
    void excluir(Long id);

    //Listar
    List<Usuario> listar();

    //Buscar
    Usuario buscarPorId(Long id);
}

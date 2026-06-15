package br.com.ifba.usuario.service;

import br.com.ifba.usuario.dto.VincularFuncionarioRequestDto;
import br.com.ifba.usuario.entity.Usuario;

import java.util.List;

public interface UsuarioIService {

    //Cadastrar
    Usuario cadastrar(Usuario usuario);

    Usuario autenticarLogin(String email, String senha);

    //Editar
    Usuario editar(Long id, Usuario usuario);

    //Excluir
    void excluir(Long id);

    //Listar
    List<Usuario> listar();

    //Buscar
    Usuario buscarPorId(Long id);

    //Vicular usuario a empresa
    void vincularPorCodigo(Long usuarioId, VincularFuncionarioRequestDto dto);
}

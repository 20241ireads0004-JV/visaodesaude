package br.com.ifba.empresa.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.painelcorporativo.dto.PainelCorporativoPostResponseDto;
import br.com.ifba.usuario.dto.VincularFuncionarioRequestDto;
import br.com.ifba.usuario.entity.Usuario;

import java.util.List;
import java.util.Map;

public interface EmpresaIService {

    Empresa cadastrarEmpresa(Empresa empresa, Long gestorId);

    Empresa editarEmpresa(Long id, Empresa empresa);

    void excluirEmpresa(Long id);

    List<Empresa> listar();

    Empresa buscarPorId(Long id);

    void vincularFuncionario(VincularFuncionarioRequestDto dto, Long usuarioId);

    PainelCorporativoPostResponseDto visualizarDados(Long gestorId, String departamento);

    Map<String, Object> buscarStatus(Long usuarioId);

    List<String> listarDepartamentos(Long gestorId);
}


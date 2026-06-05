package br.com.ifba.empresa.service;

import br.com.ifba.empresa.entity.Empresa;

import java.util.List;

public interface EmpresaIService {

    Empresa cadastrarEmpresa(Empresa empresa);

    Empresa editarEmpresa(Long id, Empresa empresa);
    void excluirEmpresa(Long id);

    List<Empresa> listar();

    Empresa buscarPorId(Long id);
}

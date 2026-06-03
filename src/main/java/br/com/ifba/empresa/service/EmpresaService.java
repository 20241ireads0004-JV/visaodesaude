package br.com.ifba.empresa.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.repository.EmpresaRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpresaService implements EmpresaIService{

    private final EmpresaRepository empresaRepository;

    @Override
    public Empresa cadastrarEmpresa(Empresa empresa) {

        //Verificação
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()){
            throw new BusinessException("O nome da Empresa é obrigatório");
        }

        //Guarda na base de dados
        return empresaRepository.save(empresa);
    }
}

package br.com.ifba.empresa.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.repository.EmpresaRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Empresa editarEmpresa(Long id, Empresa empresa) {

        Empresa existente = empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada. ID: " + id));

        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new BusinessException("O nome da Empresa é obrigatório");
        }

        existente.setNome(empresa.getNome());
        return empresaRepository.save(existente);
    }

    @Override
    public void excluirEmpresa(Long id) {

        if (!empresaRepository.existsById(id)) {
            throw new BusinessException("Empresa não encontrada. ID: " + id);
        }
        empresaRepository.deleteById(id);
    }

    @Override
    public List<Empresa> listar() {

        return empresaRepository.findAll();
    }

    @Override
    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada. ID: " + id));
    }
}

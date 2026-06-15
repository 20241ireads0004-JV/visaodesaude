package br.com.ifba.gestorcorporativo.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.repository.EmpresaRepository;
import br.com.ifba.gestorcorporativo.entity.GestorCorporativo;
import br.com.ifba.gestorcorporativo.repository.GestorCorporativoRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GestorCorporativoService implements GestorCorporativoIService{

    private final GestorCorporativoRepository gestorCorporativoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public GestorCorporativo cadastrar(GestorCorporativo gestor, Long idEmpresa) {
        //Validação de senha obrigatoria no cadastro
        if (gestor.getSenha() == null || gestor.getSenha().isBlank()){
            throw new BusinessException("A senha é obrigatória para efetuar o cadastro");
        }

        if (gestor.getSenha().length() < 6){
            throw new BusinessException("A senha deve ter no mínimo 6 caracteres");
        }

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada. ID: " + idEmpresa));

        if (usuarioRepository.existsByEmail(gestor.getEmail())) {
            throw new BusinessException("Email já cadastrado no sistema.");
        }

        if (gestorCorporativoRepository.existsByIdGestor(gestor.getIdGestor())) {
            throw new BusinessException("Este ID de Gestor já está em uso por outro profissional.");
        }

        gestor.setSenha(passwordEncoder.encode(gestor.getSenha()));
        gestor.setEmpresa(empresa);

        return gestorCorporativoRepository.save(gestor);

    }

    @Override
    @Transactional
    public GestorCorporativo editar(Long id, GestorCorporativo gestor, Long idEmpresa) {
        // 1. Busca o gestor existente
        GestorCorporativo gestorExistente = gestorCorporativoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Gestor não encontrado. ID: " + id));

        // 2. Valida se o novo e-mail já pertence a outrem
        if (!gestorExistente.getEmail().equals(gestor.getEmail())) {
            if (usuarioRepository.existsByEmail(gestor.getEmail())) {
                throw new BusinessException("Email já está em uso por outro utilizador.");
            }
        }

        // 3. Valida se o novo idGestor corporativo pertence a outro gestor
        if (!gestorExistente.getIdGestor().equals(gestor.getIdGestor())) {
            if (gestorCorporativoRepository.existsByIdGestorAndIdNot(gestor.getIdGestor(), id)) {
                throw new BusinessException("Este ID de Gestor já está atribuído a outro profissional.");
            }
        }

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada. ID: " + idEmpresa));

        // 4. Atualiza os dados básicos
        gestorExistente.setNome(gestor.getNome());
        gestorExistente.setEmail(gestor.getEmail());
        gestorExistente.setIdade(gestor.getIdade());
        gestorExistente.setSexoBiologico(gestor.getSexoBiologico());
        gestorExistente.setIdGestor(gestor.getIdGestor());
        gestorExistente.setEmpresa(empresa);

        // 5. Só atualiza a senha se ela foi preenchida (Regra do Usuário aplicada com sucesso)
        if (gestor.getSenha() != null && !gestor.getSenha().isBlank()) {
            if (gestor.getSenha().length() < 6) {
                throw new BusinessException("A nova senha deve ter no mínimo 6 caracteres.");
            }
            gestorExistente.setSenha(passwordEncoder.encode(gestor.getSenha()));
        }

        return gestorCorporativoRepository.save(gestorExistente);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!gestorCorporativoRepository.existsById(id)) {
            throw new BusinessException("Gestor não encontrado. ID: " + id);
        }

        gestorCorporativoRepository.deleteById(id);
    }

    @Override
    public List<GestorCorporativo> listar() {
        return gestorCorporativoRepository.findAll();
    }

    @Override
    public GestorCorporativo buscarPorId(Long id) {
        return gestorCorporativoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Gestor não encontrado. ID: " + id));
    }

    @Override
    public Map<String, Object> acessarPainel(Long idUsuarioGestor) {
        GestorCorporativo gestor = gestorCorporativoRepository.findById(idUsuarioGestor)
                .orElseThrow(() -> new BusinessException("Acesso Negado: Apenas gestores ou RH têm permissão para aceder aos painéis globais."));

        Map<String, Object> dadosPainel = new HashMap<>();
        dadosPainel.put("empresa", gestor.getEmpresa().getNome());
        dadosPainel.put("status", "Painel Global de Saúde Acedido com Sucesso");
        dadosPainel.put("totalFuncionariosMonitorizados", 27);
        dadosPainel.put("mediaHorasSonoEmpresa", "7.2 horas");

        return dadosPainel;
    }
}

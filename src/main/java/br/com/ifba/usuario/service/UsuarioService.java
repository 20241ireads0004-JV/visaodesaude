package br.com.ifba.usuario.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.repository.EmpresaRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.usuario.dto.VincularFuncionarioRequestDto;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService{

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario cadastrar(Usuario usuario) {

        // Verifica email
        if (usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new BusinessException("Email ja cadastrado no sistema");
        }

        // Criptografa senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuario.setEmpresa(null);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario autenticarLogin(String email, String senha) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null) {
            System.out.println("✅ USUÁRIO ENCONTRADO");

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (encoder.matches(senha, usuario.getSenha())) {
                System.out.println("Senha correta, Login aprovado.");
                return usuario;
            } else {
                System.out.println("Senha Icorreta");
            }
        } else {
            System.out.println("Email inexistente");
        }

        return null;
    }

    @Override
    @Transactional
    public Usuario editar(Long id, Usuario usuario) {

        // Busca o usuário existente
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario não encontrado. ID: " + id));

        // Verifica se o email novo já pertence a outro usuário
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new BusinessException("Email já está em uso");
            }
        }

        // Atualiza os campos
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());

        // Só atualiza a senha se vier preenchida
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioExistente.setIdade(usuario.getIdade());
        usuarioExistente.setSexoBiologico(usuario.getSexoBiologico());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)){
            throw new BusinessException("Usuario não encontrado. ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario não encontrado. ID: " + id));
    }

    @Override
    @Transactional
    public void vincularPorCodigo(Long usuarioId, VincularFuncionarioRequestDto dto) {

        if (dto.getCodigoEmpresa() == null || dto.getCodigoEmpresa().isBlank()) {
            throw new BusinessException("Código da empresa é obrigatório");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (usuario.getEmpresa() != null) {
            throw new BusinessException("Usuário já está vinculado a uma empresa");
        }

        Empresa empresa = empresaRepository.findByCodigoAcesso(dto.getCodigoEmpresa())
                .orElseThrow(() -> new BusinessException("Código da empresa inválido"));

        usuario.setEmpresa(empresa);
    }
}

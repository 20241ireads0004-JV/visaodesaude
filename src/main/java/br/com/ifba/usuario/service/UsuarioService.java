package br.com.ifba.usuario.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.repository.EmpresaRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService{

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario cadastrar(Usuario usuario, Long idEmpresa) {

        //Busca Empresa pelo Id
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada. ID " + idEmpresa));

        //Verfica se o email ja existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new BusinessException("Email ja cadastrado no sistema");
        }

        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        //Vincula Empresa a usuario
        usuario.setEmpresa(empresa);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario editar(Long id, Usuario usuario, Long idEmpresa) {

        // Busca o usuário existente
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario não encontrado. ID: " + id));

        // Verifica se o email novo já pertence a outro usuário
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new BusinessException("Email já está em uso");
            }
        }

        // Busca a empresa
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada. ID: " + idEmpresa));

        // Atualiza os campos
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());

        // Só atualiza a senha se vier preenchida
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioExistente.setIdade(usuario.getIdade());
        usuarioExistente.setSexo(usuario.getSexo());
        usuarioExistente.setEmpresa(empresa);
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
}

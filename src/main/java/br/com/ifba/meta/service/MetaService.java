package br.com.ifba.meta.service;

import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.meta.entity.Meta;
import br.com.ifba.meta.repository.MetaRepository;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService implements MetaIService{

    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Meta save(Long usuarioId, Meta meta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado. ID: " + usuarioId));

        meta.setUsuario(usuario);
        return metaRepository.save(meta);
    }

    @Override
    @Transactional
    public Meta update(Long id, Long usuarioId, Meta meta) {
        Meta metaExistente = findById(id);

        // Valida se a meta pertence ao usuário informado
        if (!metaExistente.getUsuario().getId().equals(usuarioId)) {
            throw new BusinessException("Acesso negado: A meta não pertence ao usuário informado.");
        }

        // Atualiza os campos
        metaExistente.setTitulo(meta.getTitulo());
        metaExistente.setCategoria(meta.getCategoria());
        metaExistente.setDataLimite(meta.getDataLimite());
        metaExistente.setStatus(meta.getStatus());

        return metaRepository.save(metaExistente);
    }

    @Override
    @Transactional
    public void delete(Long id, Long usuarioId) {
        Meta metaExistente = findById(id);

        // Valida se a meta pertence ao usuário informado
        if (!metaExistente.getUsuario().getId().equals(usuarioId)) {
            throw new BusinessException("Acesso negado: A meta não pertence ao usuário informado.");
        }

        metaRepository.delete(metaExistente);
    }

    @Override
    public List<Meta> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new BusinessException("Usuário não encontrado. ID: " + usuarioId);
        }
        return metaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Meta findById(Long id) {
        return metaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Meta não encontrada. ID: " + id));
    }
}

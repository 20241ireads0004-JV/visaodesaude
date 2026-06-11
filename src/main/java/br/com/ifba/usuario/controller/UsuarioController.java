package br.com.ifba.usuario.controller;

import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import br.com.ifba.usuario.dto.UsuarioGetResponseDto;
import br.com.ifba.usuario.dto.UsuarioPostRequestDto;
import br.com.ifba.usuario.dto.VincularFuncionarioRequestDto;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.service.UsuarioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioIService usuarioService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioGetResponseDto> cadastrar(@Valid @RequestBody UsuarioPostRequestDto requestDto){

        //Converte o DTO para entidade
        Usuario usuario = new Usuario();
        usuario.setNome(requestDto.getNome());
        usuario.setEmail(requestDto.getEmail());
        usuario.setSenha(requestDto.getSenha());
        usuario.setIdade(requestDto.getIdade());
        usuario.setSexo(requestDto.getSexo());

        //Passa a entidade e o id da Empresa pro Service
        Usuario salvo = usuarioService.cadastrar(usuario);

        // Converte Entidade → DTO de resposta
        UsuarioGetResponseDto responseDto = objectMapperUtil.map(salvo, UsuarioGetResponseDto.class);

        if (salvo.getEmpresa() != null) {
            responseDto.setNomeEmpresa(salvo.getEmpresa().getNome());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioGetResponseDto> editar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioPostRequestDto requestDto){

        Usuario usuario = new Usuario();
        usuario.setNome(requestDto.getNome());
        usuario.setEmail(requestDto.getEmail());
        usuario.setSenha(requestDto.getSenha());
        usuario.setIdade(requestDto.getIdade());
        usuario.setSexo(requestDto.getSexo());

        Usuario atualizado = usuarioService.editar(id, usuario);

        UsuarioGetResponseDto responseDto = objectMapperUtil.map(atualizado, UsuarioGetResponseDto.class);

        if (atualizado.getEmpresa() != null) {
            responseDto.setNomeEmpresa(atualizado.getEmpresa().getNome());
        }

        return ResponseEntity.ok(responseDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioGetResponseDto>> listar() {
        List<UsuarioGetResponseDto> lista = usuarioService.listar()
                .stream()
                .map(u -> {
                    UsuarioGetResponseDto dto = objectMapperUtil.map(u, UsuarioGetResponseDto.class);
                    if (u.getEmpresa() != null) {
                        dto.setNomeEmpresa(u.getEmpresa().getNome());
                    }else{
                        dto.setNomeEmpresa(null);
                    }

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioGetResponseDto> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        UsuarioGetResponseDto responseDto = objectMapperUtil.map(usuario, UsuarioGetResponseDto.class);

        if (usuario.getEmpresa() != null) {
            responseDto.setNomeEmpresa(usuario.getEmpresa().getNome());
        } else {
            responseDto.setNomeEmpresa(null);
        }

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{usuarioId}/vincular")
    public ResponseEntity<Void> vincularFuncionario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody VincularFuncionarioRequestDto dto) {

        usuarioService.vincularPorCodigo(usuarioId, dto);
        return ResponseEntity.noContent().build();
    }
}

package br.com.ifba.meta.controller;

import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import br.com.ifba.meta.dto.MetaGetResponseDto;
import br.com.ifba.meta.dto.MetaPostRequestDto;
import br.com.ifba.meta.entity.Meta;
import br.com.ifba.meta.service.MetaIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
@RequiredArgsConstructor
public class MetaController {

    private final MetaIService metaService;
    private final ObjectMapperUtil objectMapperUtil;

    // POST /metas/cadastrar?usuarioId=2 -> Cadastrar Meta
    @PostMapping("/cadastrar")
    public ResponseEntity<MetaGetResponseDto> save(
            @RequestParam Long usuarioId,
            @Valid @RequestBody MetaPostRequestDto requestDto) {

        Meta meta = objectMapperUtil.map(requestDto, Meta.class);
        Meta salva = metaService.save(usuarioId, meta);

        MetaGetResponseDto responseDto = objectMapperUtil.map(salva, MetaGetResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // PUT /metas/editar/{id}?usuarioId=2 -> Editar Meta
    @PutMapping("/editar/{id}")
    public ResponseEntity<MetaGetResponseDto> update(
            @PathVariable Long id,
            @RequestParam Long usuarioId,
            @Valid @RequestBody MetaPostRequestDto requestDto) {

        Meta meta = objectMapperUtil.map(requestDto, Meta.class);
        Meta atualizada = metaService.update(id, usuarioId, meta);

        MetaGetResponseDto responseDto = objectMapperUtil.map(atualizada, MetaGetResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    // DELETE /metas/excluir/{id}?usuarioId=2 -> Excluir Meta
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        metaService.delete(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // GET /metas/usuario/{usuarioId} -> Listar Metas por Usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MetaGetResponseDto>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<MetaGetResponseDto> lista = metaService.listarPorUsuario(usuarioId)
                .stream()
                .map(m -> objectMapperUtil.map(m, MetaGetResponseDto.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // GET /metas/{id} -> Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<MetaGetResponseDto> findById(@PathVariable Long id) {
        Meta meta = metaService.findById(id);
        MetaGetResponseDto responseDto = objectMapperUtil.map(meta, MetaGetResponseDto.class);

        return ResponseEntity.ok(responseDto);
    }
}
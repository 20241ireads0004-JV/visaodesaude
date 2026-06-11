package br.com.ifba.empresa.controller;

import br.com.ifba.empresa.dto.EmpresaGetResponseDto;
import br.com.ifba.empresa.dto.EmpresaPostRequestDto;
import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.service.EmpresaIService;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import br.com.ifba.painelcorporativo.dto.PainelCorporativoPostResponseDto;
import br.com.ifba.usuario.dto.VincularFuncionarioRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaIService empresaService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping("/cadastrar")
    public ResponseEntity<EmpresaGetResponseDto> cadastrar(@Valid @RequestBody EmpresaPostRequestDto requestDto, @RequestParam Long usuarioId) {

        // Converte DTO para Entidade
        Empresa empresa = objectMapperUtil.map(requestDto, Empresa.class);

        // Salva a empresa e gera o código
        Empresa empresaSalva = empresaService.cadastrarEmpresa(empresa, usuarioId);

        // Converte a Entidade salva de volta para o DTO de resposta
        EmpresaGetResponseDto responseDto = objectMapperUtil.map(empresaSalva, EmpresaGetResponseDto.class);

        // Seta os valores exatos que você quer retornar no Postman
        responseDto.setNome(empresaSalva.getNome());
        responseDto.setCodigoAcesso(empresaSalva.getCodigoAcesso());
        responseDto.setGestor(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaGetResponseDto> editar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaPostRequestDto requestDto) {

        Empresa empresa = objectMapperUtil.map(requestDto, Empresa.class);
        Empresa atualizada = empresaService.editarEmpresa(id, empresa);
        EmpresaGetResponseDto responseDto = objectMapperUtil.map(atualizada, EmpresaGetResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        empresaService.excluirEmpresa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmpresaGetResponseDto>> listar() {

        List<EmpresaGetResponseDto> lista = empresaService.listar()
                .stream()
                .map(e -> objectMapperUtil.map(e, EmpresaGetResponseDto.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaGetResponseDto> buscarPorId(@PathVariable Long id) {

        Empresa empresa = empresaService.buscarPorId(id);
        EmpresaGetResponseDto responseDto = objectMapperUtil.map(empresa, EmpresaGetResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    //  POST /empresa/vincular?usuarioId=2
    //  Aba "Sou Funcionário (Vincular)"
    //  Body: { codigoEmpresa, departamento }

    @PostMapping(
            path = "/vincular",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> vincularFuncionario(
            @RequestBody VincularFuncionarioRequestDto dto,
            @RequestParam Long usuarioId
    ) {
        try {
            empresaService.vincularFuncionario(dto, usuarioId);
            return ResponseEntity.ok(Map.of("mensagem", "Vinculado com sucesso!"));
        } catch (BusinessException | IllegalStateException | java.util.NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    //  GET /empresa/painel?usuarioId=1
    //  Rota do Painel Corporativo — restrita ao Gestor
    //  Query param opcional: &departamento=TI

    @GetMapping(
            path = "/painel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> visualizarPainel(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) String departamento
    ) {
        try {
            PainelCorporativoPostResponseDto painel = empresaService.visualizarDados(usuarioId, departamento);
            return ResponseEntity.ok(painel);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("erro", e.getMessage()));
        }
    }

    // GET /empresa/status?usuarioId=1
    // Frontend usa para saber se exibe "Painel Admin" no menu
    // Resposta varia conforme o papel do usuário:
    // Gestor      → { isGestor: true, codigoAcesso, nomeEmpresa }
    // Funcionário → { isGestor: false, vinculado: true, nomeEmpresa, departamento }
    // Sem vínculo → { isGestor: false, vinculado: false }

    @GetMapping(
            path = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> buscarStatus(@RequestParam Long usuarioId) {
        try {
            Map<String, Object> status = empresaService.buscarStatus(usuarioId);
            return ResponseEntity.ok(status);
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }
    // GET /empresa/departamentos?usuarioId=1
    //  Lista os departamentos da empresa para preencher o filtro

    @GetMapping(
            path = "/departamentos",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listarDepartamentos(@RequestParam Long usuarioId) {
        try {
            return ResponseEntity.ok(empresaService.listarDepartamentos(usuarioId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("erro", e.getMessage()));
        }
    }


}

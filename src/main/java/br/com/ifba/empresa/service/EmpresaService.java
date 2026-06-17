package br.com.ifba.empresa.service;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.repository.EmpresaRepository;
import br.com.ifba.habito.repository.HabitoRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.painelcorporativo.dto.PainelCorporativoPostResponseDto;
import br.com.ifba.usuario.dto.VincularFuncionarioRequestDto;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmpresaService implements EmpresaIService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitoRepository habitoRepository;
    private static final int LGPD_MIN_SEGMENT = 10;

    @Override
    @Transactional
    public Empresa cadastrarEmpresa(Empresa empresa, Long gestorId) {

        Usuario usuario = usuarioRepository.findById(gestorId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        //Verificação
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new BusinessException("O nome da Empresa é obrigatório");
        }

        if (empresaRepository.findByGestorId(gestorId).isPresent()) {
            throw new BusinessException("Você já possui uma empresa cadastrada.");
        }
        if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
            throw new BusinessException("CNPJ já cadastrado no sistema.");
        } else {
            empresa.setGestorId(gestorId);
            empresa.setCodigoAcesso(gerarCodigoUnico());
            usuario.setGestor(true);
            usuarioRepository.save(usuario);
            return empresaRepository.save(empresa);
        }
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


    //Metodo vincular funcionario em uma empresa
    @Transactional
    @Override
    public void vincularFuncionario(VincularFuncionarioRequestDto dto, Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        if (usuario.getEmpresa() != null) {
            throw new BusinessException("Você já está vinculado a uma empresa.");
        }

        Empresa empresa = empresaRepository.findByCodigoAcesso(dto.getCodigoEmpresa())
                .orElseThrow(() -> new NoSuchElementException("Código de empresa inválido ou não encontrado."));

        if (empresa.getGestorId().equals(usuarioId)) {
            throw new IllegalStateException("O gestor não pode se vincular como funcionário da própria empresa.");
        }

        usuario.setEmpresa(empresa);
        usuario.setDepartamento(dto.getDepartamento());
        usuarioRepository.save(usuario);
    }


    //  Agrega métricas de todos os funcionários (ANONIMIZADO / LGPD)
    //  Acesso RESTRITO ao Gestor — verificado no início do metodo
    @Override
    public PainelCorporativoPostResponseDto visualizarDados(Long gestorId, String departamento) {

        // Apenas o Gestor (quem tem empresa cadastrada) pode acessar
        Empresa empresa = empresaRepository.findByGestorId(gestorId)
                .orElseThrow(() -> new SecurityException("Acesso negado: você não é gestor de nenhuma empresa."));

        boolean filtrando = departamento != null && !departamento.isBlank()
                && !"Todos Departamentos".equals(departamento);

        List<Usuario> funcionarios = filtrando
                ? usuarioRepository.findByEmpresaIdAndDepartamento(empresa.getId(), departamento)
                : usuarioRepository.findByEmpresaId(empresa.getId());

        long total = funcionarios.size();

        // Monta o DTO raiz
        PainelCorporativoPostResponseDto painel = new PainelCorporativoPostResponseDto();
        painel.setIdPainel(empresa.getId());

        PainelCorporativoPostResponseDto.DadosAgregados dados = new PainelCorporativoPostResponseDto.DadosAgregados();
        dados.setTotalColaboradores(total);
        dados.setDepartamentoFiltrado(filtrando ? departamento : null);

        // Sem funcionários → retorna painel zerado
        if (total == 0) {
            dados.setPercentualAdesao(0.0);
            dados.setScoreMedio(0.0);
            dados.setMediaSono(0.0);
            dados.setReducaoRiscoProjetado(0.0);
            dados.setEvolucaoSono(List.of());
            dados.setRiscoPorDepartamento(List.of());
            painel.setDadosAgregados(dados);
            return painel;
        }

        List<Long> ids = funcionarios.stream().map(Usuario::getId).toList();

        // Métricas agregadas via HabitoRepository
        double mediaSono = Optional.ofNullable(habitoRepository.calcularMediaSono(ids)).orElse(0.0);
        double mediaQualid = Optional.ofNullable(habitoRepository.calcularMediaQualidade(ids)).orElse(0.0);
        long comRegistro = Optional.ofNullable(habitoRepository.contarComRegistro(ids)).orElse(0L);

        // Score: 60% peso no sono (ideal = 8h), 40% na qualidade do sono (escala 1–5)
        double scoreMedio = (Math.min(mediaSono / 8.0, 1.0) * 60) + ((mediaQualid / 5.0) * 40);

        dados.setMediaSono(arredondar(mediaSono, 1));
        dados.setScoreMedio(arredondar(scoreMedio, 0));
        dados.setPercentualAdesao(arredondar(comRegistro * 100.0 / total, 1));
        dados.setReducaoRiscoProjetado(calcularReducaoRisco(mediaSono));

        // Gráfico de linha: evolução mensal do sono
        dados.setEvolucaoSono(montarEvolucaoSono(ids));

        // Gráfico de barras: risco por departamento (com anonimização LGPD)
        dados.setRiscoPorDepartamento(montarRiscoPorDepartamento(empresa.getId()));

        painel.setDadosAgregados(dados);
        return painel;
    }


    //  O frontend chama este endpoint para saber:
    //  - Se é gestor → exibe "Painel Admin" no menu
    //  - Se é funcionário vinculado → mostra a empresa
    //  - Se não tem vínculo → mostra a tela de vincular
    @Override
    public Map<String, Object> buscarStatus(Long usuarioId) {

        // Verifica se é gestor
        Optional<Empresa> comoGestor = empresaRepository.findByGestorId(usuarioId);
        if (comoGestor.isPresent()) {
            Empresa e = comoGestor.get();
            return Map.of(
                    "isGestor", true,
                    "nomeEmpresa", e.getNome(),
                    "codigoAcesso", e.getCodigoAcesso()
            );
        }

        // Verifica se está vinculado como funcionário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        if (usuario.getEmpresa() != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("isGestor", false);
            res.put("vinculado", true);
            res.put("nomeEmpresa", usuario.getEmpresa().getNome());
            res.put("departamento", usuario.getDepartamento() != null ? usuario.getDepartamento() : "");
            return res;
        }

        return Map.of("isGestor", false, "vinculado", false);
    }

    @Override
    public List<String> listarDepartamentos(Long gestorId) {
        Empresa empresa = empresaRepository.findByGestorId(gestorId)
                .orElseThrow(() -> new SecurityException("Acesso negado."));

        return usuarioRepository.findByEmpresaId(empresa.getId())
                .stream()
                .map(Usuario::getDepartamento)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
    }


    //  Métodos auxiliares privados

    private List<PainelCorporativoPostResponseDto.DadosAgregados.EvolucaoSonoMensalDTO> montarEvolucaoSono(List<Long> ids) {
        List<Object[]> raw = habitoRepository.calcularEvolucaoMensal(ids);
        String[] nomesMeses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        List<PainelCorporativoPostResponseDto.DadosAgregados.EvolucaoSonoMensalDTO> lista = new ArrayList<>();
        for (Object[] row : raw) {
            int mes = ((Number) row[0]).intValue();
            double media = ((Number) row[1]).doubleValue();
            lista.add(new PainelCorporativoPostResponseDto.DadosAgregados.EvolucaoSonoMensalDTO(nomesMeses[mes - 1], arredondar(media, 1)));
        }
        return lista;
    }

    private List<PainelCorporativoPostResponseDto.DadosAgregados.RiscoPorDepartamentoDTO> montarRiscoPorDepartamento(Long empresaId) {
        List<Object[]> raw = habitoRepository.calcularRiscoPorDepartamento(empresaId);

        List<PainelCorporativoPostResponseDto.DadosAgregados.RiscoPorDepartamentoDTO> resultado = new ArrayList<>();
        long outrosAlto = 0, outrosMedio = 0, outrosBaixo = 0;
        boolean temOutros = false;

        for (Object[] row : raw) {
            String depto = (String) row[0];
            long alto = ((Number) row[1]).longValue();
            long medio = ((Number) row[2]).longValue();
            long baixo = ((Number) row[3]).longValue();
            long total = alto + medio + baixo;

            // LGPD: departamentos com menos de 10 pessoas são agrupados
            if (total < LGPD_MIN_SEGMENT) {
                outrosAlto += alto;
                outrosMedio += medio;
                outrosBaixo += baixo;
                temOutros = true;
                continue;
            }
            resultado.add(new PainelCorporativoPostResponseDto.DadosAgregados.RiscoPorDepartamentoDTO(depto, baixo, medio, alto));
        }

        if (temOutros) {
            resultado.add(new PainelCorporativoPostResponseDto.DadosAgregados.RiscoPorDepartamentoDTO("Outros", outrosBaixo, outrosMedio, outrosAlto));
        }
        return resultado;
    }

    /**
     * Estimativa de redução de risco baseada na média de sono da equipe
     */
    private double calcularReducaoRisco(double mediaSono) {
        if (mediaSono >= 8) return 20.0;
        if (mediaSono >= 7) return 15.0;
        if (mediaSono >= 6) return 10.0;
        return 5.0;
    }

    private double arredondar(double valor, int casas) {
        double fator = Math.pow(10, casas);
        return Math.round(valor * fator) / fator;
    }

    /**
     * Gera código único no formato EMP-XXXXXXXX (sem letras ambíguas O, I, 0, 1)
     */
    private String gerarCodigoUnico() {
        String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random random = new Random();
        String codigo;
        do {
            StringBuilder sb = new StringBuilder("EMP-");
            for (int i = 0; i < 8; i++) {
                sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            codigo = sb.toString();
        } while (empresaRepository.findByCodigoAcesso(codigo).isPresent());
        return codigo;
    }

}

package humcare.higienizacao.controller;

import humcare.application.model.Usuario;
import humcare.application.service.Sessao;
import humcare.dao.DAO;
import humcare.higienizacao.dao.HigienizacaoDAO;
import humcare.higienizacao.model.Higienizacao;
import humcare.higienizacao.tipoHigienizacao.TipoHigienizacao;
import humcare.leitos.dao.LeitoDAO;
import humcare.leitos.model.Leitos;
import humcare.leitos.status.StatusLeito;
import humcare.usuario.dao.CehUsuarioDAO;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.usuario.model.CehUsuario;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.CustomMessagebox;
import humcare.zk.custom.Toast;
import org.hibernate.Session;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class HigienizacaoController extends Window {

    // --- Componentes da Tela ---
    private Window win;
    private Label idLeitoLabel;
    private Label statusLeitoLabel;
    private Button btnIniciar, btnFinalizar, btnAdmitir, btnDesativar, btnAtivar;
    private Listbox historicoListbox;

    // --- Componentes do Modal ---
    private Window modalIniciarHigienizacao;
    private Listbox tpHigienizacaoModal;
    private Button btnConfirmarInicio, btnCancelarInicio;

    // --- DAOs e Utilitários ---
    private final LeitoDAO leitoDAO = new LeitoDAO();
    private final HigienizacaoDAO higienizacaoDAO = new HigienizacaoDAO();
    private final CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();
    private final ZkUtils zkUtils = new ZkUtils();

    // --- Variáveis de Estado ---
    private Leitos leito;
    private String urlRetorno;
    private Usuario usuarioLogado;
    private boolean isStandalonePage = false;

    public void onCreate() {
        // 1. Mapear componentes principais
        win = (Window) getFellow("winHigienizacao");
        idLeitoLabel = (Label) getFellow("idLeito");
        statusLeitoLabel = (Label) getFellow("statusLeito");
        btnIniciar = (Button) getFellow("btnIniciar");
        btnFinalizar = (Button) getFellow("btnFinalizar");
        btnAdmitir = (Button) getFellow("btnAdmitir");
        btnDesativar = (Button) getFellow("btnDesativar");
        btnAtivar = (Button) getFellow("btnAtivar");
        historicoListbox = (Listbox) getFellow("historicoListbox");

        this.isStandalonePage = !(win.getParent() instanceof Include);

        // 2. Mapear componentes do modal
        modalIniciarHigienizacao = (Window) getFellow("modalIniciarHigienizacao");
        tpHigienizacaoModal = (Listbox) modalIniciarHigienizacao.getFellow("tpHigienizacaoModal");
        btnConfirmarInicio = (Button) modalIniciarHigienizacao.getFellow("btnConfirmarInicio");
        btnCancelarInicio = (Button) modalIniciarHigienizacao.getFellow("btnCancelarInicio");

        modalIniciarHigienizacao.setVisible(false);

        // 3. Adicionar Listeners
        btnIniciar.addEventListener(Events.ON_CLICK, event -> abrirModalIniciarHigienizacao());
        btnFinalizar.addEventListener(Events.ON_CLICK, event -> finalizarHigienizacao());
        btnDesativar.addEventListener(Events.ON_CLICK, event -> desativarLeito());
        btnAtivar.addEventListener(Events.ON_CLICK, event -> ativarLeito());
        btnConfirmarInicio.addEventListener(Events.ON_CLICK, event -> confirmarInicioHigienizacao());
        btnCancelarInicio.addEventListener(Events.ON_CLICK, event -> modalIniciarHigienizacao.setVisible(false));

        // 4. Carregar Dados e VERIFICAR SESSÃO
        this.usuarioLogado = Sessao.getInstance().getUsuario();
        if (usuarioLogado == null) {
            zkUtils.MensagemErro("Sua sessão expirou. Por favor, faça o login novamente.");
            Executions.sendRedirect("/login.zul");
            return;
        }

        // Tenta obter o cdLeito da URL primeiro, depois dos parâmetros da sessão
        Integer cdLeito = null;
        String cdLeitoParam = Executions.getCurrent().getParameter("cdLeito");
        if (cdLeitoParam != null && !cdLeitoParam.isEmpty()) {
            try {
                cdLeito = Integer.parseInt(cdLeitoParam);
            } catch (NumberFormatException e) {
                zkUtils.MensagemErro("ID do leito inválido na URL.");
                voltar();
                return;
            }
        } else {
            cdLeito = (Integer) zkUtils.getParametro("cdLeito");
        }

        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");

        if (cdLeito == null) {
            zkUtils.MensagemErro("ID do leito não fornecido.");
            voltar();
            return;
        }

        this.leito = leitoDAO.buscar(cdLeito);
        if (this.leito == null) {
            zkUtils.MensagemErro("Leito não encontrado.");
            voltar();
            return;
        }

        // 5. Configurar a tela
        popularTiposHigienizacaoModal();
        configurarTelaPorEstado();
        carregarHistorico();
    }

    public boolean isStandalonePage() {
        return isStandalonePage;
    }

    private void configurarTelaPorEstado() {
        this.leito = leitoDAO.buscar(leito.getCdLeito());

        idLeitoLabel.setValue(leito.getIdLeito());
        statusLeitoLabel.setValue(leito.getStLeito() != null ? leito.getStLeito().getDescricao() : "N/A");

        btnIniciar.setVisible(false);
        btnFinalizar.setVisible(false);
        btnAdmitir.setVisible(false);
        btnDesativar.setVisible(false);
        btnAtivar.setVisible(false);

        boolean isNIR = usuarioLogado.getTpPermissaoAcesso() == FuncaoCdPerfil.NIR;
        boolean isAdmin = usuarioLogado.getTpPermissaoAcesso() == FuncaoCdPerfil.ADMINISTRADOR;

        if (leito.getStLeito() != null) {
            switch (leito.getStLeito()) {
                case LIVRE:
                    if (isNIR || isAdmin) btnIniciar.setVisible(true);
                    if (isNIR || isAdmin) btnDesativar.setVisible(true);
                    break;
                case HIGIENIZANDO:
                    if (isNIR || isAdmin) btnFinalizar.setVisible(true);
                    break;
                case OCUPADO:
                    if (isNIR || isAdmin) btnIniciar.setVisible(true);
                    if (isNIR || isAdmin) btnDesativar.setVisible(true);
                    break;
                case DESATIVADO:
                    if (isNIR || isAdmin) btnAtivar.setVisible(true);
                    break;
            }
        }
    }

    private void abrirModalIniciarHigienizacao() {
        if (tpHigienizacaoModal.getItemCount() > 0) {
            tpHigienizacaoModal.setSelectedIndex(0);
        }
        modalIniciarHigienizacao.setVisible(true);
        modalIniciarHigienizacao.doModal();
    }

    private void confirmarInicioHigienizacao() {
        TipoHigienizacao tipo = tpHigienizacaoModal.getSelectedItem().getValue();
        if (tipo == null) {
            zkUtils.MensagemErro("Selecione um tipo de higienização.");
            return;
        }

        Session session = null;
        try {
            session = DAO.openSession();
            DAO.beginTransaction(session);

            Higienizacao novaHigienizacao = new Higienizacao();
            novaHigienizacao.setCdLeito(leito.getCdLeito());
            novaHigienizacao.setTpHigienizacao(tipo);
            novaHigienizacao.setDhSolicitacao(new Timestamp(new Date().getTime()));
            novaHigienizacao.setCdUsuarioSolicitacao(usuarioLogado.getCdUsuario());
            novaHigienizacao.setDhInicio(new Timestamp(new Date().getTime()));
            novaHigienizacao.setCdUsuarioHigienizacao(usuarioLogado.getCdUsuario());
            novaHigienizacao.setFlAtivo(true);
            higienizacaoDAO.incluirAutoincrementando(session, novaHigienizacao);

            leito.setStLeito(StatusLeito.HIGIENIZANDO);
            leitoDAO.atualizar(session, leito);

            DAO.commit(session);

            modalIniciarHigienizacao.setVisible(false);
            configurarTelaPorEstado();
            carregarHistorico();
            Toast.show("Higienização iniciada com sucesso!", "Sucesso", Toast.Type.SUCCESS);

        } catch (Exception e) {
            DAO.rollback(session);
            zkUtils.MensagemErro("Erro ao iniciar a higienização.");
            e.printStackTrace();
        } finally {
            DAO.closeSession(session);
        }
    }

    private void finalizarHigienizacao() {
        CustomMessagebox.show("Deseja realmente finalizar a higienização deste leito?", "Confirmar",
                CustomMessagebox.YES | CustomMessagebox.NO, CustomMessagebox.QUESTION, true,
                event -> {
                    if (event.getName().equals(Messagebox.ON_YES)) {
                        Session session = null;
                        try {
                            session = DAO.openSession();
                            DAO.beginTransaction(session);

                            Higienizacao higienizacaoAtiva = higienizacaoDAO.buscarHigienizacaoAtivaPorLeito(session, leito.getCdLeito());

                            if (higienizacaoAtiva != null) {
                                higienizacaoAtiva.setDhFim(new Timestamp(new Date().getTime()));
                                higienizacaoAtiva.setFlAtivo(false);
                                higienizacaoDAO.atualizar(session, higienizacaoAtiva);
                            }

                            leito.setStLeito(StatusLeito.LIVRE);
                            leitoDAO.atualizar(session, leito);

                            DAO.commit(session);

                            configurarTelaPorEstado();
                            carregarHistorico();
                            Toast.show("Higienização finalizada! O leito está Livre.", "Sucesso", Toast.Type.SUCCESS);

                        } catch (Exception e) {
                            DAO.rollback(session);
                            zkUtils.MensagemErro("Erro ao finalizar a higienização.");
                            e.printStackTrace();
                        } finally {
                            DAO.closeSession(session);
                        }
                    }
                });
    }

    private void desativarLeito() {
        CustomMessagebox.show("Deseja realmente colocar este equipamento em manutenção?", "Confirmar Manutenção",
                CustomMessagebox.YES | CustomMessagebox.NO, CustomMessagebox.QUESTION, true,
                event -> {
                    if (event.getName().equals(Messagebox.ON_YES)) {
                        leito.setStLeito(StatusLeito.DESATIVADO);
                        if (leitoDAO.atualizar(leito)) {
                            configurarTelaPorEstado();
                            Toast.show("Leito desativado para manutenção.", "Sucesso", Toast.Type.SUCCESS);
                        } else {
                            zkUtils.MensagemErro("Erro ao desativar o leito.");
                        }
                    }
                });
    }

    private void ativarLeito() {
        CustomMessagebox.show("Deseja reativar este leito?", "Confirmar Ativação",
                CustomMessagebox.YES | CustomMessagebox.NO, CustomMessagebox.QUESTION, true,
                event -> {
                    if (event.getName().equals(Messagebox.ON_YES)) {
                        leito.setStLeito(StatusLeito.LIVRE);
                        if (leitoDAO.atualizar(leito)) {
                            configurarTelaPorEstado();
                            Toast.show("Leito ativado e definido como Livre.", "Sucesso", Toast.Type.SUCCESS);
                        } else {
                            zkUtils.MensagemErro("Erro ao ativar o leito.");
                        }
                    }
                });
    }

    private void carregarHistorico() {
        List<Higienizacao> historico = higienizacaoDAO.listar("t.cdLeito = " + leito.getCdLeito(), "order by t.dhInicio desc");
        if (historico != null) {
            for (Higienizacao h : historico) {
                if (h.getCdUsuarioSolicitacao() != null) {
                    CehUsuario solicitante = cehUsuarioDAO.buscar(h.getCdUsuarioSolicitacao());
                    if (solicitante != null) {
                        h.setNmUsuarioSolicitacao(solicitante.getNmPessoa());
                    }
                }
                if (h.getCdUsuarioHigienizacao() != null) {
                    CehUsuario responsavel = cehUsuarioDAO.buscar(h.getCdUsuarioHigienizacao());
                    if (responsavel != null) {
                        h.setNmUsuarioHigienizacao(responsavel.getNmPessoa());
                    }
                }
            }
        }
        historicoListbox.setModel(new ListModelList<>(historico));
    }

    private void popularTiposHigienizacaoModal() {
        tpHigienizacaoModal.getItems().clear();
        for (TipoHigienizacao tipo : TipoHigienizacao.values()) {
            Listitem item = new Listitem(tipo.toString(), tipo);
            tpHigienizacaoModal.appendChild(item);
        }
    }

    public void voltar() {
        if (isStandalonePage) {
            Executions.sendRedirect("/paginas/higienizacao/higienizacaoList.zul");
        } else {
            Include include = (Include) win.getParent();
            include.setSrc(urlRetorno);
        }
    }
}

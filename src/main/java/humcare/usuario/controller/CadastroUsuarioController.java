package humcare.usuario.controller;

import humcare.usuario.dao.CehUsuarioDAO;
import humcare.usuario.funcao.FuncaoCdPerfil;
import humcare.usuario.model.CehUsuario;
import humcare.utilitarios.LdapUtil;
import humcare.utilitarios.PasswordUtil;
import humcare.utilitarios.Utils;
import humcare.utilitarios.ZkUtils;
import humcare.zk.custom.Toast;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.text.ParseException;

public class CadastroUsuarioController extends Window {

    private Textbox nmPessoa;
    private Textbox nuCpf;
    private Textbox nuFone;
    private Textbox deEmail;
    private Textbox deSenha;

    private final ZkUtils zkUtils = new ZkUtils();
    private final Utils utils = new Utils();
    private final CehUsuarioDAO cehUsuarioDAO = new CehUsuarioDAO();

    public void onCreate() {
        this.nmPessoa = (Textbox) getFellow("nmPessoa");
        this.nuCpf = (Textbox) getFellow("nuCpf");
        this.nuFone = (Textbox) getFellow("nuFone");
        this.deEmail = (Textbox) getFellow("deEmail");
        this.deSenha = (Textbox) getFellow("deSenha");
    }

    public void gravar() throws ParseException {
        if(!validarCampos()){
            return;
        }
        if (deSenha.getValue() == null || deSenha.getValue().trim().isEmpty()) {
            LdapUtil ldapUtil = new LdapUtil();
            if (!ldapUtil.emailExists(deEmail.getValue())) {
                Clients.showNotification("Senha é obrigatória para usuários não cadastrados no LDAP!", Clients.NOTIFICATION_TYPE_WARNING, deSenha, "end_center", 3000);
                return;
            }
        }


        CehUsuario cehUsuario = new CehUsuario();
        cehUsuario.setNmPessoa(nmPessoa.getValue());
        cehUsuario.setNuCpf(utils.soNumeros(this.nuCpf.getValue()));
        cehUsuario.setNuFone(utils.soNumeros(this.nuFone.getValue()));
        cehUsuario.setDeEmail(deEmail.getValue());
        if (deSenha.getValue() != null && !deSenha.getValue().trim().isEmpty()) {
            cehUsuario.setDeSenha(PasswordUtil.hashPassword(deSenha.getValue()));
        }
        //mudar...
        cehUsuario.setDePerfil(FuncaoCdPerfil.NIR);
        cehUsuario.setFlAtivo(false);

        Integer id = cehUsuarioDAO.incluirAutoincrementando(cehUsuario);
        if(id != null && id > 0){
            Toast.show("Usuário cadastrado com sucesso! Você será redirecionado para a tela de login.", "Sucesso", Toast.Type.SUCCESS);
            voltar();
        }else{
            zkUtils.MensagemErro("Houve um erro e não foi possível incluir o usuário.");
        }

    }

    public void voltar() {
        Executions.sendRedirect("/login.zul");
    }

    private boolean isCampoVazio(Textbox textbox, String nomeCampo) {
        if (textbox.getValue() == null || textbox.getValue().trim().isEmpty()) {
            Clients.showNotification(nomeCampo + " é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, textbox, "end_center", 2000);
            textbox.setFocus(true);
            return true;
        }
        return false;
    }

    public boolean validarCampos() throws ParseException {
        boolean gerouErro = false;

        if (this.nuFone.getValue().trim().isEmpty()) {
            Clients.showNotification("Telefone é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, nuFone, "end_center", 2000);
            gerouErro = true;
            return false;
        }
        if (this.nuCpf.getValue().trim().isEmpty()) {
            Clients.showNotification("CPF é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, nuCpf, "end_center", 2000);
            gerouErro = true;
            this.nuCpf.setFocus(true);
        }
        if (this.deEmail.getValue().trim().isEmpty()) {
            Clients.showNotification("E-mail é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, deEmail, "end_center", 2000);
            gerouErro = true;
            this.deEmail.setFocus(true);
        }
        if (nmPessoa.getValue().trim().isEmpty() || nuCpf.getValue() == null) {
            Clients.showNotification("Nome é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, nmPessoa, "end_center", 3000);
            gerouErro = true;
            this.nmPessoa.setFocus(true);
        }

        return !gerouErro;
    }
}
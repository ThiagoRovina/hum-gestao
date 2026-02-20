package humcare.menu.controller;

import humcare.application.model.Usuario;
import humcare.application.service.Sessao;
import humcare.utilitarios.ZkUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class MenuController extends Window {

    private Window winMenu;
    private Include conteudo;
    private Label lbNomeUsuario;
    private Div divLogout;
//    private Div menuUsuario;
    Usuario usuario = new Usuario();

    public void onCreate() {
        Sessao.getInstance().validarSessao();
        this.usuario = Sessao.getInstance().getUsuario();

        this.divLogout = (Div) getFellow("divLogout");
//        this.menuUsuario = (Div) getFellow("menuUsuario");
        this.lbNomeUsuario = (Label) getFellow("lbNomeUsuario");

        if (this.usuario != null) {
            this.divLogout.setVisible(true);
            String[] splitNome = this.usuario.getNome().split(" ");
            this.lbNomeUsuario.setValue(ZkUtils.capitalize(splitNome[0]) + " " + (splitNome.length > 1 ? ZkUtils.capitalize(splitNome[splitNome.length - 1]) : ""));

//            if (this.usuario.getUsuarioBd().getLtProfissao().equals("Administrador")) {
//                this.menuUsuario.setVisible(true);
//            }
        }

        this.winMenu = (Window) getFellow("winMenu");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);

    }

    public void logout() {
        Sessao.getInstance().destruirSessao();
        Executions.sendRedirect("/login");
    }

    public void abrirHome() {
        Executions.sendRedirect("/");
    }
    
//    public void abrirLogin() {
//        Executions.sendRedirect("/login");
//    }
//
//    public void abrirComponentes() {
//        this.conteudo.setSrc("/paginas/zkbootstrap.zul");
//    }
//
//    public void abrirCurso() {
//        this.conteudo.setSrc("/cadastros/curso/cursoList.zul");
//    }
//
//    public void abrirLeitos() {
//        this.conteudo.setSrc("/cadastros/leito/leitoList.zul");
//    }
//
//    public void abrirMedicacao() {
//        this.conteudo.setSrc("/cadastros/medicamento/medicamentoList.zul");
//    }
//
//    public void abrirDrogas() {
//        this.conteudo.setSrc("/cadastros/droga/drogaList.zul");
//    }
//
//    public void abrirDreno() {
//        this.conteudo.setSrc("/cadastros/dreno/drenoList.zul");
//    }
//
//    public void abrirDiurese() {
//        this.conteudo.setSrc("/cadastros/diurese/diureseList.zul");
//    }
//
//    public void abrirSondas() {
//        this.conteudo.setSrc("/cadastros/sonda/sondaList.zul");
//    }
//
//    public void abrirRiscos() {
//        this.conteudo.setSrc("/cadastros/risco/riscoList.zul");
//    }
//
//    public void abrirCidades() {
//        this.conteudo.setSrc("/cadastros/cidade/cidadeList.zul");
//    }
//
//    public void abrirUsuarios() {
//        this.conteudo.setSrc("/cadastros/usuario/usuarioList.zul");
//    }
//    public void abrirPacientes() {
//        this.conteudo.setSrc("/cadastros/paciente/pacienteList.zul");
//    }
//
//    public void abrirInscricao() {
//        this.conteudo.setSrc("/cadastros/inscricao/inscricaoList.zul");
//    }
//
//    public void abrirEndereco() {
//        this.conteudo.setSrc("/exemplos/endereco/endereco.zul");
//    }
//
//    public void abrirAnexos() {
//        this.conteudo.setSrc("/exemplos/anexo/anexo.zul");
//    }
//
//    public void abrirThread() {
//        this.conteudo.setSrc("/exemplos/thread/thread.zul");
//    }
//
//    public void abrirEmail() {
//        this.conteudo.setSrc("/exemplos/email/email.zul");
//    }
//
//    public void abrirGraficos() {
//        this.conteudo.setSrc("/exemplos/graficos/graficos.zul");
//    }
//
//    public void abrirServerpush() {
//        this.conteudo.setSrc("/exemplos/serverpush/serverpush.zul");
//    }
//
//    public void abrirWebcam() {
//        this.conteudo.setSrc("/exemplos/webcam/webcam.zul");
//    }
//
//    public void abrirQrcode() {
//        this.conteudo.setSrc("/exemplos/qrcode/qrcode.zul");
//    }
//
//    public void abrirMascara() {
//        this.conteudo.setSrc("/exemplos/mascara/mascara.zul");
//    }
//
//    public void abrirCarrossel() {
//        this.conteudo.setSrc("/exemplos/carrossel/carrossel.zul");
//    }
//
//    public void abrirRest() {
//        this.conteudo.setSrc("/exemplos/rest/rest.zul");
//    }
//
//    public void abrirCaptcha() {
//        this.conteudo.setSrc("/exemplos/captcha/captcha.zul");
//    }
//
//    public void abrirModal() {
//        this.conteudo.setSrc("/exemplos/modal/modalIndex.zul");
//    }
//
//    public void abrirCursoRelatorio() {
//        this.conteudo.setSrc("/exemplos/relatorio/relatorioCurso.zul");
//    }
//
//    public void abrirRelatorios() {
//        this.conteudo.setSrc("/paginas/relatorios/relatoriosList.zul");
//    }
}
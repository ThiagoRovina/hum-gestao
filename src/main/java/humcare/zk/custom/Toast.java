/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package humcare.zk.custom;

import org.zkoss.zk.ui.util.Clients;

/**
 * Notificação no formato Toast
 * 
 * @author alison
 *
 * Arquivos: js/toastr.jr e css/toastr.css
 *
 * Projeto: https://github.com/CodeSeven/toastr
 *
 */
public class Toast {

    /**
     * Tipos de alerta.      {@link #INFO}
    *  {@link #WARNING}
     *  {@link #ERROR}
     *  {@link #SUCCESS}
     */
    public static enum Type {
        /**
         * Info é uma mensagem informativa, a cor padrão será azul.
         */
        INFO,
        /**
         * Warning é uma mensagem de alerta, a cor padrão será amarela.
         */
        WARNING,
        /**
         * Error é uma mensagem de erro, a cor padrão será vermelha.
         */
        ERROR,
        /**
         * Success é uma mensagem de sucesso, a cor padrão será verde.
         */
        SUCCESS
    }

    /**
     * Posição na tela em que a mensagem irá aparecer. Atente-se que se uma
     * mensagem estiver aparecendo em determina posição, se você inserir nova
     * mensagem, a posição da nova mensagem será ignorada e a mesma aparecerá
     * junto à que já está aparecendo.
     */
    public static enum Position {
        TOP_LEFT("toast-top-left"), TOP_CENTER("toast-top-center"), TOP_RIGHT("toast-top-right"),
        MIDDLE_CENTER("toast-middle-center"), MIDDLE_LEFT("toast-middle-left"), MIDDLE_RIGHT("toast-middle-right"),
        BOTTOM_LEFT("toast-bottom-left"), BOTTOM_CENTER("toast-bottom-center"), BOTTOM_RIGHT("toast-bottom-right");

        private String value = "";

        Position(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static boolean tapToDismiss;
    private static boolean newestOnTop;
    private static boolean progressBar;
    private static boolean debug;
    private static boolean preventDuplicates;
    private static boolean escapeHtml;

    private static String showMethod;
    private static String showEasing;
    private static Integer showDuration;

    private static String hideMethod;
    private static String hideEasing;
    private static Integer hideDuration;

    private static boolean closeMethod;
    private static boolean closeEasing;
    private static boolean closeDuration;
    private static boolean closeOnHover;
    private static boolean closeButton;

    private static Integer timeOut;
    private static Integer extendedTimeOut;

    private static String positionClass;
    private static String closeHtml;

    private static String configuration;

    /**
     * Mostra mensagem
     *
     * @param message Mensagem
     * @param title Título da mensagem
     * @param type Tipo da mensagem
     * @param position Posição na tela em que a mensagem aparecerá
     * @param duration Tempo em milisegundos que a mensagem ficará visível, valor 0 fará nunca fechar
     */
    public static void show(String message, String title, Type type, Position position, Integer duration) {
        _loadDefalts();
        positionClass = position.getValue();
        timeOut = duration;
        _show(message, title, type);
    }

    /**
     * Mostra mensagem
     *
     * @param message Mensagem
     * @param title Título da mensagem
     * @param type Tipo da mensagem
     * @param position Posição na tela em que a mensagem aparecerá
     */
    public static void show(String message, String title, Type type, Position position) {
        _loadDefalts();
        positionClass = position.getValue();
        _show(message, title, type);
    }

    /**
     * Mostra mensagem
     *
     * @param message Mensagem
     * @param title Título da mensagem
     * @param type Tipo da mensagem
     * @param duration Tempo em milisegundos que a mensagem ficará visível, valor 0 fará nunca fechar
     */
    public static void show(String message, String title, Type type, Integer duration) {
        _loadDefalts();
        timeOut = duration;
        _show(message, title, type);
    }

    /**
     * Mostra mensagem
     *
     * @param message Mensagem
     * @param title Título da mensagem
     * @param type Tipo da mensagem
     */
    public static void show(String message, String title, Type type) {
        _loadDefalts();
        _show(message, title, type);
    }

    private static void _loadDefalts() {
        tapToDismiss = false;
        newestOnTop = true;
        progressBar = true;
        debug = false;
        preventDuplicates = false; //Duplicados são encontrados baseados no conteúdo da mensagem
        escapeHtml = false;
        showMethod = "slideDown"; //Possíveis métodos fadeIn/fadeOut, slideDown/slideUp e show/hide
        showEasing = "easeOutElastic"; // Todos o possíveis valores de easing em https://gsgd.co.uk/sandbox/jquery/easing/
        showDuration = 1000;
        hideMethod = "fadeOut";
        hideEasing = "swing";
        hideDuration = 1000;
        closeMethod = false;
        closeEasing = false;
        closeDuration = false;
        closeOnHover = true; // Usado com extendedTimeOut
        closeButton = true;
        timeOut = 5000; // Quanto tempo o toast vai durar sem interação do usuário. Para que o toast nunca feche, deixe timeOut e extendedTimeOut com valor 0.
        extendedTimeOut = 5000; // Quanto tempo o toast vai durar depois de passar o mouse em cima
        positionClass = "toast-top-right";
        closeHtml = "<button type=\"button\">&times;</button>";
    }

    private static void _show(String message, String title, Type type) {
        configuration = "tapToDismiss:" + tapToDismiss + ","
                + "newestOnTop:" + newestOnTop + ","
                + "progressBar:" + progressBar + ","
                + "debug:" + debug + ","
                + "preventDuplicates:" + preventDuplicates + ","
                + "showMethod:'" + showMethod + "',"
                + "showDuration: " + showDuration + ","
                + "showEasing:'" + showEasing + "',"
                + "hideMethod:'" + hideMethod + "',"
                + "hideDuration: " + hideDuration + ","
                + "hideEasing:'" + hideEasing + "',"
                + "timeOut: " + timeOut + ","
                + "extendedTimeOut: " + extendedTimeOut + ","
                + "positionClass:'" + positionClass + "',"
                + "escapeHtml:" + escapeHtml + ","
                + "closeHtml:'" + closeHtml + "',"
                + "closeMethod:" + closeMethod + ","
                + "closeDuration:" + closeDuration + ","
                + "closeEasing:" + closeEasing + ","
                + "closeOnHover:" + closeOnHover + ","
                + "closeButton:" + closeButton + "";

        String selectedType = null;

        if (type.equals(Type.INFO)) {
            selectedType = "toastr.info";
        }
        if (type.equals(Type.WARNING)) {
            selectedType = "toastr.warning";
        }
        if (type.equals(Type.SUCCESS)) {
            selectedType = "toastr.success";
        }
        if (type.equals(Type.ERROR)) {
            selectedType = "toastr.error";
        }

        if (selectedType != null) {
            Clients.evalJavaScript("" + selectedType + "('" + message + "', '" + title + "', {" + configuration + "})");
        }
    }

}

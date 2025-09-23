/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package humcare.utilitarios;

/**
 *
 * @author alison
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;

public class IdenticonGenerator {

    public static final Long GITHUB_STYLE = 1L;
    public static final Long FIRSTLETTERS_STYLE = 2L;

    public IdenticonGenerator() {

    }

    /**
     * Github Style. Created by grender on 01/07/17.
     * https://gist.github.com/GrenderG
     *
     */
    private BufferedImage generateGithubIcon(String text, int image_width, int image_height) {

        // Create a md5-hexadecimal string from text
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.toLowerCase().getBytes("CP1252"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            text = sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println("Erro ao tratar texto ao gerar Identicon");
        }

        // Create a image  
        int width = 5, height = 5;
        BufferedImage identicon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = identicon.getRaster();

        byte[] hash = text.getBytes();
        int[] background = new int[]{255, 255, 255, 112};
        int[] foreground = new int[]{hash[0] & 255, hash[1] & 255, hash[2] & 255, 255};

        for (int x = 0; x < width; x++) {
            // Enforce horizontal symmetry
            int i = x < 3 ? x : 4 - x;
            for (int y = 0; y < height; y++) {
                int[] pixelColor;
                // Toggle pixels based on bit being on/off
                if ((hash[i] >> y & 1) == 1) {
                    pixelColor = foreground;
                } else {
                    pixelColor = background;
                }
                raster.setPixel(x, y, pixelColor);
            }
        }

        // Scale image to the size you want
        BufferedImage finalImage = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(image_width / width, image_height / height);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        finalImage = op.filter(identicon, finalImage);

        return finalImage;
    }

    /**
     * First Letters Google Style. Created by Alison-NPD
     *
     */
    private BufferedImage generateFirstLettersIcon(String text, int image_width, int image_height) {

        // Always choosing the same color for a given text
        List<int[]> google_colors = new ArrayList<>();
        google_colors.add(new int[]{226, 95, 81, 255});
        google_colors.add(new int[]{242, 96, 145, 255});
        google_colors.add(new int[]{187, 101, 202, 255});
        google_colors.add(new int[]{149, 114, 207, 255});
        google_colors.add(new int[]{120, 132, 205, 255});
        google_colors.add(new int[]{91, 149, 249, 255});
        google_colors.add(new int[]{72, 194, 249, 255});
        google_colors.add(new int[]{69, 208, 226, 255});
        google_colors.add(new int[]{72, 182, 172, 255});
        google_colors.add(new int[]{82, 188, 137, 255});
        google_colors.add(new int[]{155, 206, 95, 255});
        google_colors.add(new int[]{212, 227, 74, 255});
        google_colors.add(new int[]{254, 218, 16, 255});
        google_colors.add(new int[]{247, 192, 0, 255});
        google_colors.add(new int[]{255, 168, 0, 255});
        google_colors.add(new int[]{255, 138, 96, 255});
        google_colors.add(new int[]{194, 194, 194, 255});
        google_colors.add(new int[]{143, 164, 175, 255});
        google_colors.add(new int[]{162, 136, 126, 255});
        google_colors.add(new int[]{163, 163, 163, 255});
        google_colors.add(new int[]{175, 181, 226, 255});
        google_colors.add(new int[]{179, 155, 221, 255});
        google_colors.add(new int[]{194, 194, 194, 255});
        google_colors.add(new int[]{124, 222, 235, 255});
        google_colors.add(new int[]{188, 170, 164, 255});
        google_colors.add(new int[]{173, 214, 125, 255});

        byte[] hash = text.getBytes();
        BigInteger bi = new BigInteger(1, hash);
        BigInteger mod = BigInteger.valueOf(google_colors.size());

        int[] foreground = new int[]{255, 255, 255, 190};
        int[] background = google_colors.get(bi.mod(mod).intValue());

        // Selecting the initials of the name
        String firstWord;
        String lastWord;
        if (text.trim().indexOf(" ") > 0) {
            firstWord = text.trim().substring(0, text.trim().indexOf(" "));
            lastWord = text.trim().substring(text.trim().lastIndexOf(" ") + 1);
        } else {
            firstWord = text.trim();
            lastWord = "";
        }
        text = ((firstWord.length() > 0 ? firstWord.substring(0, 1) : "") + (lastWord.length() > 0 ? lastWord.substring(0, 1) : "")).trim().toUpperCase();

        // Creating image
        BufferedImage finalImage = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = finalImage.createGraphics();

        Color backgroundColor = new Color(background[0], background[1], background[2], background[3]);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, image_width, image_height);

        Color fontColor = new Color(foreground[0], foreground[1], foreground[2], foreground[3]);
        g.setColor(fontColor);

        Font font = null;
        try {
            String caminho = ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getRealPath("") + "/fonts/Roboto-Medium.ttf";
            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(caminho)).deriveFont(52f);
        } catch (Exception ex) {
            font = new Font("Arial", Font.BOLD, 52);
        }
        g.setFont(font);

        // Centering text
        FontMetrics fm = g.getFontMetrics();
        int x = (image_width - fm.stringWidth(text)) / 2;
        int y = (image_height - fm.getHeight()) / 2 + fm.getAscent();

        // Drawing
        g.drawString(text, x, y);

        return finalImage;
    }

    /**
     * Obtém sempre a mesma imagem que representa determinado texto
     *
     * @param text Texto que irá gerar uma imagem única baseado nele
     * @param style Tipo que pode ser IdenticonGenerator.GITHUB_STYLE ou
     * IdenticonGenerator.FIRSTLETTERS_STYLE
     * @return Imagem em array de bytes
     */
    public byte[] getInBytes(String text, Long style) {

        BufferedImage image = null;
        if (style.equals(GITHUB_STYLE)) {
            image = generateGithubIcon(text, 100, 100);
        } else if (style.equals(FIRSTLETTERS_STYLE)) {
            image = generateFirstLettersIcon(text, 100, 100);
        }

        byte[] imageInByte = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(IdenticonGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return imageInByte;
    }

    /**
     * Obtém sempre a mesma imagem que representa determinado texto
     *
     * @param text Texto que irá gerar uma imagem única baseado nele
     * @param style Tipo que pode ser IdenticonGenerator.GITHUB_STYLE ou
     * IdenticonGenerator.FIRSTLETTERS_STYLE
     * @return Imagem do tipo AImage para usar no componente de imagem do ZK
     * Framework
     */
    public AImage getAImage(String text, Long style) {
        AImage image = null;
        try {
            image = new AImage("", getInBytes(text, style));
        } catch (Exception ex) {
            System.out.println("ERRO: Não foi possível gerar o Identicon!");
        }

        return image;
    }

}

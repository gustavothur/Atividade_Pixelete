import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gustavo.thur on 27/03/2017.
 */
public class Pixelate {

    public int saturate (int value){
        if(value > 255)
            return 255;
        else if (value < 0)
            return 0;
        else
            return value;
    }

    public float[][] sharpening(int a){

        if(a == 1)
        {
            return new float[][]{{0.0f, -1.0f, 0.0f},
                                 {-1.0f, 2.0f, -1.0f},
                                 {0.0f, -1.0f, 0.0f}};
        }
        else
            return new float[][]{{0,0},{0,0}};
    }

    public BufferedImage Pixalete (BufferedImage img, int tamPixel){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < img.getHeight(); y += tamPixel)
        {
            for(int x = 0; x < img.getWidth(); x+= tamPixel)
            {
                int med = tamPixel / 2;
                int medX, medY;
                if((x + med) > img.getWidth())
                    medX = x;
                else
                    medX = (x + med);
                if((y + med) > img.getHeight())
                    medY = y;
                else
                    medY = (y + med);

                Color pixel_meio = new Color(img.getRGB(medX - 1, medY - 1));
                for (int ky = 0; ky < tamPixel; ky++)
                {
                    for (int kx = 0; kx < tamPixel; kx++)
                    {
                        int px = x + (kx - med);
                        int py = y + (ky - med);
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight())
                            continue;
                        out.setRGB(px, py, pixel_meio.getRGB());
                    }
                }
            }
        }
        return out;
    }

    public BufferedImage Sharpening (BufferedImage img, float[][] sharp, int pixel_tamanho){
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < img.getHeight(); y += pixel_tamanho)
        {
            for(int x = 0; x < img.getWidth(); x += pixel_tamanho)
            {
                int red = 0;
                int green = 0;
                int blue = 0;

                for(int ky = 0; ky < 3; ky++)
                {
                    for(int kx = 0; kx < 3; kx++)
                    {
                        int px = x + (kx - 1);
                        int py = y + (ky - 1);

                        if(px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight())
                            continue;

                        Color pixel = new Color(img.getRGB(px,py));
                        red += saturate((int)(pixel.getRed()*sharp[kx][ky]));
                        green += saturate((int)(pixel.getGreen()*sharp[kx][ky]));
                        blue += saturate((int)(pixel.getBlue()*sharp[kx][ky]));
                    }
                }
                Color pix = new Color(red,green,blue);
                for (int ky = 0; ky < pixel_tamanho; ky++)
                {
                    for(int kx = 0; kx < pixel_tamanho; kx++)
                    {
                        int medX, medY;

                        if((x + kx) >= img.getWidth())
                            medX = x;
                        else
                            medX = x + kx;
                        if((y + ky) >= img.getHeight())
                            medY = y;
                        else
                            medY = y + ky;

                        System.out.println("MedX: " + medX);
                        System.out.println("MedY: " + medY + "\n");
                        out.setRGB(medX,medY,pix.getRGB());
                    }
                }
            }
        }
        return out;
    }

    public void run () throws IOException {
        File PATH = new File("C:\\Users\\gustavo.thur\\IdeaProjects\\Dunno2\\src");
        BufferedImage arcoiris = ImageIO.read(new File(PATH, "arco_iris.jpg"));
        BufferedImage pixeleteArcoIris = Pixalete(arcoiris, 3);
        BufferedImage sharpenedArcoIris = Sharpening(pixeleteArcoIris, sharpening(1), 3);
        ImageIO.write(pixeleteArcoIris, "png", new File(PATH, "pixeleteArcoIris.png"));
        ImageIO.write(sharpenedArcoIris, "png", new File(PATH, "sharpenedArcoIris.png"));

    }

    public static void main (String[] args) throws IOException {
        Pixelate atividade = new Pixelate();
        atividade.run();
    }
}

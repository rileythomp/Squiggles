import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FloodFill {
    private final int len = 360;
    private final int IMG_TYPE = 5;
    private int[][] board;
    private int curx;
    private int cury;
    private int outlineLen;
    private int fills;

    public FloodFill() throws IOException {
        fills = 0;
        board = new int[len][len];
        System.out.println("Running FloodFill...");

        // start draw outline

        drawOutline(len/16, len/2, len/2);
        drawOutline(len/16 + len/8, len/2, len/4);



        drawOutline(len/16 + 175, len/4 + 10, len/4 + 20);
        drawOutline(len/16 + len/16 + 185, len/4 + 10, len/8);



        drawOutline(len/16 + 175, len/2 + len/4 - 10, len/4 + 20);
        drawOutline(len/16 + len/16 + 185, len/2 + len/4 - 10, len/8);

        // end draw outline

        genImage("outline");

        try {
            fill(len/8, len/2, 2);
        }
        catch (StackOverflowError e) {
            System.out.println("Caught stack overflow error after " + fills + " calls");
            genImage("floodfill");
            return;
        }

        try {
            fill(213, len/4 + 10, 3);
        }
        catch (StackOverflowError e) {
            System.out.println("Caught stack overflow error after " + fills + " calls");
            genImage("floodfill");
            return;
        }

        try {
            fill(213, len/2 + len/4 - 10, 4);
        }
        catch (StackOverflowError e) {
            System.out.println("Caught stack overflow error after " + fills + " calls");
            genImage("floodfill");
            return;
        }

        genImage("floodfill");
    }

    private void drawOutline(int inity, int initx, int initlen) {
//        cury = len/4;
//        curx = len/2;
        cury = inity;
        curx = initx;
        outlineLen = initlen;

        // down and left
        updateBoard(1, -1);

        // down and right
        updateBoard(1, 1);

        // up and right
        updateBoard(-1, 1);

        // up and left
        cury--;
        while ((curx >= initx && cury >= inity) && (board[cury][curx] != 1)) {
            board[cury][curx] = 1;
            double dir = Math.random()*2;

            if (dir < 1) {
                // go down
                cury--;
            }
            else {
                // go left;
                curx--;
            }

        }

        if (cury + 1 == inity || cury == inity) {
            if (cury + 1 == inity) {
                cury++;
            }

            for (int x = curx; x >= initx; --x) {
                board[cury][x] = 1;
            }
        }

        if (curx + 1 == initx || curx == initx) {
            if (curx + 1 == initx) {
                curx++;
            }

            if (cury <= inity) {
                for (int y = cury; y <= inity; ++y) {
                    board[y][curx] = 1;
                }
            }
            else {
                for (int y = cury; y >= inity; --y) {
                    board[y][curx] = 1;
                }
            }
        }
    }

    private void fill(int y, int x, int colorCode) throws StackOverflowError {
        fills++;
        board[y][x] = colorCode;

        if (board[y - 1][x] == 0) {
            fill(y - 1, x, colorCode);
        }
        if (board[y + 1][x] == 0) {
            fill(y + 1, x, colorCode);
        }
        if (board[y][x - 1] == 0) {
            fill(y, x - 1, colorCode);
        }
        if (board[y][x + 1] == 0) {
            fill(y, x + 1, colorCode);
        }
    }

    private void genImage(String name) throws IOException {
        BufferedImage img = new BufferedImage(len, len, IMG_TYPE);

        // set pixels
        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                if (board[y][x] == 1) {
                    int rgb = 0;
                    rgb = (rgb << 8) + 0;
                    rgb = (rgb << 8) + 0;
                    img.setRGB(x, y, rgb);
                }
                else if (board[y][x] == 2) {
                    int rgb = 64;
                    rgb = (rgb << 8) + 128;
                    rgb = (rgb << 8) + 255;
                    img.setRGB(x, y, rgb);
                }
                else if (board[y][x] == 3) {
                    int rgb = 255;
                    rgb = (rgb << 8) + 128;
                    rgb = (rgb << 8) + 64;
                    img.setRGB(x, y, rgb);
                }
                else if (board[y][x] == 4) {
                    int rgb = 64;
                    rgb = (rgb << 8) + 255;
                    rgb = (rgb << 8) + 128;
                    img.setRGB(x, y, rgb);
                }
                else {
                    int rgb = 255;
                    rgb = (rgb << 8) + 255;
                    rgb = (rgb << 8) + 255;
                    img.setRGB(x, y, rgb);
                }
            }
        }

        File file = new File(name + ".jpg");
        ImageIO.write(img, "jpg", file);
        System.out.println("Created " + name + ".jpg");
    }

    private void updateBoard(int dy, int dx) {
        for (int i = 0; i < outlineLen; ++i) {
            double dir = Math.random()*2;

            if (dir < 1) {
                cury += dy;
            }
            else {
                curx += dx;
            }

            board[cury][curx] = 1;
        }
    }

}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class FloodFill {
    private final int len = 256;
    private final int outlineLen = 128;
    private final int IMG_TYPE = 5;
    private int[][] board;
    private int curx;
    private int cury;
    private int fills;

    public FloodFill() throws IOException {
        fills = 0;
        board = new int[len][len];
        System.out.println("Running FloodFill...");
        long start = Instant.now().toEpochMilli();

        cury = len/4;
        curx = len/2;

        // down and left
        updateBoard(1, -1);

        // down and right
        updateBoard(1, 1);

        // up and right
        updateBoard(-1, 1);

        // up and left
        cury--;
        while ((curx >= len/2 && cury >= len/4) && (board[cury][curx] != 1)) {
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

        if (cury + 1 == len/4 || cury == len/4) {
            if (cury + 1 == len/4) {
                cury++;
            }

            for (int x = curx; x >= len/2; --x) {
                board[cury][x] = 1;
            }
        }

        if (curx + 1 == len/2 || curx == len/2) {
            if (curx + 1 == len/2) {
                curx++;
            }

            if (cury <= len/4) {
                for (int y = cury; y <= len/4; ++y) {
                    board[y][curx] = 1;
                }
            }
            else {
                for (int y = cury; y >= len/4; --y) {
                    board[y][curx] = 1;
                }
            }
        }

        genImage("outline");

        try {
            fill(len/4 + outlineLen/2, len/2);
        }
        catch (StackOverflowError e) {
            System.out.println("Caught stack overflow error after " + fills + " calls");
            genImage("floodfill");
        }

        genImage("floodfill");
    }

    private void fill(int y, int x) throws StackOverflowError {
        fills++;
        board[y][x] = 2;

        if (board[y - 1][x] == 0) {
            fill(y - 1, x);
        }
        if (board[y + 1][x] == 0) {
            fill(y + 1, x);
        }
        if (board[y][x - 1] == 0) {
            fill(y, x - 1);
        }
        if (board[y][x + 1] == 0) {
            fill(y, x + 1);
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

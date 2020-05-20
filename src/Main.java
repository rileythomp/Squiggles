import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static Map<Integer, Pair<Integer, Integer>> neighbourMap;
    static {
        neighbourMap = new HashMap<>();
        neighbourMap.put(0, new Pair(-1, -1));
        neighbourMap.put(1, new Pair(-1, 0));
        neighbourMap.put(2, new Pair(-1, 1));
        neighbourMap.put(3, new Pair(0, -1));
        neighbourMap.put(4, new Pair(0, 1));
        neighbourMap.put(5, new Pair(1, -1));
        neighbourMap.put(6, new Pair(1, -0));
        neighbourMap.put(7, new Pair(1, 1));
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Flood fill or squiggles (f or s): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("f")) {
            FloodFill ff = new FloodFill();
        }
        else if (input.equalsIgnoreCase("s")) {
            System.out.println("Running Squiggles...");
            long start = Instant.now().toEpochMilli();

            int[][] board = new int[512][512];
            for (int y = 0; y < board.length; ++y) {
                for (int x = 0; x < board[0].length; ++x) {
                    board[y][x] = 0;
                }
            }

            int curx = 0;
            int cury = 0;
            board[cury][curx] = 1;


            for (int i = 0; i < 6942069; ++i) {
                // if cant move anywhere, break
                boolean hasMove = false;
                for (int j = 0; j < neighbourMap.size(); ++j) {
                    int poty = cury + neighbourMap.get(j).getKey();
                    int potx = curx + neighbourMap.get(j).getValue();

                    if (potx >= 0 && potx < board.length && poty >= 0 && poty < board.length && neighboursFilled(potx, poty, board) < 2 && board[poty][potx] != 1) {
                        hasMove = true;
                    }
                }

                while (!hasMove) {
                    // no, go to a random cell that has been moved to already and continue
                    int neighbour = (int)(Math.random()*8);
                    int poty = cury + neighbourMap.get(neighbour).getKey();
                    int potx = curx + neighbourMap.get(neighbour).getValue();
                    if (potx >= 0 && potx < board.length && poty >= 0 && poty < board.length && board[poty][potx] == 1) {
                        curx = potx;
                        cury = poty;
                        break;
                    }
                }

                while (hasMove) {
                    int neighbour = (int)(Math.random()*8); // num between 0 and 7

                    int poty = cury + neighbourMap.get(neighbour).getKey();
                    int potx = curx + neighbourMap.get(neighbour).getValue();

                    if (potx >= 0 && potx < board.length && poty >= 0 && poty < board.length && neighboursFilled(potx, poty, board) < 2 && board[poty][potx] != 1) {
                        curx = potx;
                        cury = poty;
                        board[cury][curx] = 1;
                        break;
                    }
                }
            }

            BufferedImage img = new BufferedImage(512, 512, 5);

            for (int y = 0; y < img.getHeight(); ++y) {
                for (int x = 0; x < img.getWidth(); ++x) {
                    if (board[y][x] == 1) {
                        int rgb = 0;
                        rgb = (rgb << 8) + 0;
                        rgb = (rgb << 8) + 0;
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

            File file = new File("squiggles.jpg");
            ImageIO.write(img, "jpg", file);
            System.out.println("Created squiggles.jpg");

            long end = Instant.now().toEpochMilli();
            System.out.println("Finished Tracer in " + formatSeconds(start, end));
        }
    }

    public static boolean moveNotPossible(int x, int y, int[][] board) {
        boolean moveNotPossible = true;

        if (x > 0 && y > 0 && board[y-1][x-1] == 0) {
            moveNotPossible = false;
        }
        if (y > 0 && board[y-1][x] == 0) {
            moveNotPossible = false;
        }
        if (x < board.length - 1 && y > 0 && board[y-1][x+1] == 0) {
            moveNotPossible = false;
        }
        if (x > 0 && board[y][x-1] == 0) {
            moveNotPossible = false;
        }
        if (x < board.length - 1 && board[y][x+1] == 0) {
            moveNotPossible = false;
        }
        if (x > 0 && y < board.length - 1 && board[y+1][x-1] == 0) {
            moveNotPossible = false;
        }
        if (y < board.length - 1 && board[y+1][x] == 0) {
            moveNotPossible = false;
        }
        if (x < board.length - 1 && y < board.length - 1 && board[y+1][x+1] == 0) {
            moveNotPossible = false;
        }

        return moveNotPossible;
    }

    public static int neighboursFilled(int x, int y, int[][] board) {
        int neighboursFilled = 0;

        if (x > 0 && y > 0 && board[y-1][x-1] == 1) {
            neighboursFilled++;
        }
        if (y > 0 && board[y-1][x] == 1) {
            neighboursFilled++;
        }
        if (x < board.length - 1 && y > 0 && board[y-1][x+1] == 1) {
            neighboursFilled++;
        }
        if (x > 0 && board[y][x-1] == 1) {
            neighboursFilled++;
        }
        if (x < board.length - 1 && board[y][x+1] == 1) {
            neighboursFilled++;
        }
        if (x > 0 && y < board.length - 1 && board[y+1][x-1] == 1) {
            neighboursFilled++;
        }
        if (y < board.length - 1 && board[y+1][x] == 1) {
            neighboursFilled++;
        }
        if (x < board.length - 1 && y < board.length - 1 && board[y+1][x+1] == 1) {
            neighboursFilled++;
        }

        return neighboursFilled;
    }

    public static int updateXWithGuess(int guess, int curx) {
        if (guess == 0 || guess == 3 || guess == 5) {
            return curx - 1;
        }
        else if (guess == 1 || guess == 6) {
            return curx;
        }
        else {
            return curx + 1;
        }
    }

    public static int updateYWithGuess(int guess, int cury) {
        if (guess < 3) {
            return cury -1;
        }
        else if (guess < 5) {
            return cury;
        }
        else {
            return cury + 1;
        }
    }

    public static int getIntegerInput(String question) {
        Scanner scanner = new Scanner(System.in);
        int ans = 0;
        while (true) {
            System.out.print(question);
            String iterations = scanner.next();
            try {
                ans = Integer.parseInt(iterations);
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a positive integer");
            }
            catch (NullPointerException e) {
                System.out.println("Please enter a positive integer");
            }
        }

        scanner.close();
        return ans;
    }

    public static File getImageInput(String question) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(question);
            String imgName = scanner.next();
            File imgFile = new File(imgName);
            if (imgFile.exists()) {
                scanner.close();
                return imgFile;
            }
            else {
                System.out.println(imgFile.getName() + " does not exist");
            }
        }
    }

    public static boolean getBooleanInput(String question) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(question);
            String bool = scanner.next();
            if (bool.equalsIgnoreCase("y")) {
                scanner.close();
                return true;
            }
            else if (bool.equalsIgnoreCase("n")) {
                scanner.close();
                return false;
            }
            else {
                System.out.println("Please enter y or n");
            }
        }
    }

    public static String formatSeconds(long start, long end) {
        int seconds = (int)((end - start)/1000);
        int minutes = 0;

        while (seconds >= 60) {
            minutes++;
            seconds -= 60;
        }

        return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }
}

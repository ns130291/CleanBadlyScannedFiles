/*
Copyright (C) 2014 ns130291

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/


package de.nsvb.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author ns130291
 */
public class CleanBadlyScannedFiles {

    private static final int[][] punkt = {{-1, 0, -1}, {0, 1, 0}, {-1, 0, -1}};

    //not needed, covered by pattern 'punkt'
    //private static final int[][] treppe1 = {{0, 0, 0, -1}, {0, 1, 0, 0}, {0, 0, 1, 0}, {-1, 0, 0, 0}};
    //private static final int[][] treppe2 = {{-1, 0, 0, 0}, {0, 0, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, -1}};

    private static final int[][] strich1 = {{0, 0, 0}, {0, 1, 0}, {0, 1, 0}, {0, 0, 0}};
    private static final int[][] strich2 = {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};

    private static final int[][] viereck = {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};

    private static final int[][] winkel1 = {{0, 0, 0, 0}, {0, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};
    private static final int[][] winkel2 = {{0, 0, 0, 0}, {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};
    private static final int[][] winkel3 = {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}};
    private static final int[][] winkel4 = {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                long time = System.currentTimeMillis();

                BufferedImage img = ImageIO.read(new File(args[0]));

                int staubkörner = 0;

                //Patterns with black pixel at [1][1]
                ArrayList<int[][]> patterns1 = new ArrayList<>();
                patterns1.add(punkt);
                patterns1.add(strich1);
                patterns1.add(strich2);
                //patterns1.add(treppe1);
                patterns1.add(winkel2);
                patterns1.add(winkel3);
                patterns1.add(winkel4);
                patterns1.add(viereck);
                
                //Patterns with white pixel at [1][1], but black pixel at [2][1]
                ArrayList<int[][]> patterns2 = new ArrayList<>();
                //patterns2.add(treppe2);
                patterns2.add(winkel1);

                //border regions are not yet processed
                for (int x = img.getMinX() + 1; x < img.getWidth() - 2; x++) {
                    for (int y = img.getMinY() + 1; y < img.getHeight() - 2; y++) {
                        if (img.getRGB(x, y) == Color.BLACK.getRGB()) {
                            staubkörner += process(img, x, y, patterns1);
                        } else if (img.getRGB(x + 1, y) == Color.BLACK.getRGB()) {
                            staubkörner += process(img, x, y, patterns2);
                        }
                    }
                }

                ImageIO.write(img, "png", new File(args[0]));
                System.out.println(staubkörner + " Staubkörner gefunden und entfernt");
                System.out.println("Dauer: " + (System.currentTimeMillis() - time) / 1000.0);

            } catch (IOException ex) {
                System.out.println("Image not found");
            }
        } else {
            System.out.println("Missing image path");
        }
    }

    private static boolean match(BufferedImage img, int x, int y, int p) {
        int color = img.getRGB(x, y);
        return p == -1 || p == 1 && color == Color.BLACK.getRGB() || p == 0 && color == Color.WHITE.getRGB();
    }

    private static int process(BufferedImage img, int x, int y, ArrayList<int[][]> patterns) {

        int staubkörner = 0;
        for (int[][] pattern : patterns) {

            boolean match = true;
            int x1 = x - 1;
            int y1 = y - 1;
            loop:
            for (int i = 0; i < pattern.length; i++) {
                for (int j = 0; j < pattern[i].length; j++) {
                    if (!match(img, x1 + i, y1 + j, pattern[i][j])) {
                        match = false;
                        break loop;
                    }
                }
            }

            if (match) {
                for (int i = 0; i < pattern.length; i++) {
                    for (int j = 0; j < pattern[i].length; j++) {
                        if (pattern[i][j] == 1) {
                            img.setRGB(x1 + i, y1 + j, Color.WHITE.getRGB());
                        }
                    }
                }
                staubkörner++;
                y++;
                break;
            }
        }
        return staubkörner;
    }

}

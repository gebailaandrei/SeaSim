import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class Earth extends JComponent{
    private final float[][] map = new float[2400000][3];
    int totalPoints = 0;
    String fileName = "src\\earth.xyz";

    public void readDataArray(String fileName) throws Exception{
        Scanner scanner = new Scanner(new File(fileName));
        String[] line;

        while(scanner.hasNextLine()) {
            line = scanner.nextLine().split("\\s+");
            for(int j = 0; j < 3; j++)
                map[totalPoints][j] = Float.parseFloat(line[j]);
            totalPoints++;
        }
        scanner.close();
    }

    public void paintComponent(Graphics gr) {
        for (int i = 0; i < totalPoints; i++) {
            //LAND - GREEN AREAS
            for(int j = seaLevel; j <= 1500; j += 100)
            {
                if(map[i][1] < -60 && map[i][2] > seaLevel)
                {
                    gr.setColor(new Color(255, 255, 255));
                    break;
                }
                if(map[i][2] > j && map[i][2] < j + 100)
                {
                    int r = 50 - j / 200 * 2;
                    int g = 100 - j / 100 * 6;
                    gr.setColor(new Color(r, g, 0));
                    break;
                }
            }
            // LAND - MOUNTAIN AREAS
            for(int j = 1500 + seaLevel; j <= 4000; j += 100)
            {
                if(map[i][1] < -60 && map[i][2] > seaLevel)
                {
                    gr.setColor(new Color(255, 255, 255));
                    break;
                }
                if(map[i][2] > j && map[i][2] < j + 100)
                {
                    int r = 105 - (j - 1500) / 50;
                    int g = 65 - (j - 1500) / 100;
                    gr.setColor(new Color(r, g, 0));
                    break;
                }
            }
            for(int j = 4000 + seaLevel; j <= 8000; j += 250)
            {
                if(map[i][1] < -602 && map[i][2] > seaLevel)
                {
                    gr.setColor(new Color(255, 255, 255));
                    break;
                }
                if(map[i][2] > j && map[i][2] < j + 250)
                {
                    int rgb = 95 + j / 100 * 2;
                    gr.setColor(new Color(rgb, rgb, rgb)); // Mountain Peak Snow
                    break;
                }
            }
            //WATER
            for(int j = seaLevel; j >= -6000 + seaLevel; j -= 75)
            {
                if(map[i][2] > j - 75 && map[i][2] < j)
                {
                    int g = 170 + (j / 75) * 2;
                    int b = 245 + (j / 75) * 3;
                    gr.setColor(new Color(5, g, b));
                    break;
                }
            }
            gr.fillRect(coordinates[i][0], coordinates[i][1], 1, 1);
        }
    }

    int[][] coordinates; // The coordinates in the map after being scaled
    int seaLevel = 0;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width + 20,
            height = Toolkit.getDefaultToolkit().getScreenSize().height - 70;
    double xMin = 0.0, xMax = 360.0, yMin = -90.0, yMax = 90.0;

    public int scaleX(double x) { return (int) (width * (x - xMin) / (xMax - xMin));} // scales x coordinates for map representation
    public int scaleY(double y) { return (int)(height * (yMin - y)/(yMax - yMin) +height); } // scales y coordinates for map representation

    public void generateMap() throws Exception {
        Scanner input = new Scanner(System.in);
        do{
            System.out.println("Enter a sea rise level to simulate: ");
            seaLevel = input.nextInt();
        }while(seaLevel < -100 || seaLevel > 100);

        readDataArray(fileName);
        coordinates = new int[totalPoints][2];
        //height = (int) (width * (yMax - yMin) / (xMax - xMin));

        for (int i = 0; i < totalPoints; i++) {
            if(i < totalPoints /2){
                coordinates[i][0] = scaleX(map[i + totalPoints /2][0]);
                coordinates[i][1] = scaleY(map[i][1]);
            }else{
                coordinates[i][0] = scaleX(map[i - totalPoints /2][0]);
                coordinates[i][1] = scaleY(map[i][1]);
            }

        }
    }

    public void createMap() throws Exception{
        Earth map = new Earth();
        map.generateMap();
        JFrame f = new JFrame("World Map");
        f.setSize(width, height);
        f.add(map);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        Earth map = new Earth();
        map.createMap();
    }
}

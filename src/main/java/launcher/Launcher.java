package launcher;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Launcher extends JFrame {

    private static String whereIsJavaFX = "";

    public static void main(String[] args) {

        JFrame frame = new JFrame("Launch Client");
        JButton b = new JButton("Select JavaFX Library...");
        JButton l = new JButton("Launch");
        b.setBounds(50, 30, 200, 30);
        l.setBounds(100, 100, 100, 30);
        frame.add(b);
        frame.add(l);
        frame.setSize(300, 200);
        frame.setLayout(null);

        b.addActionListener(actionEvent -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose your JavaFX directory: ");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnValue = jfc.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (jfc.getSelectedFile().isDirectory()) {
                    whereIsJavaFX = jfc.getSelectedFile().toString();
                    System.out.println("You selected the directory: " + whereIsJavaFX);
                    //File f = new File(".whereIsJavaFX");
                    try {
                        FileWriter fileWriter = new FileWriter(".whereIsJavaFX");
                        PrintWriter printWriter = new PrintWriter(fileWriter);
                        printWriter.print(whereIsJavaFX);
                        printWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        l.addActionListener(actionEvent -> {
            try {
                File f = new File(".whereIsJavaFX");
                if (f.exists()) {
                    Scanner sc = new Scanner(f);
                    whereIsJavaFX = sc.nextLine();
                    Process pro = Runtime.getRuntime().exec("java -jar -Xms256m -Xmx1g --module-path " + whereIsJavaFX.toString() + " --add-modules=javafx.controls,javafx.fxml Client.jar");
                    System.exit(0);
                } else {
                    System.out.println("No JavaFX Lib Path found");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

}

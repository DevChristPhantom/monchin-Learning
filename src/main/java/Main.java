import javax.swing.*;

public class Main {

    public static void main (String[] args) {
        JFrame objFP = new JFrame("Entrenamiento y evaluación con Ecuación Normal");
        objFP.setContentPane(new frmPrin().jpPrin);
        objFP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        objFP.setSize(600,800);
        objFP.setLocationRelativeTo(null);
        objFP.setVisible(true);


    }
}

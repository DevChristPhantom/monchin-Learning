import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class frmPrin {
    public JPanel jpPrin;
    private JPanel jpCarDS;
    private JPanel jpGestBotDS;
    private JPanel jpGestTextaDS;
    private JButton btnCarArch;
    private JTextArea txtaEntre;
    private JTextArea txtaEval;
    private JPanel jpGestOper;
    private JPanel jpBotOper;
    private JPanel jpGrafEstOper;
    private JButton btnEncEcNorm;
    private JTextField txtEcRect;
    private JPanel jpRectMSE;
    private JTextArea txtaMSE;

    private final List<clsDataset> datos = new ArrayList<>();
    private final List<clsDataset> datEntre = new ArrayList<>();
    private final List<clsDataset> datEval = new ArrayList<>();

    public frmPrin() {
        gestEve();

    }

    public void gestEve(){
        btnCarArch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carArchCSV();
            }
        });
        btnEncEcNorm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFreeChart ge = clsGestOper.genGraf(datEntre);
                ChartPanel cp = new ChartPanel(ge);
                cp.setPreferredSize(new Dimension(jpGrafEstOper.getWidth(),jpGrafEstOper.getHeight()));
                jpGrafEstOper.removeAll();
                jpGrafEstOper.add(cp, BorderLayout.CENTER);
                jpGrafEstOper.revalidate();
                jpGrafEstOper.repaint();

                double[] datRL = clsGestOper.calEcNorm(datEntre);
                double m = datRL[0];
                double b = datRL[1];
                txtEcRect.setText("Y = " + String.format("%.2f", m) + "X + " + String.format("%.2f", b));
                txtaMSE.setText(clsGestOper.evalDatMSE(datEval, m, b));
            }
        });
    }

    private void carArchCSV() {
        JFileChooser elecArch = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos archivo", "csv");
        elecArch.setFileFilter(filtro);

        int archSel = elecArch.showOpenDialog(null);
        if (archSel == JFileChooser.APPROVE_OPTION) {
            File arch = elecArch.getSelectedFile();
            leerCSV(arch);
        }
    }

    private void leerCSV(File arch) {
        try (BufferedReader lector = new BufferedReader(new FileReader(arch))) {
            String linea;
            datEntre.clear();
            datEval.clear();
            txtaEntre.setText("");
            txtaEval.setText("");

            //Datos listos para evaluación

            while((linea = lector.readLine()) != null){
                if(!linea.trim().isEmpty()){
                    String[] valores = linea.split(",");
                    try{
                        double x = Double.parseDouble(valores[0]);
                        double y = Double.parseDouble(valores[1]);
                        clsDataset dato = new clsDataset(x,y);
                        datos.add(dato);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            int corte = (int)(datos.size()*0.8);

            for (int i=0; i<datos.size(); i++){
                if(i < corte){
                    datEntre.add(datos.get(i));
                    txtaEntre.append("x: " + datos.get(i).getX() + " | y: " + datos.get(i).getY() + "\n");
                } else {
                    datEval.add(datos.get(i));
                    txtaEval.append("x: " + datos.get(i).getX() + " | y: " + datos.get(i).getY() + "\n");
                }
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error al leer el CSV", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

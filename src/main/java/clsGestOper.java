import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

public class clsGestOper {
    public static JFreeChart genGraf(List<clsDataset> datos){
        if(datos.isEmpty()){
            JOptionPane.showMessageDialog(null, "Cargue un CSV, primero" + "ADVERTENCIA" + JOptionPane.WARNING_MESSAGE);
            return null;
        }
        double[] datRL = calEcNorm(datos);
        double m = datRL[0];
        double b = datRL[1];
        XYSeries ptos = new XYSeries("Datos");
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MIN_VALUE;

        for (clsDataset d : datos){
            ptos.add(d.getX(), d.getY());
            if(d.getX() < minX) minX = d.getX();
            if(d.getX() > maxX) maxX = d.getX();
        }

        XYSeriesCollection dsTemp = new XYSeriesCollection();
        dsTemp.addSeries(ptos);

        JFreeChart ge = ChartFactory.createScatterPlot(
                "Gráfico de dispesión",
                "X",
                "Y",
                dsTemp
        );

        XYPlot plot = ge.getXYPlot();

        //Recta
        XYSeries linea = new XYSeries("Recta");
        linea.add(minX, m * minX * b);
        linea.add(maxX, m * maxX * b);

        XYSeriesCollection dsTemp2 = new XYSeriesCollection();
        dsTemp2.addSeries(linea);

        plot.setDataset(1, dsTemp2);
        plot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        return ge;
    }

    public static double[] calEcNorm(List<clsDataset> datos) {
        int n = datos.size();
        double x, y, sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        double m, b;

        for(clsDataset d : datos){
            sumX += d.getX();
            sumY += d.getY();
            sumXY += d.getY() * d.getX();
            sumX2 += Math.pow(d.getX(), 2);
        }

        //Ecuacion normal

        m = (n * sumXY - sumX * sumY)/
                (n * sumX2 - Math.pow(sumX, 2));

        b = (sumY - m * sumX)/n;
        return new double[] {m,b};
    }

    public static String evalDatMSE(List<clsDataset> datos, double m, double b){
        String cad = "";
        double error = 0.0;

        for (int i = 0; i < datos.size(); i++){
            double yPred = m * datos.get(i).getX() + b;
            cad += String.format("Real: " + datos.get(i).getY() + " | Predicción: " + String.format("%.2f", yPred)+ "\n");
            error += Math.pow((datos.get(i).getY() - yPred), 2);
        }
        double mse = error/datos.size();

        cad += "\nMSE de prueba: " + String.format("%.2f", mse);
        return cad;
    }
}

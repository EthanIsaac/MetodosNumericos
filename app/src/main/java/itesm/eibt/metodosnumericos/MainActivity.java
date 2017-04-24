package itesm.eibt.metodosnumericos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

//RECURSOS
/* Crear la tabla de datos
LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
 */


public class MainActivity extends AppCompatActivity {

    private Button btnGauss;
    private Button btnDOS;
    private Button btnTRES;
    private Button btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargaMenu();
    }

    private void cargaMenu() {
        setContentView(R.layout.activity_main);
        btnGauss = (Button) findViewById(R.id.boton_gauss);
        btnGauss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cargaGauss();
            }
        });
        btnDOS = (Button) findViewById(R.id.boton_dos);
        btnDOS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cargaGrafico();
            }
        });
        btnTRES = (Button) findViewById(R.id.boton_tres);
        btnTRES.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });
    }

    private void cargaGrafico() {
        setContentView(R.layout.activity_grafico);
        btnMenu = (Button) findViewById(R.id.boton_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cargaMenu();
            }
        });
    }

    private void cargaGauss() {
        setContentView(R.layout.activity_gauss);
    }

    private void mostrarGrafico(LineGraphSeries<DataPoint> series) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
    }

    private Float[][] ordenar(Float[][] matriz)
    {
        int size = matriz.length;
        for(int i=0;i<size;i++)
        {
            if(matriz[i][i]==0)
            {
                for(int j=0;j<size;j++)
                {
                    if(matriz[j][i]!=0 && matriz[i][j]!=0)
                    {
                        Float temp;
                        for(int k=0;k<size;k++)
                        {
                            temp = matriz[i][k];
                            matriz[i][k]=matriz[j][k];
                            matriz[j][k]=temp;

                        }
                        break;
                    }
                }
            }
        }
        return matriz;
    }
}

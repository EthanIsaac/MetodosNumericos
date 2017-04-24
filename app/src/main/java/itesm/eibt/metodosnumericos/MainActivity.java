package itesm.eibt.metodosnumericos;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    private Button btnGenerar;
    private Button btnResuelve;
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
        //TODO DESPUÉS DE AQUÍ
        limpiarScroll();
        crearTabla(3);
        btnMenu = (Button) findViewById(R.id.boton_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cargaMenu();
            }
        });
        btnResuelve = (Button) findViewById(R.id.boton_resuelve);
        btnResuelve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Float[][] m = convierteTablaMatriz((TableLayout)findViewById(R.id.table)); // Obtengo la matriz flotante
                if(m!=null)
                {
                    resolverMatriz(m);
                }
            }
        });
        btnGenerar = (Button) findViewById(R.id.boton_generar);
        btnGenerar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                limpiarScroll();
                EditText size   = (EditText)findViewById(R.id.edit_size);
                crearTabla(Integer.parseInt(size.getText().toString()));
            }
        });
    }

    private void resolverMatriz(Float[][] m) {
        int size = m.length;
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scroll);
        TableLayout tabla = (TableLayout) scroll.getChildAt(0);
        limpiarScroll();
        scroll.removeViewAt(0);
        scroll.addView(tabla);
        m = ordenar(m); // Ordena la matriz
        scroll.addView(generaImprimible(m)); // Imprime la matriz ordenada
        m = dividirEnPivote(m,0); // Divide la primera línea entre el pivote
        scroll.addView(generaImprimible(m)); // Imprime
        for(int i=1;i<size;i++)
        {
            for(int j=0;j<i;j++)
            {
                if(m[i][j]!=0)
                {
                    Float pivote = m[i][j];
                    for(int k=0;k<=size;k++)
                    {
                        m[i][k]=m[i][k]-(m[j][k]*pivote);
                    }
                    scroll.addView(generaImprimible(m));
                }
            }
            if(m[i][i]!=1)
            {
                m = dividirEnPivote(m,i);
                scroll.addView(generaImprimible(m));
            }
        }
        for(int i=size-2;i>=0;i--)
        {
            for(int j=size-1;j>i;j--)
            {
                if(m[i][j]!=0)
                {
                    Float pivote = m[i][j];
                    for(int k=0;k<=size;k++)
                    {
                        m[i][k]=m[i][k]-(m[j][k]*pivote);
                    }
                    scroll.addView(generaImprimible(m));
                }
            }
        }
    }

    private Float[][] dividirEnPivote(Float[][] matriz, int linea) {
        int size = matriz.length;
        Float pivote = matriz[linea][linea];
        for(int i=0;i<=size;i++)
        {
            matriz[linea][i]=matriz[linea][i]/pivote;
        }
        return matriz;
    }

    private Float[][] convierteTablaMatriz(TableLayout tabla) {
        int filas = tabla.getChildCount();
        int columnas = filas+1;
        Float[][] matriz = new Float[filas][columnas];
        for(int i=0;i<filas;i++)
        {
            for(int j=0;j<columnas;j++)
            {

                EditText value = (EditText)((TableRow)tabla.getChildAt(i)).getChildAt(j);
                if(value.getText().toString().equals(""))
                {
                    /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setMessage("No dejes celdas en blanco!");
                    dialogBuilder.setCancelable(true).setTitle("ALERTA: CELDAS VACÍAS");
                    dialogBuilder.create().show();*/
                    return null;
                }
                matriz[i][j] = Float.parseFloat(value.getText().toString());
            }
        }
        return matriz;
    }

    private void limpiarScroll() {
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scroll);
        scroll.removeAllViews();
        scroll.setOrientation(LinearLayout.VERTICAL);
        TableLayout tabla = new TableLayout(this);
        tabla.setId(R.id.table);
        scroll.addView(tabla);
    }

    private void crearTabla(int size) {
        TableLayout tabla = (TableLayout) findViewById(R.id.table);;
        tabla.removeAllViews();
        for(int i=0;i<size;i++)
        {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            for(int j=0;j<=size;j++)
            {
                EditText et = new EditText(this);
                et.setWidth(150);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setTextSize(15);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                if(j==size)
                {
                    et.setBackgroundColor(Color.GRAY);
                }
                tr.addView(et);
            }
            tabla.addView(tr);
        }
    }

    private void mostrarGrafico(LineGraphSeries<DataPoint> series) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
    }

    private TableLayout generaImprimible(Float[][] matriz)
    {
        int size = matriz.length;
        TableLayout tabla = new TableLayout(this);
        for(int i=-1;i<size+1;i++)
        {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            for(int j=0;j<=size;j++)
            {
                TextView tv = new TextView(this);
                tv.setWidth(150);
                tv.setTextSize(15);
                if((i>-1)&&(i<size))
                {
                    tv.setText(""+matriz[i][j]);
                    if(j==size)
                    {
                        tv.setBackgroundColor(Color.GRAY);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                tr.addView(tv);
            }
            tabla.addView(tr);
        }
        return tabla;

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

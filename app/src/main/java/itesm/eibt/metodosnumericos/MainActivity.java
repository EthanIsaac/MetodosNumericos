package itesm.eibt.metodosnumericos;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

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

    private boolean isMenuUp;
    private Button btnGauss;
    private Button btnDivisionSinteticaDoble;
    private Button btnMinimosInterpolacion;
    private Button btnMenu;
    private Button btnGenerar;
    private Button btnResuelve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargaMenu();
    }

    @Override
    public void onBackPressed() {
        if(!isMenuUp)
        {
            cargaMenu();
        }
        else
        {
            finish();
        }
    }

    private void cargaMenu() {
        isMenuUp = true;
        setContentView(R.layout.activity_main);
        btnGauss = (Button) findViewById(R.id.boton_gauss);
        btnGauss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cargaGauss();
            }
        });
        btnDivisionSinteticaDoble = (Button) findViewById(R.id.boton_divisionsinteticadoble);
        btnDivisionSinteticaDoble.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { cargaDivisionSinteticaDoble();
            }
        });
        btnMinimosInterpolacion = (Button) findViewById(R.id.boton_interpolacion);
        btnMinimosInterpolacion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { cargaInterpolacion(); }
        });
    }

    // MÉTODOS INTERPOLACIÓN

    private void cargaInterpolacion()  {
        setContentView(R.layout.activity_interpolacion);
        // TODO DESPUÉS DE AQUÍ
        isMenuUp = false;
        limpiarScroll();
        crearTablaInterpolacion(3);
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
                ArrayList datos = obtenerDatosInterpolacion((TableLayout)findViewById(R.id.table));
                if(datos!=null)
                {
                    Float[][] m = new Float[3][3];
                    m[0][0] = 1.0f;
                    m[0][1] = 1.0f;
                    m[0][2] = 0.0f;
                    m[1][0] = 1.0f;
                    m[1][1] = 0.0f;
                    m[1][2] = 1.0f;
                    m[2][0] = 0.0f;
                    m[2][1] = 1.0f;
                    m[2][2] = 0.0f;
                    calcularPolinomioInterpolacion(datos);
                    calculaInversa(m);
                }
            }
        });
        btnGenerar = (Button) findViewById(R.id.boton_generar);
        btnGenerar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText size = (EditText)findViewById(R.id.edit_size);
                int sizeNum = 0;
                try{
                    sizeNum = Integer.parseInt(size.getText().toString());
                }catch (Exception e) {  }
                if(sizeNum!=0)
                {
                    limpiarScroll();
                    crearTablaInterpolacion(sizeNum);
                }
                else
                {
                    muestraAlerta("¡INGRESA UNA CANTIDAD DE DATOS VÁLIDA (MAYOR A CERO)!");
                }
            }
        });
    }

    private void calcularPolinomioInterpolacion(ArrayList datos) {

    }

    private Float[] calculaCoeficientes(Float[][] matrizX, Float[] matrizY)   {
        Float[] coeficientes = null;
        return coeficientes;
    }

    private Float[][] calculaInversa(Float [][] matriz)   {
        int n=matriz.length;  //dimensión de la matriz
        Float[][] a = matriz;
        Float[][] b = new Float[n][n];   //matriz de los términos independientes
        Float[][] c = new Float[n][n];   //matriz de las incógnitas
//matriz unidad
        for(int i=0; i<n; i++){
            b[i][i]=1.0f;
        }
//transformación de la matriz y de los términos independientes
        for(int k=0; k<n-1; k++){
            for(int i=k+1; i<n; i++){
//términos independientes
                for(int s=0; s<n; s++){
                    b[i][s]-=a[i][k]*b[k][s]/a[k][k];
                }
//elementos de la matriz
                for(int j=k+1; j<n; j++){
                    a[i][j]-=a[i][k]*a[k][j]/a[k][k];
                }
            }
        }
//cálculo de las incógnitas, elementos de la matriz inversa
        for(int s=0; s<n; s++){
            c[n-1][s]=b[n-1][s]/a[n-1][n-1];
            for(int i=n-2; i>=0; i--){
                c[i][s]=b[i][s]/a[i][i];
                for(int k=n-1; k>i; k--){
                    c[i][s]-=a[i][k]*c[k][s]/a[i][i];
                }
            }
        }
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scroll);
        scroll.addView(imprimeMatrizCuadrada(c));
        return c;
    }

    private ArrayList obtenerDatosInterpolacion(TableLayout tabla) {
        ArrayList datos = new ArrayList();
        int filas = tabla.getChildCount();
        for(int i = 1; i<filas;i++)
        {
            TableRow dato = (TableRow)tabla.getChildAt(i);
            String x = ((EditText)dato.getChildAt(0)).getText().toString();
            String y = ((EditText)dato.getChildAt(1)).getText().toString();
            if(x.equals("")||y.equals(""))
            {
                muestraAlerta("¡NO DEJES CELDAS EN BLANCO!");
                return null;
            }
            datos.add(new parDeDatos(Float.parseFloat(x),Float.parseFloat(y)));
        }
        return datos;
    }

    private void crearTablaInterpolacion(int size){
        TableLayout tabla = (TableLayout) findViewById(R.id.table);;
        tabla.removeAllViews();
        TableRow titulos = new TableRow(this);
        titulos.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView xTitulos = new TextView(this);
        xTitulos.setText("X");
        xTitulos.setWidth(150);
        xTitulos.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        xTitulos.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            xTitulos.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        xTitulos.setBackgroundColor(Color.GRAY);
        titulos.addView(xTitulos);
        TextView yTitulos = new TextView(this);
        yTitulos.setText("Y");
        yTitulos.setWidth(150);
        yTitulos.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        yTitulos.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            yTitulos.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        yTitulos.setBackgroundColor(Color.GRAY);
        titulos.addView(yTitulos);
        tabla.addView(titulos);
        for(int i=0;i<size;i++)
        {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            EditText x = new EditText(this);
            x.setWidth(150);
            x.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            x.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                x.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tr.addView(x);
            EditText y = new EditText(this);
            y.setWidth(150);
            y.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            y.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                y.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tr.addView(y);
            tabla.addView(tr);
        }
    }

    private class parDeDatos extends Object{
        private Float x;
        private Float y;
        public parDeDatos(Float x, Float y){
            this.x = x;
            this.y = y;
        }
        public void setX(Float x){
            this.x = x;
        }
        public void setY(Float y){
            this.y = y;
        }
        public Float getX(){
            return this.x;
        }
        public Float getY(){
            return this.y;
        }
    }

    private TableLayout imprimeMatrizCuadrada(Float[][] matriz)  {
        int size = matriz.length;
        TableLayout tabla = new TableLayout(this);
        for(int i=0;i<size;i++)
        {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            for(int j=0;j<size;j++)
            {
                TextView tv = new TextView(this);
                tv.setWidth(150);
                tv.setTextSize(15);
                tv.setText(""+matriz[i][j]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                tr.addView(tv);
            }
            tabla.addView(tr);
        }
        return tabla;
    }

    // MÉTODOS DIVISIÓN SINTÉTICA

    private void cargaDivisionSinteticaDoble() {
        setContentView(R.layout.activity_divisionsinteticadoble);
        // TODO DESPUÉS DE AQUÍ
        isMenuUp = false;
        limpiarScroll();
        crearTablaDivisionSintetica(2);
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
                EditText rr   = (EditText)findViewById(R.id.edit_rr);
                EditText ss   = (EditText)findViewById(R.id.edit_ss);
                Float rrNum = null;
                try{
                    rrNum = Float.parseFloat(rr.getText().toString());
                }catch (Exception e) {  }
                Float ssNum = null;
                try{
                    ssNum = Float.parseFloat(ss.getText().toString());
                }catch (Exception e) {  }
                ArrayList coeficientes = obtenerCoeficientesDivisionSinteticaDoble((TableLayout)findViewById(R.id.table));
                if(coeficientes!=null)
                {
                    if(rrNum!=null && ssNum!=null)
                    {
                        divisionTotal(coeficientes,rrNum,ssNum);
                    }
                    else
                    {
                        muestraAlerta("¡INGRESA UN VALOR PARA rr Y ss!");
                    }
                }

            }
        });
        btnGenerar = (Button) findViewById(R.id.boton_generar);
        btnGenerar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText size = (EditText)findViewById(R.id.edit_size);
                int sizeNum = 0;
                try{
                    sizeNum = Integer.parseInt(size.getText().toString());
                }catch (Exception e) {  }
                if(sizeNum!=0)
                {
                    limpiarScroll();
                    crearTablaDivisionSintetica(sizeNum);
                }
                else
                {
                    muestraAlerta("¡INGRESA UN GRADO DE POLINOMIO VÁLIDO (MAYOR A CERO)!");
                }
            }
        });
    }

    private ArrayList obtenerCoeficientesDivisionSinteticaDoble(TableLayout tabla)  {
        ArrayList coeficientes = new ArrayList();
        int filas = tabla.getChildCount();
        for(int i=filas-1;i>=0;i--)
        {
            int columnas = ((TableRow)tabla.getChildAt(i)).getChildCount();
            for(int j=columnas-2;j>=0;j-=2)
            {
                EditText value = (EditText)((TableRow)tabla.getChildAt(i)).getChildAt(j);
                if(value.getText().toString().equals(""))
                {
                    muestraAlerta("¡NO DEJES CELDAS EN BLANCO!");
                    return null;
                }
                coeficientes.add(Float.parseFloat(value.getText().toString()));
            }
        }
        return coeficientes;
    }

    private void crearTablaDivisionSintetica(int grado) {
        TableLayout tabla = (TableLayout) findViewById(R.id.table);
        tabla.removeAllViews();
        grado++;
        int contadorPotencias=0;
        int filas = grado/3;
        int columnas = 3;
        int columnasFinales = grado%3;
        if(columnasFinales!=0)
        {
            filas++;
        }
        for(int i=0;i<filas;i++)
        {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            if(i==filas-1&&columnasFinales!=0)
            {
                columnas = columnasFinales;
            }
            for(int j=0;j<columnas;j++)
            {
                EditText et = new EditText(this);
                et.setWidth(150);
                et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                et.setTextSize(15);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                TextView tv = new TextView(this);
                tv.setWidth(170);
                tv.setTextSize(15);
                tv.setText("*x ^ " + contadorPotencias + " + ");
                if(contadorPotencias==grado-1)
                {
                    tv.setText("*x ^ " + contadorPotencias);
                }
                contadorPotencias++;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                }
                tr.addView(et);
                tr.addView(tv);
            }
            tabla.addView(tr);
        }
    }

    private void divisionTotal(ArrayList a, Float rr, Float ss)  {
        // Limpio el scroll
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scroll);
        TableLayout tabla = (TableLayout) scroll.getChildAt(0);
        limpiarScroll();
        scroll.removeViewAt(0);
        scroll.addView(tabla);
        // Declaración de variables
        ArrayList b, c;
        int t;
        Float r, s, delta, dr, ds, tol;
        String valores;
        // Construccion de objetos
        b = a;
        t = a.size();
        r = rr;
        s = ss;
        tol = (float)Math.pow(10,-5);
        valores = "";
        // Cálculo
        while(t>3)
        {
            while(Math.abs(Float.parseFloat(b.get(t-1).toString()))>tol || Math.abs(Float.parseFloat(b.get(t-2).toString()))>tol)
            {
                b = divisionSinteticaDoble(a,r,s);
                c = divisionSinteticaDoble(b,r,s);
                delta = ((float)Math.pow(Float.parseFloat(c.get(t-3).toString()),2) - (Float.parseFloat(c.get(t-2).toString())*Float.parseFloat(c.get(t-4).toString())));
                dr = ((Float.parseFloat(c.get(t-4).toString())*Float.parseFloat(b.get(t-1).toString())) - (Float.parseFloat(c.get(t-3).toString())*Float.parseFloat(b.get(t-2).toString())))/delta;
                ds = ((Float.parseFloat(b.get(t-2).toString())*Float.parseFloat(c.get(t-2).toString())) - (Float.parseFloat(b.get(t-1).toString())*Float.parseFloat(c.get(t-3).toString())))/delta;
                r = r+dr;
                s = s+ds;
            }
            valores += solucionesDeX(1.0f,-r,-s);
            t-=2;
            a=b;
        }
        if(t==3)
        {
            valores += solucionesDeX(Float.parseFloat(b.get(0).toString()),Float.parseFloat(b.get(1).toString()),Float.parseFloat(b.get(2).toString()));
        }
        else
        {
            valores += solucionesDeX(0.0f,Float.parseFloat(b.get(0).toString()),Float.parseFloat(b.get(1).toString()));
        }
        TextView resultados = new TextView(this);
        resultados.setText("\nLas raices del polinomio son:\n" + valores);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            resultados.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        scroll.addView(resultados);
    }

    private String solucionesDeX(Float a, Float b, Float c)  {
        String soluciones = "";
        if(a!=0.0f)
        {
            Float discriminante, x1, x2;
            discriminante = (float)Math.pow(b,2)-(4*a*c);
            if(discriminante > 0)
            {
                x1 = (-b + (float)Math.sqrt(discriminante))/(2*a);
                x2 = (-b - (float)Math.sqrt(discriminante))/(2*a);
                soluciones += "\nx = " + x1 + ";\nx = " + x2 + ";";
            }
            else if(discriminante == 0)
            {
                x1 = -b/(2*a);
                soluciones += "\nx = " + x1 + ";";
            }
            else if(discriminante < 0)
            {
                soluciones += "\nx = " + (-b/(2*a)) + " + " + ((float)Math.sqrt(-discriminante)/(2*a)) + "i;";
                soluciones += "\nx = " + (-b/(2*a)) + " - " + ((float)Math.sqrt(-discriminante)/(2*a)) + "i;";
            }
        }
        else
        {
            soluciones += "\nx = " + (-c/b) + ";";
        }
        return soluciones;
    }

    private ArrayList divisionSinteticaDoble(ArrayList a, Float r, Float s)  {
        ArrayList polinomioResultante = new ArrayList();
        Float rr=0.0f, rs=0.0f, t;
        for(int i = 0; i < a.size();i++)
        {
            if(i==0)
            {
                rr = Float.parseFloat(a.get(0).toString());
                polinomioResultante.add(rr);
            }
            else if(i==1)
            {
                rs = rr;
                rr = Float.parseFloat(a.get(1).toString())+(rr*r);
                polinomioResultante.add(rr);
            }
            else
            {
                t = rr;
                rr = Float.parseFloat(a.get(i).toString())+(rr*r)+(rs*s);
                rs = t;
                polinomioResultante.add(rr);
            }
        }
        return polinomioResultante;
    }

    // MÉTODOS GAUSS JORDAN

    private void cargaGauss() {
        setContentView(R.layout.activity_gauss);
        // TODO DESPUÉS DE AQUÍ
        isMenuUp = false;
        limpiarScroll();
        crearTablaGauss(3);
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
                crearTablaGauss(Integer.parseInt(size.getText().toString()));
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
        m = ordenarMatriz(m); // Ordena la matriz
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
                        m[i][k]=m[i][k]-(m[j][k]*pivote); // Convierte en cero
                    }
                    scroll.addView(generaImprimible(m));
                }
            }
            if(m[i][i]!=1)
            {
                if(m[i][i]==0)
                {
                    TextView mensaje = new TextView(this);
                    mensaje.setText("No se puede resolver la matriz.");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        mensaje.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    scroll.addView(mensaje);
                    return;
                }
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
                    muestraAlerta("¡NO DEJES CELDAS EN BLANCO!");
                    return null;
                }
                matriz[i][j] = Float.parseFloat(value.getText().toString());
            }
        }
        return matriz;
    }

    private void crearTablaGauss(int size) {
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
                et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
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

    private TableLayout generaImprimible(Float[][] matriz)  {
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

    private Float[][] ordenarMatriz(Float[][] matriz) {
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

    // MÉTODOS MISCELÁNEA

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

    private void mostrarGrafico(LineGraphSeries<DataPoint> series) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
    }

    private void limpiarScroll() {
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scroll);
        scroll.removeAllViews();
        scroll.setOrientation(LinearLayout.VERTICAL);
        TableLayout tabla = new TableLayout(this);
        tabla.setId(R.id.table);
        scroll.addView(tabla);
    }

    private void muestraAlerta(String s)    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Guill
 */
public class Analizador extends javax.swing.JFrame {
    /**
     * Creates new form Analizador
     */
    ArrayList<String> tipo = new ArrayList<>();
    ArrayList<String> tipotoken = new ArrayList<>();
    ArrayList<Integer> numlinea = new ArrayList<>();
    ArrayList<Integer> columna = new ArrayList<>();
    ArrayList<String> line= new ArrayList<>();
    ArrayList<Integer> errores=new ArrayList<>();
    DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel dtm2 = new DefaultTableModel();
    int numlineas;
    boolean banderaSin=true,banderaLex=true;
   
   //Cambiar las rutas de escritura y leectura del archivo de ser necesario
    public Analizador() {
        initComponents();
        setLocationRelativeTo(this);
        this.setResizable(false);
        this.setSize(680,320);
        mostrar();
        
        dtm2.addColumn("Error");
        dtm2.addColumn("Linea");
        jTable2.setModel(dtm2);
                 
                
    }
    private ArrayList<Token> lex(String input) {
       
        final ArrayList<Token> tokens = new ArrayList<Token>();
        int ic = 0;
        int ic2=0;
        String buffcont="";
        final StringTokenizer st = new StringTokenizer(input);        
        String palabra="";
        while (st.hasMoreTokens()) {
            
            palabra = st.nextToken();
            
            for(ic=ic2;ic<=numlineas;ic++){
                buffcont=buffcont+palabra;
                if(line.get(ic2).contains(buffcont)){
                   //System.out.println("entro al positivo");
                    break;
                }
               //System.out.println("buffer: "+buffcont+"\n linea: "+line.get(ic2));
                if(!line.get(ic2).contains(buffcont)){
                    //System.out.println("entro al nega"+ic2);
                    ic2=ic+1;
                    buffcont="";
                    //System.out.println("nuevo valor ic2"+ic2);  
                } 
            }
            buffcont=buffcont+" ";
            boolean matched = false;

            for (Tipo tokenTipo : Tipo.values()) {
                Pattern patron = Pattern.compile(tokenTipo.patron); 
                Matcher matcher = patron.matcher(palabra);
                //System.out.println("Tipo; "+patron+"Token; "+palabra);
               //System.out.println(palabra+" jjfj "+ic2+" hjjf "+numeroColumna(ic2,palabra));

                if (matcher.matches()) {
                    
                    Token tk = new Token();
                    tk.setTipo(tokenTipo);
                    tk.setValor(palabra);
                    tk.setLinea(ic2);
                    tokens.add(tk);
                    
                    matched = true;
                    
                    break;
                }

            }
            if(!matched){
                banderaLex=false;
                this.setSize(850,this.getHeight());
                dtm2.addRow(new Object[] {"  "+palabra,"  "+ic2});
            }
                


        }
        return tokens;
    }
    private boolean validaPar(String cadena){
        Pila pila = new Pila();
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == '(' || cadena.charAt(i) == '[' || cadena.charAt(i) == '{') {
                pila.Insertar(cadena.charAt(i));
            } else {

                if (cadena.charAt(i) == ')') {

                    if (pila.extraer() != '(') {
                        return false;
                    }

                } else {

                    if (cadena.charAt(i) == ']') {

                        if (pila.extraer() != '[') {
                            return false;
                        }

                    } else {

                        if (cadena.charAt(i) == '}') {
                            if (pila.extraer() != '{') {
                                return false;
                            }
                        }

                    }

                }

            }
        }
        return pila.PilaVacia();
    
    }
    //PARSER O ANALIZADOR SINTACTICO
    private void Parser(){ 
        String paren="";
        //RECORRE TODO EL PROGRAMA EN BUSCA DE LOS CARACTERES ({[]})
        for(int i=0;i<txtentrada.getText().length();i++){
            String text=txtentrada.getText();
            String cha = String.valueOf(text.charAt(i));
            Pattern CHSQ=Pattern.compile(TipoSin.CHARSQC.patron);
            Matcher MCHSQ=CHSQ.matcher(cha);
            if(MCHSQ.matches()){
                paren=paren+cha;   
            }
        }
        //Valida si hay balanceo
        if(!validaPar(paren)){
            banderaSin=false;
            TXTError.setText("Se encontro un error en el balanceo de aberturas y cerraduras\nCompruebalo!\n");
            this.setSize(this.getWidth(),520);
        }
        boolean band=true;
        for(int i=0;i<line.size();i++){
            //Generamos los matchers de cada tipo
            Pattern PD=Pattern.compile(TipoSin.PACKAGED.patron);
            Matcher MPD=PD.matcher(line.get(i));
            Pattern CD=Pattern.compile(TipoSin.CLASS.patron);
            Matcher MCD=CD.matcher(line.get(i));
            Pattern MaD=Pattern.compile(TipoSin.MAIN.patron);
            Matcher MMaD=MaD.matcher(line.get(i));
            Pattern MD=Pattern.compile(TipoSin.METHOD.patron);
            Matcher MMD=MD.matcher(line.get(i));
            Pattern Exp=Pattern.compile(TipoSin.EXP.patron);
            Matcher MExp=Exp.matcher(line.get(i));
            Pattern Ret=Pattern.compile(TipoSin.RETURNST.patron);
            Matcher MRet=Ret.matcher(line.get(i));
            Pattern VD=Pattern.compile(TipoSin.VARDEC.patron);
            Matcher MVD=VD.matcher(line.get(i));
            Pattern PR=Pattern.compile(TipoSin.PRINT.patron);
            Matcher MPR=PR.matcher(line.get(i));
            Pattern CHSQ=Pattern.compile(TipoSin.CHARSQC.patron);
            Matcher MCHSQ=CHSQ.matcher(line.get(i));
            if(MPD.matches()){
                continue;   
            }
            if(MCD.matches()){
                continue;
            }
            if (MMaD.matches()){
                continue;
            }
            if (MMD.matches()){
                continue;
            }
            if(MExp.matches()){
                continue;
            }
            if(MCHSQ.matches()){
                continue;
            }
            if(MPR.matches()){
                continue;
            }
            if(MVD.matches()){
                continue; 
            }
            if(MRet.matches()){
                continue;
            }
            else{
                //En caso de que no haga match se hace una bandera sintactica falsa y se agrega a una tabla de errores
                band=false;
                banderaSin=false;
                this.setSize(this.getWidth(),520);
                errores.add(i);
                continue;
            }
            

        }
        //AGREGAR A UNA TABLA DE ERRORES
        for(int i=0;i<errores.size();i++){
            int lnr=errores.get(i);
            String area=TXTError.getText();
            area=area+"Se encontró un error en la linea: < "+(lnr+1)+" > \n";
            area=area+line.get(lnr)+"\n";
            TXTError.setText(area);
           
        }
       

        
    }
    
    /*CODIGO NO USADO*/{
   /* public int numeroColumna(int ic2,String palabra){
        int counter=0;
        
        ArrayList<String> columna=new ArrayList<String>();
        StringTokenizer st=new StringTokenizer(line.get(ic2));
        while(st.hasMoreElements()){
            columna.add(st.nextToken());
            counter++;
        }
        for(int i=0;i<columna.size();i++){
            System.out.println(palabra);
            System.out.println(columna.get(i));
            if(columna.get(i).equals(palabra)){
                
                return i;
            }
        }
        
        return counter;
    }
    
    /*
   private ArrayList<String> lex2(String input) {
       String buffer="";
       ArrayList<String> palabras= new ArrayList<String>();
       ArrayList<String> tkns=new ArrayList<>(Arrays.asList(input.split("((\\s)+)?(\\p{Punct}+)?")));
       for(int i=0;i<tkns.size();i++){
           System.out.println(tkns.get(i));
       }
       

      
       
        return null;
        
    }/*

        while (st.hasMoreTokens()) {
            
            
            boolean matched = false;

            for (Tipo tokenTipo : Tipo.values()) {
                Pattern patron = Pattern.compile(tokenTipo.patron);
                Matcher matcher = patron.matcher(palabra);
                if (matcher.matches()) {
                    Token tk = new Token();
                    tk.setTipo(tokenTipo);
                    tk.setValor(palabra);
                    tokens.add(tk);
                    matched = true;
                    break;
                }
            }
            

            if (matched=false) {
                //throw new RuntimeException("Se encontró un token invalido.");
                //System.err.println("Error lexico: " + palabra);
                errores.add(palabra);
                for (int i = 0; i < errores.size(); i++) {

                    dtm2.addRow(new Object[]{errores.get(i)});

                }
                errores.clear();

            }

        }

        return tokens;
    }
       */}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnanalizar = new javax.swing.JButton();
        btnsubir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtentrada = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TXTError = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnanalizar.setText("analizar");
        btnanalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnanalizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnanalizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        btnsubir.setText("subir archivo");
        btnsubir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsubirActionPerformed(evt);
            }
        });
        getContentPane().add(btnsubir, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 11, -1, -1));

        txtentrada.setColumns(20);
        txtentrada.setEditable(false);
        txtentrada.setRows(5);
        jScrollPane1.setViewportView(txtentrada);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 73, 214, 194));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 73, 420, 194));

        jButton1.setText("Limpiar Tabla");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 11, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 367, 151, -1));

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jButton3.setText("Descargar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 50, -1, -1));

        TXTError.setEditable(false);
        TXTError.setColumns(20);
        TXTError.setRows(5);
        jScrollPane2.setViewportView(TXTError);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 630, 170));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Token", "Linea"
            }
        ));
        jScrollPane4.setViewportView(jTable2);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 70, 160, 200));

        pack();
    }// </editor-fold>//GEN-END:initComponents
   int j = 0;
    int i = 0;
    //ACCION DEL BOTON ANALIZAR//
    private void btnanalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnanalizarActionPerformed
        // TODO add your handling code here:

        if (txtentrada.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Digite caracteres en el campo");
        }
        else{
            //Genera una lista de tokens con el texto en el campo
            ArrayList<Token> tokens = lex(txtentrada.getText());
            //Ya que los obtiene los agrega a la tabla de tokens
                for (Token token : tokens) {
                String a = ("<" + token.getTipo()+">");
                String b = ("" + token.getValor());
                int c = ( token.getLinea()+1);
                tipo.add(a);
                tipotoken.add(b);
                numlinea.add(c);
                dtm.addRow(new Object[]{tipo.get(j), tipotoken.get(j),numlinea.get(j).toString()});
                j++;

            }
        }
        if(!txtentrada.getText().equals("")){
          Parser();
        }
         if(banderaSin && banderaLex){
           JOptionPane.showMessageDialog(null, "No se encontraron errores lexicos y sintacticos ");
        }
        if(!banderaSin && banderaLex){
            JOptionPane.showMessageDialog(null, "Se encontraron errores sintacticos ");
        }
        if(banderaSin && !banderaLex){
            JOptionPane.showMessageDialog(null, "Se encontraron erres lexicos ");
        }
        if(!banderaSin && !banderaLex){
            JOptionPane.showMessageDialog(null, "Se encontraron errores lexicos y sintacticos");
        }


    }//GEN-LAST:event_btnanalizarActionPerformed
    //METODO PARA ABRIR EL ARCHIVO CON LA GRAMATICA//
    public void abrir() {
        FileReader fr = null;
        BufferedReader bf = null;
        File archivo = null;
        try {
            archivo = new File("C:\\Users\\Guill\\Desktop\\Analizador\\src\\analizador\\Prueba.java");
            fr = new FileReader(archivo);
            bf = new BufferedReader(fr);
            String linea;
            String lineaVieja;
            while ((linea = bf.readLine()) != null) {
                numlineas++;
                lineaVieja=txtentrada.getText();
                String lineanueva=lineaVieja+linea+"\n";
                txtentrada.setText(lineanueva);
                line.add(linea);
               
                
            }
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Archivo no encontrado");
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    //DESCARAGAR LOS DATOS EN UN ARCHIVO//
    public void descargaDatos()throws Exception{
        boolean band=false;
        BufferedWriter bfw = new BufferedWriter(new FileWriter("C:\\Users\\Guill\\Desktop\\Analizador\\src\\analizador\\datos.txt"));
        /***** IMPRESION DE LA PRIMER TABLA ****/
        for(int i = 0 ; i < jTable1.getColumnCount() ; i++)
        {
            bfw.write(jTable1.getColumnName(i));
            bfw.write("\t\t");
        }
        for (int i = 0 ; i < jTable1.getRowCount(); i++){

            bfw.newLine();
            for(int j = 0 ; j < jTable1.getColumnCount();j++)
            {
                bfw.write((String)(jTable1.getValueAt(i,j))); 
                
                bfw.write("\t\t");
            }
        }
        bfw.write("\n\n\n");
        //****** IMPRESION TABLA DE ERRORES ******//
        for(int i = 0 ; i < jTable2.getColumnCount() ; i++)
        {
            if(dtm2.getRowCount()!=0){
            bfw.write(jTable2.getColumnName(i));
            bfw.write("\t\t");
            }
        }
        for (int i = 0 ; i < jTable2.getRowCount(); i++){

            bfw.newLine();
            for(int j = 0 ; j < jTable2.getColumnCount();j++)
            {
                bfw.write((String)(jTable2.getValueAt(i,j))); 
                
                bfw.write("\t\t");
            }
        }
        bfw.write("\n\n\n");
        bfw.write(TXTError.getText());
        bfw.close();
    } 
   //MOSTRAR LA TABLA DE TOKENS//
    public void mostrar() {

        try {
            dtm.addColumn("TIPO");
            dtm.addColumn("LEXEMA");
            dtm.addColumn("Linea");
            jTable1.setModel(dtm);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "error mostrar" + ex);
        }
    }

    private void btnsubirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsubirActionPerformed
        // TODO add your handling code here:
        abrir();

    }//GEN-LAST:event_btnsubirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int a = dtm.getRowCount();
       
        for (int i = 0; i < a; i++) {
            dtm.removeRow(0);  
        }
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        txtentrada.setText("");
        numlineas=0;
        numlinea.clear();
        line.clear();;
        this.setSize(700,320);
        TXTError.setText("");
        tipo.clear();
        tipotoken.clear();
        columna.clear();
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            descargaDatos();
        } catch (Exception ex) {
            Logger.getLogger(Analizador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Analizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Analizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Analizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Analizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Analizador().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea TXTError;
    private javax.swing.JButton btnanalizar;
    private javax.swing.JButton btnsubir;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea txtentrada;
    // End of variables declaration//GEN-END:variables
}

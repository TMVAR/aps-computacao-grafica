package aps_computacao_grafica;



import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 *
 * @author siabr
 */
public class Cena implements GLEventListener, KeyListener{
    private float angulo = 0;
    private float tx = 0;
    private float sx = 1;
    
    private boolean telaInicial;
    private boolean pause;
    private int level;
    
    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
    }

    @Override
    public void display(GLAutoDrawable drawable) {  
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();                
        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);        
        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);       
        gl.glLoadIdentity(); //lê a matriz identidade
        
        /*
            desenho da cena        
        *
        */
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        gl.glColor3f(1,0,0); //cor branca        
        
        gl.glRotatef(angulo, 0, 0, 1);
        gl.glTranslatef(tx, tx, 0);
        gl.glScalef(sx, sx, 1);
                
        //desenha um retangulo
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(-0.5f, -0.5f);
            gl.glVertex2f(0.5f, -0.5f);
            gl.glVertex2f(0.5f, 0.5f);
            gl.glVertex2f(-0.5f, 0.5f);
        gl.glEnd();    


        gl.glFlush();      
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {    
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();        
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);      
        gl.glLoadIdentity(); //lê a matriz identidade
        //projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-1,1,-1,1,-1,1);
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        System.out.println("Reshape: " + width + ", " + height);
    }    
       
    @Override
    public void dispose(GLAutoDrawable drawable) {}        

    @Override
    public void keyPressed(KeyEvent e) {         
        switch(e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            //........
        }
        
        switch(e.getKeyChar()){
            case 'T':
                tx += 0.1f;
                break;
            case 't':
                tx -= 0.1f;
                break;
            case 'E':
                sx += 0.1f;
                break;
            case 'e':
                sx -= 0.1f;
                break;
            case 'r':
                angulo += 45.0f;
                break;
            
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}

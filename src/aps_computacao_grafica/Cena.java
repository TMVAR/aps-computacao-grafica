package aps_computacao_grafica;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import static com.jogamp.opengl.GL.GL_POINTS;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT; //primitivas 3D
import java.text.NumberFormat;
import java.util.Random;
import textura.Textura;

/**
 *
 * @author siabr
 */
public class Cena implements GLEventListener, KeyListener {

    Random rand = new Random();
    private float angulo = 0;
    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private int tonalizacao = GL2.GL_SMOOTH;
    float luzR = 0.2f, luzG = 0.2f, luzB = 0.2f;
    private float incAngulo = 0;

    // Atributos    
    private float limite;
    float xMin, xMax, yMin, yMax, zMin, zMax;
    private boolean triangulo = false;
    private boolean primeiraReta = true;
    private float inicioY = 0;
    private float atualY = 0;
    private float finalY = 0;
    private float inicioX = 0;
    private float atualX = 0;
    private float finalX = 0;
    private float barraX = 0;

    private boolean incrementaX;
    private boolean incrementaY;
    private boolean decrementaX;
    private boolean decrementaY;

    private boolean inicio = true;
    private boolean pause = false;

    private int valorIncrementoX;
    private int valorIncrementoY;
    private int valorDecrementoX;
    private int valorDecrementoY;

    //Referencia para classe Textura
    private Textura textura = null;
    //Quantidade de Texturas a ser carregada
    private int totalTextura = 8;

    //Constantes para identificar as imagens
    public static final String FACE1 = "imagens/dado/1_dado.png";
    public static final String FACE2 = "imagens/dado/2_dado.png";
    public static final String FACE3 = "imagens/dado/3_dado.png";
    public static final String FACE4 = "imagens/dado/4_dado.png";
    public static final String FACE5 = "imagens/dado/5_dado.png";
    public static final String FACE6 = "imagens/dado/6_dado.png";
    public static final String BOLA = "imagens/pong/bola-tenis.jpg";
    public static final String BARRA = "imagens/pong/barra.jfif";

    private int filtro = GL2.GL_LINEAR; ////GL_NEAREST ou GL_LINEAR
    private int wrap = GL2.GL_REPEAT;  //GL.GL_REPEAT ou GL.GL_CLAMP
    private int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND    

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        GL2 gl = drawable.getGL().getGL2();

        angulo = 0;
        incAngulo = 0;
        limite = 1;

        //Cria uma instancia da Classe Textura indicando a quantidade de texturas
        textura = new Textura(totalTextura);

        //habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        gl = drawable.getGL().getGL2();
        glut = new GLUT(); //objeto da biblioteca glut
        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 0);
        //limpa a janela com a cor especificada
        //limpa o buffer de profundidade
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); //lê a matriz identidade
        if (inicio) {
            desenhaTextoGrande(gl, -5, 0, "PONG");
            desenhaTextoPequeno(gl, -8, -20, "Aperte Enter para iniciar");
        } else {
            if (pause) {
                desenhaTextoGrande(gl, 0, 0, "PAUSE");
            } else {

                /*
            desenho da cena        
        *
                 */
                gl.glColor3f(1.0f, 1.0f, 1.0f);

                // criar a cena aqui....
                iluminacaoAmbiente();
                ligaLuz();
                //não é geração de textura automática
                textura.setAutomatica(false);

                //configura os filtros
                textura.setFiltro(filtro);
                textura.setModo(modo);
                textura.setWrap(wrap);

                animation();

                gl.glFlush();
            }

        }
    }

    public void desenhaTextoGrande(GL2 gl, int x, int y, String frase) {
        glut = new GLUT(); //objeto da biblioteca glut
        gl.glRasterPos2f(x, y);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, frase);
    }

    public void desenhaTextoPequeno(GL2 gl, int x, int y, String frase) {
        glut = new GLUT(); //objeto da biblioteca glut
        gl.glRasterPos2f(x, y);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, frase);
    }

    public void bola() {

        textura.gerarTextura(gl, BOLA, 0);
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(-2.0f, -2.0f, 0.0f);

        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(2.0f, -2.0f, 0.0f);

        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(2.0f, 2.0f, 0.0f);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-2.0f, 2.0f, 0.0f);

        gl.glEnd();
        textura.desabilitarTextura(gl, 0);

    }

    public void barra() {
        textura.gerarTextura(gl, BARRA, 0);
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(-10.0f, -90.0f, 0.0f);

        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(10.0f, -90.0f, 0.0f);

        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(10.0f, -85.0f, 0.0f);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-10.0f, -85.0f, 0.0f);

        gl.glEnd();
        textura.desabilitarTextura(gl, 0);

    }

    public void animation() {
        if (primeiraReta) {
            incrementaY();
            incrementaX();

            if (atualY == 100) {
                primeiraReta = false;
            }
        }

        if (verificaBolaRebatida()) {
            incrementaY = true;
            decrementaY = false;
            if (atualX > barraX) {
                incrementaX = true;
                decrementaX = false;
            } else if (atualX < barraX) {
                decrementaX = true;
                incrementaX = false;
            } else if (atualX == barraX) {
                incrementaX = false;
                decrementaX = false;
            }

        }

        /*if (verificaBolaRebatida()) {
            incrementaY = true;
            decrementaY = false;
            if (atualX != barraX && atualX > barraX) {
                if (atualX < 100 || atualX > -100) {

                } else if (incrementaX) {
                    incrementaX = true;
                    decrementaX = false;
                }
            } else if (atualX != barraX && atualX < barraX) {
                decrementaX = true;
                incrementaX = false;      
                
                
                if (atualX < 100 || atualX > -100) {
                    
                } else if (!incrementaX) {
                    incrementaX = true;
                    decrementaX = false;
                }
            } else {
                incrementaX = false;
                decrementaX = false;

            }

        }*/
        //Bateu na parede direita
        if (atualX >= 98 && atualX <= 105) {
            if (rand.nextBoolean()) {
                valorDecrementoX = -(rand.nextInt(2));
            } else {
                valorDecrementoX = rand.nextInt(2);
            }
            incrementaX = false;
            decrementaX = true;
        }

        //Bateu na parede esquerda
        if (atualX <= -98 && atualX >= -105) {
            if (rand.nextBoolean()) {
                valorIncrementoX = rand.nextInt(2);
            } else {
                valorIncrementoX = -(rand.nextInt(2));
            }
            incrementaX = true;
            decrementaX = false;
        }

        //Bateu na parede de cima
        if (atualY >= 98 && atualY <= 105) {
            if (rand.nextBoolean()) {
                valorDecrementoY = -(rand.nextInt(2));
            } else {
                valorDecrementoY = rand.nextInt(2);
            }
            incrementaY = false;
            decrementaY = true;
        }
        //Bateu na parede de baixo
        if (atualY <= -98 && atualY >= -105) {
            if (rand.nextBoolean()) {
                valorIncrementoY = rand.nextInt(2);
            } else {
                valorIncrementoY = -(rand.nextInt(2));
            }
            incrementaY = true;
            decrementaY = false;
        }
        if (incrementaX) {
            incrementaX();
        }
        if (incrementaY) {
            incrementaY();
        }
        if (decrementaX) {
            decrementaX();
        }
        if (decrementaY) {
            decrementaY();
        }

        //Limita o movimento da barra apenas as bordas
        if (barraX >= 90) {
            barraX--;
        }
        if (barraX <= -90) {
            barraX++;
        }
        //Movimenta a bola pelo cenario
        gl.glPushMatrix();
        gl.glTranslated(atualX, atualY, 0);
        bola();
        gl.glPopMatrix();
        movimentaBarra();
    }

    public boolean verificaBolaRebatida() {
        return atualX >= (barraX - 11) && atualX <= (barraX + 11) && atualY <= -83 && atualY >= -86;
    }

    public void incrementaY() {
        atualY = atualY + 1 + valorIncrementoY;
    }

    public void decrementaY() {
        atualY = atualY - 1 + valorDecrementoY;
    }

    public void incrementaX() {
        atualX = atualX + 1 + valorIncrementoX;
    }

    public void decrementaX() {
        atualX = atualX - 1 + valorDecrementoX;
    }

    public void movimentaBarra() {
        //Movimenta a barra no eixo X
        if (barraX < 90 && barraX > -90) {
            gl.glPushMatrix();
            gl.glTranslated(barraX, 0, 0);
            barra();
            gl.glPopMatrix();
        }
    }

    public void iluminacaoAmbiente() {
        float luzAmbiente[] = {1f, 1f, 0.2f, 1.0f}; //cor
        float posicaoLuz[] = {0.0f, 0.0f, 100.0f, 1.0f}; //pontual

        // define parametros de luz de número 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }

    public void ligaLuz() {
        // habilita a definição da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // habilita o uso da iluminação na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de número 0
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonalização GOURAUD (default)        
        gl.glShadeModel(tonalizacao);
    }

    public void desligaluz() {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
        //desliga a iluminacao
        gl.glDisable(GL2.GL_LIGHTING);
    }

    // Animacao de rotacao do Cubo
    private void rotacionarCubo() {
        angulo = angulo + incAngulo;
        if (angulo > 360f) {
            angulo = angulo - 360;
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        gl = drawable.getGL().getGL2();
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade
        //projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-100, 100, -100, 100, -100, 100);
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            //........
        }
        switch (e.getKeyChar()) {

            case KeyEvent.VK_ESCAPE:
                /*  Escape Key */
                System.exit(0);
                break;

            case 'j'://inicia animacao
                barraX = barraX - 6;
                break;

            case 'l': //para a animacao
                barraX = barraX + 6;
                break;
            case 'p':
                if (pause) {
                    pause = false;
                } else {
                    pause = true;
                }
            case KeyEvent.VK_ENTER:
                
                    inicio = false;
                
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
/*
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE1, 0);
              
        // Face FRONTAL
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(-30.0f, -30.0f, 30.0f);
        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(30.0f, -30.0f, 30.0f);
        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(30.0f, 30.0f, 30.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-30.0f, 30.0f, 30.0f);
        gl.glEnd();

        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 0);

        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE2, 1);

        // Face POSTERIOR
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(-30.0f, -30.0f, -30.0f);
        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(-30.0f, 30.0f, -30.0f);
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(30.0f, 30.0f, -30.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(30.0f, -30.0f, -30.0f);
        gl.glEnd();

        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 1);

        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE3, 2);

        // Face SUPERIOR
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(-30.0f, 30.0f, -30.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-30.0f, 30.0f, 30.0f);
        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(30.0f, 30.0f, 30.0f);
        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(30.0f, 30.0f, -30.0f);
        gl.glEnd();

        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 2);

        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE4, 3);

        // Face INFERIOR
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(-30.0f, -30.0f, -30.0f);
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(30.0f, -30.0f, -30.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(30.0f, -30.0f, 30.0f);
        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(-30.0f, -30.0f, 30.0f);
        gl.glEnd();
        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 3);

        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE5, 4);

        // Face LATERAL DIREITA
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(30.0f, -30.0f, -30.0f);
        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(30.0f, 30.0f, -30.0f);
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(30.0f, 30.0f, 30.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(30.0f, -30.0f, 30.0f);
        gl.glEnd();
        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 4);

        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE6, 5);
        // Face LATERAL ESQUERDA
        gl.glBegin(GL2.GL_QUADS);
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-30.0f, -30.0f, -30.0f);
        gl.glTexCoord2f(limite, 0.0f);
        gl.glVertex3f(-30.0f, -30.0f, 30.0f);
        gl.glTexCoord2f(limite, limite);
        gl.glVertex3f(-30.0f, 30.0f, 30.0f);
        gl.glTexCoord2f(0.0f, limite);
        gl.glVertex3f(-30.0f, 30.0f, -30.0f);
        gl.glEnd();
        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 5);

        gl.glPopMatrix();
        desligaluz();

        //Rotacao do Cubo
        rotacionarCubo();

        gl.glPopMatrix();*/

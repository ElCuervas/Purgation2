package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BarraDeVida {

    private TextureRegion[] vidaActual;
    private Texture textureAnimation;
    private Entidad entidad;
    private long maxVida;
    public BarraDeVida(Entidad entidad) {
        this.entidad = entidad;
        this.maxVida = entidad.vida;
        textureAnimation = new Texture(Gdx.files.internal("barra vida.png"));

        TextureRegion[][] tmp = TextureRegion.split(textureAnimation, textureAnimation.getWidth() / 6, textureAnimation.getHeight());
        vidaActual = new TextureRegion[6];
        System.arraycopy(tmp[0], 0, vidaActual, 0, 6);

    }

    public void dibujarBarraVida(Batch batch) {
        if (entidad.vida < maxVida) {
            double vidaPorcentaje = (double) entidad.vida / maxVida;
            int corazon=0;
            batch.draw(vidaActual[corazon], entidad.hitBox.x, entidad.hitBox.y + entidad.hitBox.height, entidad.hitBox.width, 5);
        }
    }
}
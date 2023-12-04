package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

public class BarraDeVida {

    private TextureRegion[] vidaActual;
    private Texture textureAnimation;
    private Entidad entidad;
    private long maxVida;
    private float modificacionX;
    private float modificacionY;
    private float multiplicador;
    public BarraDeVida(Entidad entidad,Texture barravida,float modificacionX,float modificacionY,float multiplicador) {
        this.entidad = entidad;
        this.maxVida = entidad.vida;
        this.modificacionX=modificacionX;
        this.modificacionY= modificacionY;
        this.multiplicador=multiplicador;
        textureAnimation = barravida;
        TextureRegion[][] tmp = TextureRegion.split(textureAnimation, textureAnimation.getWidth() / 6, textureAnimation.getHeight());
        vidaActual = new TextureRegion[6];
        System.arraycopy(tmp[0], 0, vidaActual, 0, 6);
    }

    public void dibujarBarraVida(Batch batch) {
        double vidaPorcentaje = 100*(double) entidad.vida / maxVida;
        int corazon=5;
        float x =entidad.hitBox.x+entidad.hitBox.height/2;
        float y =entidad.hitBox.y + entidad.hitBox.height;
        if (entidad.vida < maxVida) {
            if (vidaPorcentaje>=83.2){
                corazon=1;
            } else if (vidaPorcentaje<83.2 && vidaPorcentaje>=66.6) {
                corazon=2;
            } else if (vidaPorcentaje<66.6 && vidaPorcentaje>=50) {
                corazon=3;
            } else if (vidaPorcentaje<50 && vidaPorcentaje>=33.3) {
                corazon=4;
            } else if (vidaPorcentaje<33.3 && vidaPorcentaje>=16.6) {
                corazon=5;
            }
            batch.draw(vidaActual[corazon], x+modificacionX, y+modificacionY, (entidad.hitBox.width/2)*multiplicador, (entidad.hitBox.height/2)*multiplicador);
        }else{
            batch.draw(vidaActual[0], x+modificacionX, y+modificacionY, (entidad.hitBox.width/2)*multiplicador, (entidad.hitBox.height/2)*multiplicador);
        }
    }
}
package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BarraDeVida {

    private Animation<TextureRegion> spriteAnimation;
    private Texture textureAnimation;
    private Entidad entidad;
    private long maxVida;

    public BarraDeVida(Entidad entidad) {
        this.entidad = entidad;
        this.maxVida=entidad.vida;
        textureAnimation = new Texture(Gdx.files.internal("Player_animation.png"));

    }

    public void draw() {
    }
}
package com.purgation2.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals

class JugadorTest {
    private AssetManager asset;
    private Jugador playerTest;

    @BeforeAll
    void beforeAll(){
        asset = new AssetManager();
        asset.load("Player.png", Texture.class);
        asset.load("enemigo.png", Texture.class);
        asset.load("bala.png", Texture.class);
        asset.load("jefe.png", Texture.class);
        asset.load("charged.png.png", Texture.class);
        asset.finishLoading();
        playerTest = new Jugador(2500, 2500, 64 * 3, 64 * 3, ((Texture) asset.get("Player.png")), animador((Texture) asset.get("charged.png"), 7, 0.1f, 0));
    }

    @BeforeEach
    void setUp() {
        playerTest.reset()
    }

    @Test
    void testSetVidaMaxima() {
        playerTest.setVidaMaxima(400)
        assertEquals(500, playerTest.vidaMaxima)
    }

    @Test
    void testSetDamage() {
    }

    @Test
    void testSetVelocidad() {
    }

    static Animation<TextureRegion> animador(Texture imagen, int cantframe, float fotogramas, int comienzoframe) {
        TextureRegion[][] tmp = TextureRegion.split(imagen, imagen.getWidth() / cantframe, imagen.getHeight());
        TextureRegion[] animation = new TextureRegion[cantframe];
        int index = 0;
        for (int i = comienzoframe; i < cantframe; i++) {
            animation[index++] = tmp[0][i];
        }
        return new Animation<>(fotogramas, animation);
    }
}

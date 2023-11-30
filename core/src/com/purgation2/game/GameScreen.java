package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;
public class GameScreen implements Screen {

	final Setup game;
	OrthographicCamera camera;
	Sprite mapa;
	AssetManager asset;
	Sound DeadSound;
	Music SongMusic;
	Jugador player1;
	ArrayList<Bala> balasJugador;
	ArrayList<Bala> balasJefe;
	ArrayList<minion> minions;
	private double[] mejorasMinion;
	private double[] mejorasJugador;
	private double[] mejorasEnemigo;
	private double[] mejorasJefe;

	public GameScreen(final Setup game){
		this.game=game;
		asset = new AssetManager();
		asset.load("mapa.png", Texture.class);
		asset.load("player.png",Texture.class);
		asset.load("enemigo.png", Texture.class);
		asset.finishLoading();

		player1 = new Jugador(0,0,64,64,((Texture) asset.get("player.png")));

		mapa = new Sprite((Texture) asset.get("mapa.png"));
		mapa.setPosition(0,0);
		mapa.setSize(1000,1000);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(50*(h/w), 50);
		camera.position.set(600, 480, 0); //x player, y player
		camera.zoom=25;
		camera.update();

		DeadSound=Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
		SongMusic=Gdx.audio.newMusic(Gdx.files.internal("songfond.mp3"));
		SongMusic.setVolume(0.4f);
		SongMusic.setLooping(true);
	}
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		handleInput();
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		mapa.draw(game.batch);
		player1.sprite.draw(game.batch);
		game.batch.end();
		player1.controlls();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 30f;
		camera.viewportHeight = 30f * height/width;
		camera.update();
	}
	@Override
	public void show() {
		SongMusic.play();
	}
	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		asset.dispose();
		DeadSound.dispose();
	}



	public void generarEnemigo() {

	}

	public void generarJefe() {
	}

	public void generarMinion() {
	}
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-5, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(5, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -5, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 5, 0);
		}
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 1000 - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 1000 - effectiveViewportHeight / 2f);
	}
}
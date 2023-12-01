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
		asset.load("bala.png",Texture.class);
		asset.finishLoading();

		player1 = new Jugador( 5000 / 2 + 64*3/2,5000 / 2 + 64*3/2,64*3,64*3,((Texture) asset.get("player.png")));
		player1.velocidad=900;

		mapa = new Sprite((Texture) asset.get("mapa.png"));
		mapa.setPosition(0,0);
		mapa.setSize(5000,5000);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(50*(h/w), 50);
		camera.position.set(player1.hitBox.x+(player1.hitBox.width/2), player1.hitBox.y+(player1.hitBox.height/2), 0);//x player, y player
		camera.zoom=60;
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
		player1.renderizar(game.batch);//renderizado individual

		game.batch.end();

		if (player1.hitBox.x < 0)
			player1.hitBox.x = 0;
		if (player1.hitBox.x > 5000 - player1.hitBox.width)
			player1.hitBox.x = 5000 - player1.hitBox.width;
		if (player1.hitBox.y < 0)
			player1.hitBox.y = 0;
		if (player1.hitBox.y > 5000 - player1.hitBox.height)
			player1.hitBox.y = 5000 - player1.hitBox.height;

		player1.moverse();
		player1.atacar(camera,(Texture) asset.get("bala.png"));

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 40f;
		camera.viewportHeight = 40f * height/width;
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
			camera.translate(-11, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(11, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -11, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 11, 0);
		}
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 2000/camera.viewportWidth);

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 5000 - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 5000 - effectiveViewportHeight / 2f);
	}
}
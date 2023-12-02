package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;
public class GameScreen implements Screen {

	final Setup game;
	OrthographicCamera camera;
	Sprite mapa;
	Sprite borde;
	AssetManager asset;
	Sound DeadSound;
	Music SongMusic;
	Jugador player1;
	ArrayList<Enemigo> enemigos;
	Animation<TextureRegion> animationEnemigo;
	float stateTime;
	ArrayList<Bala> balasJugador;
	ArrayList<Bala> balasJefe;
	ArrayList<minion> minions;
	private double[] mejorasMinion;
	private double[] mejorasJugador;
	private double[] mejorasEnemigo;
	private double[] mejorasJefe;
	private final float tiempoEntreGeneraciones = 10f;
	private int cantidadEnemigosOleada=10;
	private float tiempoEntreGeneracionEnemigo = 3f;
	private float tiempoDesdeUltimaGeneracion;

	public GameScreen(final Setup game){
		this.game=game;
		asset = new AssetManager();
		asset.load("mapa.png", Texture.class);
		asset.load("borde.png",Texture.class);
		asset.load("Player.png",Texture.class);
		asset.load("enemigo.png", Texture.class);
		asset.load("bala.png",Texture.class);
		asset.finishLoading();

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				generarEnemigo(animador((Texture) asset.get("enemigo.png"),3,0.2f,0),10);
			}
		},0,5);

		player1 = new Jugador(2500+ 64*3/2,2500+ 64*3/2,64*3,64*3,((Texture) asset.get("Player.png")));

		player1 = new Jugador(2500+ 64*3/2,2500+ 64*3/2,64*3,64*3,(Texture) asset.get("Player.png"));
		player1.setVelocidad(900);

		enemigos=new ArrayList<>();
		stateTime=0f;

		mapa = new Sprite((Texture) asset.get("mapa.png"));
		mapa.setPosition(0,0);
		mapa.setSize(5000,5000);

		borde = new Sprite((Texture) asset.get("borde.png"));
		borde.setPosition(-1400,-1400);
		borde.setSize(8000,8000);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(50*(h/w), 50);
		camera.position.set(player1.hitBox.x+(player1.hitBox.width/2), player1.hitBox.y+(player1.hitBox.height/2), 0);//x player, y player
		camera.zoom=60;
		updateCameraPosition();

		DeadSound=Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
		SongMusic=Gdx.audio.newMusic(Gdx.files.internal("songfond.mp3"));
		SongMusic.setVolume(0.4f);
		SongMusic.setLooping(true);
		tiempoDesdeUltimaGeneracion=-5000;
	}
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		handleInput();
		stateTime+=Gdx.graphics.getDeltaTime();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		borde.draw(game.batch);
		mapa.draw(game.batch);
		player1.renderizar(game.batch,camera,(Texture) asset.get("bala.png"));//renderizado individual jugador

		for (Enemigo enemigo:enemigos) {//renderizado colectivo enemigos
			enemigo.renderizar(game.batch);
			enemigo.atacar((Texture) asset.get("bala.png"));
		}
		game.batch.end();
		limiteMapa();

		Iterator<Enemigo> iterEnemigos = enemigos.iterator();
		while (iterEnemigos.hasNext()) {
			Enemigo enemigoActivo = iterEnemigos.next();
			enemigoActivo.recibirDaño(player1);
			if (enemigoActivo.getVida() <= 0) {
				DeadSound.play();
				iterEnemigos.remove();
			} else if (enemigoActivo.hitBox.x<0||enemigoActivo.hitBox.x>5000||enemigoActivo.hitBox.y<0||enemigoActivo.hitBox.y>5000) {
				iterEnemigos.remove();
			}
		}
	}
	public void limiteMapa() {
		if (player1.hitBox.x < 0)
			player1.hitBox.x = 0;
		if (player1.hitBox.x > 5000 - player1.hitBox.width)
			player1.hitBox.x = 5000 - player1.hitBox.width;
		if (player1.hitBox.y < 0)
			player1.hitBox.y = 0;
		if (player1.hitBox.y > 5000 - player1.hitBox.height)
			player1.hitBox.y = 5000 - player1.hitBox.height;

		if (camera.position.x < 0)
			camera.position.x = 0;
		if (camera.position.x > 5000 - player1.hitBox.width)
			camera.position.x = 5000 - player1.hitBox.width;
		if (camera.position.y < 0)
			camera.position.y = 0;
		if (camera.position.y > 5000 - player1.hitBox.height)
			camera.position.y = 5000 - player1.hitBox.height;
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
	public void generarEnemigo(Animation<TextureRegion> animation, int cantidadEnemigos) {
		for (int i = 0; i < cantidadEnemigos; i++) {
			int spawn = MathUtils.random(-1600, 800);
			float spawnX;
			float spawnY;
			if(spawn<0){
				spawnX = player1.hitBox.x + spawn-800;
				spawnY = player1.hitBox.y + spawn-800;
			}else{
				spawnX = player1.hitBox.x + spawn+800;
				spawnY = player1.hitBox.y + spawn+800;
			}
			Enemigo nuevoEnemigo = new Enemigo(spawnX, spawnY, 64 * 3, 64 * 3, player1,animation);
			enemigos.add(nuevoEnemigo);
		}
	}
	private float crearCordenadaX(long margen) {
		float spawnX;
		do {
			spawnX = MathUtils.random(0, 5000);
		} while (spawnX > camera.position.x - margen - camera.viewportWidth / 2 && spawnX < margen +camera.position.x + camera.viewportWidth / 2);
		return spawnX;
	}

	private float crearCordenadaY(long margen) {
		float spawnY;
		do {
			spawnY = MathUtils.random(0, 5000);
		} while (spawnY > camera.position.y - margen - camera.viewportHeight / 2 && spawnY < margen +camera.position.y + camera.viewportHeight / 2);
		return spawnY;
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
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 5000);
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 5000);
		updateCameraPosition();
	}
	private void updateCameraPosition() {
		boolean playerOnLeftEdge = player1.hitBox.x <= camera.viewportWidth / 2f;
		boolean playerOnRightEdge = player1.hitBox.x >= 5000 - camera.viewportWidth / 2f;
		boolean playerOnBottomEdge = player1.hitBox.y <= camera.viewportHeight / 2f;
		boolean playerOnTopEdge = player1.hitBox.y >= 5000 - camera.viewportHeight / 2f;
		if (!playerOnRightEdge && !playerOnLeftEdge) {
			camera.position.x = player1.hitBox.x + player1.hitBox.width / 2;
		}

		if (!playerOnTopEdge && !playerOnBottomEdge) {
			camera.position.y = player1.hitBox.y + player1.hitBox.height /2;
		}
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
	}
	public Animation<TextureRegion> animador(Texture imagen, int cantframe, float fotogramas,int comienzoframe){
		TextureRegion[][] tmp = TextureRegion.split(imagen,imagen.getWidth()/cantframe, imagen.getHeight());
		TextureRegion[] animation = new TextureRegion[cantframe];
		int index=0;
		for (int i = comienzoframe; i < cantframe; i++) {
			animation[index++]=tmp[0][i];
		}
		Animation<TextureRegion> animationfinal = new Animation<>(fotogramas,animation);
		return animationfinal;
	}
}
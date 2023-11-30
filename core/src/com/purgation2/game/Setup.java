package com.purgation2.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Setup extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		this.setScreen(new Menu(this));
	}
	@Override
	public void render(){
		super.render();
	}
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}

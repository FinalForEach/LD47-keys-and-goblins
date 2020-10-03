package finalforeach.ld47.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TileMap
{
	public static final int MAP_SIZE = 256;
	Tile[][] tiles = new Tile[MAP_SIZE][MAP_SIZE];
	public Vector2 spawnLoc;

	public TileMap() 
	{
		spawnLoc = new Vector2();
		loadFromFile("tilemap.txt");
	}
	public void loadFromFile(String fileName) 
	{
		FileHandle fh = Gdx.files.internal(fileName);

		String mapStr = fh.readString();
		String[] mapLines =  mapStr.split("\n");
		for(int ln = Math.min(mapLines.length, MAP_SIZE)-1; ln >=0; ln--)
		{
			String l = mapLines[ln];
			for(int cn = 0; cn < l.length() && cn < MAP_SIZE; cn++)
			{
				char c = l.charAt(cn);
				Tile t = null;
				switch(c) 
				{
				case 'D': t = new DebugTile();break;
				case 'F': t = new FloorTile();break;
				case 'W': t = new WallTile();break;
				case 'L': 
					t = new LadderTile();
					spawnLoc = new Vector2(cn * 16, ln * 16);
					System.out.println(spawnLoc);
					break;
				}
				addTile(t, cn, ln);
			}
		}
	}
	public void addTile(Tile t, int i, int j) 
	{
		tiles[i][j] = t;
		t.setTilePos(i, j);
	}
	public Tile getTile(int i, int j) 
	{
		if(i<0 || j<0 || i>=MAP_SIZE || j>=MAP_SIZE)return null;
		return tiles[i][j];
	}

	public void draw(SpriteBatch batch) 
	{
		for(int i =0; i < MAP_SIZE; i++){
			for(int j =0; j < MAP_SIZE; j++){
				Tile t = tiles[i][j];
				if(t!=null) 
				{
					t.draw(batch);
				}
			}
		}
	}
	public void update() {
		for(int i =0; i < MAP_SIZE; i++){
			for(int j =0; j < MAP_SIZE; j++){
				Tile t = tiles[i][j];
				if(t!=null) 
				{
					t.update(this);
				}
			}
		}
	}
	public Array<Tile> getSurroundingTiles(int i, int j, boolean includeCenter) 
	{
		Array<Tile> tiles = new Array<Tile>();
		for(int i2=-1; i2<=1;i2++) 
		{
			for(int j2=-1; j2<=1;j2++) 
			{
				if(i==0 && j==0 && !includeCenter)continue;
				Tile t = getTile(i+i2, j+j2);
				if(t!=null) 
				{
					tiles.add(t);
				}
			}	
		}
		return tiles;
	}
	public Array<Tile> getAdjacentTiles(int i, int j, boolean includeCenter) 
	{
		Array<Tile> tiles = new Array<Tile>();
		Tile t =null;
		if(includeCenter) 
		{
			t = getTile(i, j);
			if(t!=null) {
				tiles.add(t);
			}
		}
		t = getTile(i-1, j);
		if(t!=null) {
			tiles.add(t);
		}
		t = getTile(i+1, j);
		if(t!=null) {
			tiles.add(t);
		}
		t = getTile(i, j-1);
		if(t!=null) {
			tiles.add(t);
		}
		t = getTile(i, j+1);
		if(t!=null) {
			tiles.add(t);
		}
		return tiles;
	}

}

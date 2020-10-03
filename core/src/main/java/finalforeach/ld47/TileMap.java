package finalforeach.ld47;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileMap
{
	public static final int MAP_SIZE = 256;
	Tile[][] tiles = new Tile[MAP_SIZE][MAP_SIZE];
	
	public TileMap() 
	{
		addTile(new WallTile(), 0, 0);
		addTile(new DebugTile(), 1, 1);
		addTile(new FloorTile(), 1, 2);
		addTile(new WallTile(), 1, 3);
		addTile(new WallTile(), 1, 4);
		addTile(new DebugTile(), 2, 4);		

		addTile(new WallTile(), 10, 10);
		addTile(new WallTile(), 10, 11);
		addTile(new WallTile(), 11, 10);
		addTile(new WallTile(), 11, 11);
		addTile(new WallTile(), 12, 10);
		addTile(new WallTile(), 12, 11);
		
		addTile(new WallTile(), 20, 10);
		addTile(new WallTile(), 20, 11);
		addTile(new WallTile(), 20, 12);
		

		addTile(new WallTile(), 25, 10);
		addTile(new WallTile(), 25, 11);
		addTile(new WallTile(), 25, 12);
		addTile(new WallTile(), 25, 13);
		addTile(new WallTile(), 26, 12);
		
	}
	public void addTile(Tile t, int i, int j) 
	{
		tiles[i][j] = t;
		t.i=i;
		t.j=j;
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

}

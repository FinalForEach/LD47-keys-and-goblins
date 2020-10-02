package finalforeach.ld47;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileMap
{
	public static final int MAP_SIZE = 256;
	Tile[][] tiles = new Tile[MAP_SIZE][MAP_SIZE];
	
	public TileMap() 
	{
		addTile(new DebugTile(), 0, 0);
		addTile(new DebugTile(), 1, 1);
		addTile(new DebugTile(), 1, 2);
	}
	public void addTile(Tile t, int i, int j) 
	{
		tiles[i][j] = t;
		t.i=i;
		t.j=j;
	}

	public void draw(SpriteBatch batch) 
	{
		for(int i =0; i < MAP_SIZE; i++) 
		{
			for(int j =0; j < MAP_SIZE; j++) 
			{
				Tile t = tiles[i][j];
				if(t!=null) 
				{
					t.draw(batch);
				}
			}
		}
	}

}

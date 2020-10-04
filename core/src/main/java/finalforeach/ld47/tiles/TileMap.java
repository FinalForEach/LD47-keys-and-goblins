package finalforeach.ld47.tiles;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import finalforeach.ld47.entities.HeartItem;
import finalforeach.ld47.entities.KeyItem;

public class TileMap
{
	public static final int MAP_SIZE = 128;
	Tile[][] tiles = new Tile[MAP_SIZE][MAP_SIZE];
	public LevelTheme levelTheme = LevelTheme.NORMAL;
	public Set<IUpdateDelta> updatingTiles;
	public Set<FloorTile> floorTiles;
	public Vector2 spawnLoc;

	public TileMap() 
	{
		updatingTiles = new HashSet<>();
		floorTiles = new HashSet<>();
		spawnLoc = new Vector2();
		generateLevel();
	}
	public void generateLevel() 
	{

		for(int i=0;i<MAP_SIZE;i++) 
		{
			for(int j=0;j<MAP_SIZE;j++) 
			{
				addTile(new WallTile(), i, j);
			}
		}

		int numRooms = MathUtils.random(10, 16);
		Array<Room> rooms = new Array<Room>();
		for(int r = 0; r < numRooms; r++) 
		{
			int roomWidth = MathUtils.random(8, 32);
			int roomHeight = MathUtils.random(8, 32);
			int startI = MathUtils.random(1, MAP_SIZE - roomWidth - 1);
			int startJ = MathUtils.random(1, MAP_SIZE - roomHeight - 1);
			rooms.add(new Room(startI,startJ,roomWidth,roomHeight));
		}
		for(Room room : rooms) 
		{
			int x = room.startI;
			int y = room.startJ;
			int x2 = (room.startI + room.width);
			int y2 = (room.startJ + room.height);
			for(int i=x;i<x2;i++) 
			{
				for(int j=y;j<y2;j++) 
				{
					addTile(new FloorTile(), i, j);
				}
			}
		}

		// Connect the rooms with paths

		Set<Room> connRooms = new HashSet<Room>();
		Set<Room> unconnRooms = new HashSet<Room>();
		for(Room room : rooms) 
		{
			unconnRooms.add(room);
			Room closest = null;
			for(int rB =0; rB < rooms.size; rB++) 
			{
				Room roomB = rooms.get(rB);
				if(room==roomB)continue;
				if(closest==null)closest = roomB;

				if(room.manhattanDistToRoom(roomB) < room.manhattanDistToRoom(closest)) 
				{
					closest = roomB;
				}
			}
		}


		connRooms.add(rooms.random());
		unconnRooms.removeAll(connRooms);
		while(connRooms.size() < rooms.size) 
		{
			Room rA = (Room) connRooms.toArray()[MathUtils.random(connRooms.size()-1)];
			Room rB = (Room) unconnRooms.toArray()[MathUtils.random(unconnRooms.size()-1)];

			connRooms.add(rB);
			unconnRooms.remove(rB);
			generatePathBetweenRooms(rA, rB);
		}

		// Decorate the rooms

		Array<Tile> replaceableTiles = new Array<Tile>();
		for(int i=0;i<MAP_SIZE;i++) 
		{
			for(int j=0;j<MAP_SIZE;j++) 
			{
				Tile t =getTile(i,j);
				if(t==null)continue;
				Array<Tile> surr = getSurroundingTiles(i, j, true);
				boolean canReplace = true;
				for(Tile s : surr) 
				{
					if(!(s instanceof FloorTile)) 
					{
						canReplace = false;break;
					}
				}
				if(canReplace) 
				{
					replaceableTiles.add(t);
				}
			}
		}
		Tile rt = replaceableTiles.random();
		replaceableTiles.removeValue(rt, true);
		spawnLoc.set(rt.bb.min.x,rt.bb.min.y);
		addTile(new LadderTile(), rt.getI(), rt.getJ());

		rt = replaceableTiles.random();
		replaceableTiles.removeValue(rt, true);
		addTile(new StairTile(), rt.getI(), rt.getJ());

		int numChests = 5;
		int numSpikes = 10;
		for(int i = 0; i < numChests; i++) 
		{
			rt = replaceableTiles.random();
			if(rt!=null) 
			{
				replaceableTiles.removeValue(rt, true);
				if(i==0) 
				{
					addTile(new ChestTile(new KeyItem(levelTheme)), rt.getI(), rt.getJ());
				}else 
				{
					addTile(new ChestTile(new HeartItem()), rt.getI(), rt.getJ());
				}
			}
		}
		for(int s = 0; s < numSpikes; s++) 
		{
			rt = replaceableTiles.random();
			if(rt!=null) 
			{
				replaceableTiles.removeValue(rt, true);
				for(Tile t : getSurroundingTiles(rt.getI(), rt.getJ(), true, MathUtils.random(2, 3))) 
				{
					if(t instanceof FloorTile && MathUtils.randomBoolean(0.5f)) 
					{
						addTile(new SpikeTile(), t.getI(), t.getJ());	
						replaceableTiles.removeValue(t, true);
					}
				}
			}
		}
		if(levelTheme==LevelTheme.OVERGROWN) 
		{
			int numPlants = 30;

			for(int p = 0; p < numPlants; p++) 
			{
				rt = replaceableTiles.random();
				if(rt!=null) 
				{
					replaceableTiles.removeValue(rt, true);
					for(Tile t : getSurroundingTiles(rt.getI(), rt.getJ(), true, MathUtils.random(1, 4))) 
					{
						if(t instanceof FloorTile && MathUtils.randomBoolean(0.125f)) 
						{
							addTile(new PlantTile(), t.getI(), t.getJ());	
							replaceableTiles.removeValue(t, true);
						}
					}
				}
			}
		}

		if(levelTheme==LevelTheme.HOT) 
		{
			int numLavaPools = 10;

			for(int p = 0; p < numLavaPools; p++) 
			{
				rt = replaceableTiles.random();
				if(rt!=null) 
				{
					replaceableTiles.removeValue(rt, true);
					for(Tile t : getSurroundingTiles(rt.getI(), rt.getJ(), true, MathUtils.random(1, 4))) 
					{
						// Ensure you do not block off an entrance
						if(replaceableTiles.contains(t, false)) 
						{
							if(t instanceof FloorTile && MathUtils.randomBoolean(0.9f)) 
							{
								addTile(new LavaTile(), t.getI(), t.getJ());	
								replaceableTiles.removeValue(t, true);
							}
						}
					}
				}
			}
		}



		// Trim all the walls
		for(int i=0;i<MAP_SIZE;i++) 
		{
			for(int j=0;j<MAP_SIZE;j++) 
			{

				Tile t =getTile(i,j);
				if(t instanceof FloorTile) 
				{
					floorTiles.add((FloorTile) t);
				}
				if(!(t instanceof WallTile))continue;
				Array<Tile> surr = getSurroundingTiles(i, j, false);
				boolean canTrim = true;
				for(Tile s : surr) 
				{
					if(!(s instanceof WallTile)) 
					{
						canTrim = false;break;
					}
				}
				if(canTrim) 
				{
					removeTile(i,j);
				}
			}
		}
		update();
	}
	private void generatePathBetweenRooms(Room roomA, Room roomB) 
	{
		roomA.connectedRooms.add(roomB);
		roomB.connectedRooms.add(roomA);
		int pathStartX = MathUtils.random(roomA.startI, roomA.startI+roomA.width);
		int pathEndX = MathUtils.random(roomB.startI, roomB.startI+roomB.width);

		int pathStartY = MathUtils.random(roomA.startJ, roomA.startJ+roomA.height);
		int pathEndY = MathUtils.random(roomB.startJ, roomB.startJ+roomB.height);

		int incX = (int)Math.signum(pathEndX - pathStartX);
		int incY = (int)Math.signum(pathEndY - pathStartY);
		for(int i=pathStartX; i!=pathEndX;i+=incX) 
		{
			addTile(new FloorTile(), i, pathStartY);
		}
		for(int j=pathStartY; j!=pathEndY;j+=incY) 
		{
			addTile(new FloorTile(), pathEndX , j);
		}
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
	public void removeTile(int i, int j) 
	{
		tiles[i][j] = null;
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
		return getSurroundingTiles(i, j, includeCenter, 1);
	}
	public Array<Tile> getSurroundingTiles(int i, int j, boolean includeCenter, int size) 
	{
		Array<Tile> tiles = new Array<Tile>();
		for(int i2=-size; i2<=size;i2++) 
		{
			for(int j2=-size; j2<=size;j2++) 
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
	public void clickTile(int i, int j) 
	{
		Tile t = getTile(i, j);
		if(t != null) 
		{
			t.click();
		}
	}

}

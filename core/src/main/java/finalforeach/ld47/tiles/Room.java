package finalforeach.ld47.tiles;

import java.util.HashSet;
import java.util.Set;

public class Room
{
	Set<Room> connectedRooms;
	int startI, startJ, width, height;
	Room(int i, int j, int w, int h)
	{
		connectedRooms = new HashSet<Room>();
		startI = i;
		startJ = j;
		width = w;
		height = h;
	}
	public double manhattanDistToRoom(Room roomB) 
	{
		float cxA = startI + width/2;
		float cyA = startJ + height/2;
		float cxB = roomB.startI + roomB.width/2;
		float cyB = roomB.startJ + roomB.height/2;
		return Math.abs(cxB - cxA) + Math.abs(cyB - cyA);
	}
}
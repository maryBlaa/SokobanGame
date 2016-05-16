package com.sokoban.maryblaa.sokoban.collision;

import com.sokoban.maryblaa.sokoban.math.Vector2;

public interface Shape2D {
	
	public boolean intersects(Shape2D shape);
	public boolean intersects(Point point);
	public boolean intersects(Circle circle);
	public boolean intersects(AABB box);
	
	public Vector2 getPosition();
	public void setPosition(Vector2 position);
}

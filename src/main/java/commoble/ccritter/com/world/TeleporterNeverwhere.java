package commoble.ccritter.com.world;

import java.util.LinkedList;
import java.util.Random;

import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.util.IntLoc;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterNeverwhere extends Teleporter
{
	private WorldServer serv;	//superclass's worldserver is private, need this one
	private Random rando;
	private int centerX;
	private int centerY;
	private int centerZ;
	
	public TeleporterNeverwhere(WorldServer serv, int x, int y, int z)
	{
		super(serv);
		this.serv = serv;
		this.rando = new Random();
		this.centerX = x;
		this.centerY = y;
		this.centerZ = z;
	}


    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity ent, double xPos, double yPos, double zPos, float f)
    {
    	// going TO neverwhere
    	if (this.serv.provider.dimensionId == CommonProxy.neverwhereDimID)
    	{
            if (!this.placeInExistingPortal(ent, xPos, yPos, zPos, f))
            {
                this.makePortal(ent);
                this.placeInExistingPortal(ent, xPos, yPos, zPos, f);
            }
    	}
    	else	// coming FROM neverwhere
    	{
    		this.placeInDecentSpawnPoint(ent, xPos, yPos, zPos, f);
    	}
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    public boolean placeInExistingPortal(Entity ent, double xPos, double yPos, double zPos, float miscfloat)
    {
      
        // going to Neverwhere
        if (this.serv.provider.dimensionId == CommonProxy.neverwhereDimID)
        {
        	LinkedList<IntLoc> list = new LinkedList<IntLoc>();
        	
        	// scan a cube around the center location and pick a random portal-containing position within it
        	for (int i = centerX-5; i <= centerX+5; i++)
        	{
        		for (int j = centerY-5; j <= centerY+5 && j < serv.getActualHeight()-1; j++)
        		{
        			for (int k = centerZ-5; k <= centerZ+5; k++)
        			{
        				if (j > 0 && serv.getBlock(i, j, k) == CommonProxy.neverPortal)
        				{
        					list.add(new IntLoc(i, j, k));
        				}
        			}
        		}
        	}
        	
        	if (list.isEmpty())
        	{
        		return false;
        	}
        	else
        	{
        		IntLoc loc = list.get(rando.nextInt(list.size()));
        		ent.setLocationAndAngles(loc.x + 0.5D, loc.y + 0.5D, loc.z + 0.5D, ent.rotationYaw, ent.rotationPitch);
        		return true;
        	}
        }
        else // coming from Neverwhere
        {
        	this.placeInDecentSpawnPoint(ent, xPos, yPos, zPos, miscfloat);
        	return true;
        }
    }
    
    public boolean makePortal(Entity ent)
    {
    	// clean out the area around the portal
    	
    	for (int i = centerX-9; i <= centerX+9; i++)
    	{
    		for (int j = (centerY-8 > 0 ? centerY-8 : 1); j <= centerY+9 && j < serv.getActualHeight()-1; j++)
    		{
    			for (int k = centerZ-9; k <= centerZ+9; k++)
    			{
    				double dist = MathHelper.sqrt_double((centerX-i)*(centerX-i) + (centerY-j)*(centerY-j) + (centerZ-k)*(centerZ-k));
    				if (dist <= 10.0D && serv.getBlock(i, j, k) != Blocks.bedrock)
    				{
    					serv.setBlock(i, j, k, Blocks.air);
    				}
    			}
    		}
    	}
    	
    	generatePortalBlocks(centerX, centerY, centerZ, 6);

    	return true;
    }
    
    void generatePortalBlocks(int x, int y, int z, int portalCount)
    {
    	if (y > 0 && y < serv.getActualHeight()-1 && portalCount != 0 && serv.getBlock(x, y, z) != Blocks.bedrock)
    	{
	    	serv.setBlock(x, y, z, CommonProxy.neverPortal);
	    	portalCount--;
	    	
	    	int direction = rando.nextInt(6);
	    	switch (direction)
	    	{
	    		case 0: x++;
	    			break;
	    		case 1: x--;
    				break;
	    		case 2: y++;
	    			break;
	    		case 3: y--;
					break;
	    		case 4: z++;
    				break;
	    		case 5: z--;
    				break;
	    	}
	    	
	    	generatePortalBlocks(x, y, z, portalCount);
	    	
	    	direction = rando.nextInt(6);
	    	switch (direction)
	    	{
	    		case 0: x++;
	    			break;
	    		case 1: x--;
    				break;
	    		case 2: y++;
	    			break;
	    		case 3: y--;
					break;
	    		case 4: z++;
    				break;
	    		case 5: z--;
    				break;
	    	}
	    	generatePortalBlocks(x, y, z, portalCount);
    	}
    }
    
    private void placeInDecentSpawnPoint(Entity ent, double xPos, double yPos, double zPos, float miscfloat)
    {
    	// get a list of suitable locations around the portal point and pick one at random
    	LinkedList<IntLoc> list = new LinkedList<IntLoc>();
    	int x = MathHelper.floor_double(xPos);
    	int y = MathHelper.floor_double(yPos);
    	int z = MathHelper.floor_double(zPos);
    	
    	for (int i = x-10; i <= x+10; i++)
    	{
    		for (int j = (y-10 > 0 ? y-10 : 1); j <= y+10 && j < serv.getActualHeight()-2; j++)
    		{
    			for (int k = z-10; k <= z+10; k++)
    			{
    				if (serv.getBlock(i, j, k) == Blocks.air && serv.getBlock(i, j+1, k) == Blocks.air)
    				{
    					list.add(new IntLoc(i,j,k));
    				}
    			}
    		}
    	}
    	
    	// spawn in random rock if no air block, or 
    	if (list.isEmpty())
    	{
    		double randX = this.rando.nextDouble()*10.0 - 5.0;
    		double randY = this.rando.nextDouble()*10.0 - 5.0;
    		double randZ = this.rando.nextDouble()*10.0 - 5.0;
    		
    		ent.setLocationAndAngles(xPos+randX, yPos+randY, zPos+randZ, ent.rotationYaw, ent.rotationPitch);
    	}
    	else
    	{
    		IntLoc loc = list.get(this.rando.nextInt(list.size()));
    		
    		ent.setLocationAndAngles(loc.x+0.5D, loc.y+0.5D, loc.z+0.5D, ent.rotationYaw, ent.rotationPitch);
    		
    	}
    }
}

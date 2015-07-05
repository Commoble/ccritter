package commoble.ccritter.com.block.tileentity;

import commoble.ccritter.com.CommonProxy;
import commoble.ccritter.com.world.TeleporterNeverwhere;
import commoble.ccritter.network.PacketPortalRender;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityNeverPortal extends TileEntityWithCubicModel
{
	public int portalTimer;
	private final static int portalTickAdd = 5;
	private final static int maxPortalTime = 20;
	public final static int portalResetTime = -200;
	//private boolean[] renderOnSide;	// false if side should not be rendered
	public int sidesIdentifier;
	public boolean needsNeighborCheck;
	
	public TileEntityNeverPortal()
	{
		super();
		
		this.portalTimer = portalResetTime;
		this.sidesIdentifier = 0;
		this.needsNeighborCheck = true;
	}
	
	public void checkAllSides()
	{
		this.sidesIdentifier = 0;
		for (int side=0; side<6; side++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(side);
			Block adjblock = this.worldObj.getBlock(this.xCoord+dir.offsetX, this.yCoord + dir.offsetY, this.zCoord+dir.offsetZ);
			if (!adjblock.isOpaqueCube() && adjblock != CommonProxy.neverPortal)
			{
				this.sidesIdentifier += (1 << side);
			}
		}
		CommonProxy.network.sendToAll(new PacketPortalRender(this.xCoord, this.yCoord, this.zCoord, this.sidesIdentifier));
		this.needsNeighborCheck = false;
	}
	
	@Override
	public void updateEntity()
	{
		if (this.portalTimer > 0)
		{
			this.portalTimer--;
		}
		else if (this.portalTimer < 0)
		{
			this.portalTimer++;
		}
		if (this.needsNeighborCheck)
		{
			this.checkAllSides();
		}
	}
	
	public void incrementPortal(Entity ent, int x, int y, int z)
	{
		int dim = CommonProxy.neverwhereDimID;
		if ((ent.ridingEntity == null) && (ent.riddenByEntity == null) && (ent instanceof EntityPlayerMP))
		{
			EntityPlayerMP player = (EntityPlayerMP) ent;
			MinecraftServer mserv = MinecraftServer.getServer();
			this.portalTimer += portalTickAdd;
			if (portalTimer > maxPortalTime)
			{
				this.portalTimer = portalResetTime;
				if (player.dimension != CommonProxy.neverwhereDimID)	// going to Neverwhere
				{
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dim,
							new TeleporterNeverwhere(mserv.worldServerForDimension(dim), x, y, z));
				}
				else	// going to Surface
				{
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0,
							new TeleporterNeverwhere(mserv.worldServerForDimension(0), x, y, z));
				}
			}
		}
	}

	@Override
	public int getSidesIdentifier()
	{
		return this.sidesIdentifier;
	}
}

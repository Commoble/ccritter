package commoble.ccritter.network;

import commoble.ccritter.com.block.tileentity.TileEntityNeverPortal;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPortalRender implements IMessage
{
	int x;
	int y;
	int z;
	int sides;
	
	public PacketPortalRender(int x, int y, int z, int sides)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.sides = sides;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = ByteBufUtils.readVarInt(buf, 4);
		this.y = ByteBufUtils.readVarInt(buf, 4);
		this.z = ByteBufUtils.readVarInt(buf, 4);
		this.sides = ByteBufUtils.readVarInt(buf, 4);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, x, 4);
		ByteBufUtils.writeVarInt(buf, y, 4);
		ByteBufUtils.writeVarInt(buf, z, 4);
		ByteBufUtils.writeVarInt(buf, sides, 4);
	}

	public static class Handler implements IMessageHandler<PacketPortalRender, IMessage>
	{

		@Override
		public IMessage onMessage(PacketPortalRender message, MessageContext ctx)
		{
			TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x, message.y, message.z);
			if (te instanceof TileEntityNeverPortal)
			{
				((TileEntityNeverPortal)te).sidesIdentifier = message.sides;
			}

			return null; // no response
		}
		
	}
}

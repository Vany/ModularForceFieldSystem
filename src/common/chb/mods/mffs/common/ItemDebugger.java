/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
*/

package chb.mods.mffs.common;

import java.util.Arrays;

import ic2.api.IWrenchable;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ItemDebugger extends  ItemMultitool  {
	protected StringBuffer info = new StringBuffer();

	public ItemDebugger(int i) {
		super(i,3);
	}

	

    
    
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getBlockTileEntity(x,y,z);
		
		if (!world.isRemote) {
			
			if (tileEntity instanceof TileEntityProjector) {
				info.setLength(0);
				info.append("Projector: ").append(((TileEntityProjector) tileEntity).getForcefieldtextur_id(0));
				Functions.ChattoPlayer(entityplayer, info.toString());
			}
			
			
			if (tileEntity instanceof TileEntityCapacitor) {
				info.setLength(0);
				info.append("Capacitor: ").append(((TileEntityCapacitor) tileEntity).getSecStation_ID());
				Functions.ChattoPlayer(entityplayer, info.toString());
			}
			if (tileEntity instanceof TileEntitySecurityStation) {
				info.setLength(0);
				info.append("Capacitor: ").append(((TileEntitySecurityStation) tileEntity).getSecurtyStation_ID());
				Functions.ChattoPlayer(entityplayer, info.toString());
			}
			
			
		}else{
			
			
			if (tileEntity instanceof TileEntityProjector) {
				info.setLength(0);
				info.append("Projector: ").append(((TileEntityProjector) tileEntity).getForcefieldtextur_id(0));
				Functions.ChattoPlayer(entityplayer, info.toString());
			}
			
			if (tileEntity instanceof TileEntityCapacitor) {
				info.setLength(0);
				info.append("Capacitor: ").append(((TileEntityCapacitor) tileEntity).getSecStation_ID());
				Functions.ChattoPlayer(entityplayer, info.toString());
			}
			if (tileEntity instanceof TileEntitySecurityStation) {
				info.setLength(0);
				info.append("Capacitor: ").append(((TileEntitySecurityStation) tileEntity).getSecurtyStation_ID());
				Functions.ChattoPlayer(entityplayer, info.toString());
			}
		}

		return false;
	}


	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
		System.out.println("Capacitor:" +Linkgrid.getWorldMap(world).getCapacitor().size());
		System.out.println("Converter:" +Linkgrid.getWorldMap(world).getConverter().size());
		System.out.println("Extractor:" +Linkgrid.getWorldMap(world).getExtractor().size());
		System.out.println("Projector:" +Linkgrid.getWorldMap(world).getProjektor().size());
	
		
		return itemstack;
	}
}

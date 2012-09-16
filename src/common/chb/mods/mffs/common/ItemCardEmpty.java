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

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ItemCardEmpty extends Item {
	public ItemCardEmpty(int i) {
		super(i);
		setIconIndex(16);
		setMaxStackSize(1);
		setTabToDisplayOn(CreativeTabs.tabMaterials);
	}
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}
	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer,
			World world, int i, int j, int k, int l) {
		
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

			if (tileEntity instanceof TileEntityCapacitor) {
				if(Linkgrid.getWorldMap(world).getSecStation().get(((TileEntityCapacitor)tileEntity).getSecStation_ID()) != null)
				{
					if (!(Linkgrid.getWorldMap(world).getSecStation().get(((TileEntityCapacitor)tileEntity).getSecStation_ID()).isAccessGranted(entityplayer.username,2))) {
						return false;
					}
				}

				ItemStack newcard =  new ItemStack(ModularForceFieldSystem.MFFSitemfc);
				ItemCardPowerLink.setCapacitorID(newcard,(((TileEntityCapacitor)tileEntity).getCapacitor_ID()));
				
				entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = newcard;
	
				if (world.isRemote)
				Functions.ChattoPlayer(entityplayer, "[Generator] Success: <Power-Link> Card create");
				return true;
			}

			if (tileEntity instanceof TileEntitySecurityStation) {
				if (!(((TileEntitySecurityStation)tileEntity).isAccessGranted(entityplayer.username,ModularForceFieldSystem.PERSONALID_FULLACCESS))) {
					return false;
				}

				ItemStack newcard =   new ItemStack(ModularForceFieldSystem.MFFSItemSecLinkCard);
				NBTTagCompoundHelper.getTAGfromItemstack(newcard).setInteger("Secstation_ID", ((TileEntitySecurityStation)tileEntity).getSecurtyStation_ID());
				entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = newcard;
				
				if (world.isRemote)
				Functions.ChattoPlayer(entityplayer, "[Security Station] Success: <Security Station Link>  Card create");
				return true;
			}
		
		return false;
	}
}

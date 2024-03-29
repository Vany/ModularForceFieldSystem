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

import chb.mods.mffs.api.ISecurityLinkCard;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ItemCardSecurityLink extends Item implements ISecurityLinkCard {
	private StringBuffer info = new StringBuffer();

	public ItemCardSecurityLink(int i) {
		super(i);
		setIconIndex(19);
		setMaxStackSize(1);
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
	public boolean isSecurityCardValidity(ItemStack itemstack, World world) {

		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {

			TileEntitySecurityStation secstation = Linkgrid.getWorldMap(world)
					.getSecStation()
					.get(nbtTagCompound.getInteger("Secstation_ID"));
			if (secstation != null) {
				return true;
			}

		}

		return false;
	}

	public boolean isAccessGranted(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int accessmode,
			boolean AccessErrorMessage) {
		
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {

			TileEntitySecurityStation secstation = Linkgrid.getWorldMap(world)
					.getSecStation()
					.get(nbtTagCompound.getInteger("Secstation_ID"));
			
			if (secstation != null) {

				if (!secstation.isAccessGranted(entityplayer.username,
						accessmode)) {
					if (AccessErrorMessage)
						Functions.ChattoPlayer(entityplayer,
								"[MFFS SecurityStation] access denied for "
										+ entityplayer.username);
					return false;
				}

			}
		}
		return true;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (!world.isRemote) {
			if (tileEntity instanceof TileEntityCapacitor) {

				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, ModularForceFieldSystem.PERSONALID_FULLACCESS)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 4, "<Security Station Link>");

				}
			}

			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, ModularForceFieldSystem.PERSONALID_FULLACCESS)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 1, "<Security Station Link>");
				}
			}
			
			if (tileEntity instanceof TileEntitySecStorage) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, ModularForceFieldSystem.PERSONALID_FULLACCESS)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 0, "<Security Station Link>");
				}
			}
			

			if (tileEntity instanceof TileEntityProjector) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, ModularForceFieldSystem.PERSONALID_FULLACCESS)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 12, "<Security Station Link>");
				}
			}
		}

		return false;
	}

}
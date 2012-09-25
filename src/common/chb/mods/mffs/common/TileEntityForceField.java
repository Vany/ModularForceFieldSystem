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

import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;


public class TileEntityForceField extends TileEntity implements INetworkHandlerListener{
private int[] texturid = {180,180,180,180,180,180};
private boolean init = true;

	public TileEntityForceField() {
	}

	public int[] getTexturid()
	{
		return texturid;
	}

	public int getTexturid(int l)
	{
		return texturid[l];
	}
	
	public void  setTexturid(String texturid )
	{
		texturid = texturid.replace("[", "");
		texturid = texturid.replace("]", "");
		String[] test = texturid.split(",");	

		int[] texturarray = new int[6];
		
		texturarray[0]=Integer.parseInt(test[0]);
		texturarray[1]=Integer.parseInt(test[1]);
		texturarray[2]=Integer.parseInt(test[2]);
		texturarray[3]=Integer.parseInt(test[3]);
		texturarray[4]=Integer.parseInt(test[4]);
		texturarray[5]=Integer.parseInt(test[5]);
		
		setTexturid(texturarray);
	}
	
	
	

	public void  setTexturid(int[] texturid )
	{
		this.texturid = texturid;
		NetworkHandler.updateTileEntityField(this, "texturid");
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		texturid = nbttagcompound.getIntArray("texturid");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setIntArray("texturid", texturid);
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote == false ) {
		if(init)
		{
			UpdateTextur();
		   init = false;
		}
		}else{
			if(init)
			{
				NetworkHandler.requestInitialData(this);
			   init = false;
			}
		}
	}

	public void UpdateTextur()
	{
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(worldObj).getForceFieldStackMap(WorldMap.Cordhash(this.xCoord, this.yCoord, this.zCoord));

		if(ffworldmap != null)
		{
			if(!ffworldmap.isEmpty())

			{
			 TileEntityProjector projector = Linkgrid.getWorldMap(worldObj).getProjektor().get(ffworldmap.getProjectorID());

				if(projector != null)
				{
					setTexturid(projector.getForcefieldtextur_id());
					worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
				}
			}else{worldObj.setBlockWithNotify(this.xCoord, this.yCoord,this.zCoord, 0);}
	}else{
		worldObj.setBlockWithNotify(this.xCoord, this.yCoord,this.zCoord, 0);}
	}



	public ItemStack[] getContents() {
		return null;
	}

	public void setMaxStackSize(int arg0) {
	}

	@Override
	public void onNetworkHandlerUpdate(String field) {
		if (field.equals("texturid")) {
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public List<String> geFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();
		NetworkedFields.add("texturid");
		return NetworkedFields;
	}



}

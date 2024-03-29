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



import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import chb.mods.mffs.api.IForceEnergyCapacitor;
import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileEntityCapacitor extends TileEntityMachines implements
ISidedInventory,INetworkHandlerEventListener,INetworkHandlerListener, IForceEnergyCapacitor{
	
	private ItemStack inventory[];
	private int forcePower;
	private int maxforcepower;
	private int transmitrange;
	private int Capacitor_ID;
	private int Remote_Capacitor_ID;
	private int SecStation_ID;
	private boolean create;
	private boolean LinkedSecStation;
	private short linketprojektor;
	private int capacity;
	private int SwitchTyp;
	private int Powerlinkmode;
	private boolean OnOffSwitch;

	public TileEntityCapacitor() {
		inventory = new ItemStack[5];
		transmitrange = 8;
		SecStation_ID = 0;
		forcePower = 0;
		maxforcepower = 10000000;
		Capacitor_ID = 0;
		Remote_Capacitor_ID = 0;
		linketprojektor = 0;
		create = true;
		LinkedSecStation = false;
		capacity = 0;
		SwitchTyp = 0;
		OnOffSwitch = false;
		Powerlinkmode= 0;
	
	}
	
	
	public boolean isCreate() {
		return create;
	}


	public void setCreate(boolean create) {
		this.create = create;
	}


	public int getRemote_Capacitor_ID() {
		return Remote_Capacitor_ID;
	}

	private void setRemote_Capacitor_ID(int remote_Capacitor_ID) {
		Remote_Capacitor_ID = remote_Capacitor_ID;
	}

	public int getPowerlinkmode() {
		return Powerlinkmode;
	}


	public void setPowerlinkmode(int powerlinkmode) {
		Powerlinkmode = powerlinkmode;
	}

	public boolean getOnOffSwitch() {
		return OnOffSwitch;
	}

	public void setOnOffSwitch(boolean a) {
	   this.OnOffSwitch = a;
	}

	public int getswitchtyp() {
		return SwitchTyp;
	}

	public void setswitchtyp(int a) {
	   this.SwitchTyp = a;
	}

	@Override
	public int getCapacity(){
		return capacity;
	}

	
	public void setCapacity(int Capacity){
		if(this.capacity != Capacity)
		{
		this.capacity = Capacity;
		NetworkHandlerServer.updateTileEntityField(this, "capacity");
		}
	}

	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerCapacitor(inventoryplayer.player, this);
	}

	public boolean isLinkedSecStation() {
		return LinkedSecStation;
	}

	public void setLinkedSecStation(boolean linkedSecStation) {
		LinkedSecStation = linkedSecStation;
	}

	public void setMaxforcepower(int maxforcepower) {
		this.maxforcepower = maxforcepower;
	}

	@Override
	public int getMaxForcePower() {
		return maxforcepower;
	}
	
	public Short getLinketProjektor() {
		return linketprojektor;
	}

	public void setLinketprojektor(Short linketprojektor) {
		if(this.linketprojektor != linketprojektor){
		this.linketprojektor = linketprojektor;
		NetworkHandlerServer.updateTileEntityField(this, "linketprojektor");
		}
	}

	@Override
	public int getForcePower() {
		return forcePower;
	}
	

	public void setForcePower(int f) {
		forcePower = f;
	}

	public int getSecStation_ID() {
		return SecStation_ID;
	}

	public void setTransmitrange(short transmitrange) {
		if(this.transmitrange != transmitrange){
		this.transmitrange = transmitrange;
		NetworkHandlerServer.updateTileEntityField(this, "transmitrange");
		}
	}

	public int getTransmitRange() {
		return transmitrange;
	}

	public int getCapacitor_ID() {
		return Capacitor_ID;
	}

	public int getSizeInventory() {
		return inventory.length;
	}

	private void checkslots(boolean init) {
		int stacksize = 0;
		short temp_transmitrange = 8;
		int temp_maxforcepower = 10000000;

		if (getStackInSlot(0) != null) {
			if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemupgradecapcap) {
				temp_maxforcepower += (2000000 * getStackInSlot(0).stackSize);
			}
		}

		if (getStackInSlot(1) != null) {
			if (getStackInSlot(1).getItem() == ModularForceFieldSystem.MFFSitemupgradecaprange) {
				stacksize += getStackInSlot(1).stackSize;
			}
		}
		
		if (getStackInSlot(2) != null) {
			
			if (getStackInSlot(2).getItem() instanceof IForceEnergyItems) {
				
				IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) getStackInSlot(2).getItem();
				
				if(ForceEnergyItem.getForceEnergy(getStackInSlot(2)) < ForceEnergyItem.getMaxForceEnergy())
				{
					
					int maxtransfer = ForceEnergyItem.getforceEnergyTransferMax();
					int freeeamount = ForceEnergyItem.getMaxForceEnergy() - ForceEnergyItem.getForceEnergy(getStackInSlot(2));
					
					if(this.getForcePower() > 0)
					{

					  if(this.getForcePower() > maxtransfer)
					  {
						    if(freeeamount > maxtransfer)
						    {
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+maxtransfer);
				                this.setForcePower(this.getForcePower() - maxtransfer);		    
						    }else{
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+freeeamount);
				                this.setForcePower(this.getForcePower() - freeeamount);	
						    }
			                
					  }else{
						  
						    if(freeeamount > this.getForcePower())
						    {
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+this.getForcePower());
				                this.setForcePower(this.getForcePower() - this.getForcePower());		    
						    }else{
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+freeeamount);
				                this.setForcePower(this.getForcePower() - freeeamount);	
						    }
						  
						  
					  }
					  
					  getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2)));
					}
			
				}
			
			}
			
		
			
			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemfc) {
				if (getRemote_Capacitor_ID() != NBTTagCompoundHelper.getTAGfromItemstack(
						getStackInSlot(2)).getInteger("CapacitorID")) {
					setRemote_Capacitor_ID(NBTTagCompoundHelper.getTAGfromItemstack(
							getStackInSlot(2)).getInteger("CapacitorID"));
				}
				if(getRemote_Capacitor_ID() == Capacitor_ID)
				{
					setRemote_Capacitor_ID(0);
				}

				if (Linkgrid.getWorldMap(worldObj).getCapacitor()
						.get(this.getRemote_Capacitor_ID()) != null) {
					int transmit = Linkgrid.getWorldMap(worldObj)
							.getCapacitor().get(this.getRemote_Capacitor_ID())
							.getTransmitRange();
					int gen_x = Linkgrid.getWorldMap(worldObj).getCapacitor()
							.get(this.getRemote_Capacitor_ID()).xCoord
							- this.xCoord;
					int gen_y = Linkgrid.getWorldMap(worldObj).getCapacitor()
							.get(this.getRemote_Capacitor_ID()).yCoord
							- this.yCoord;
					int gen_z = Linkgrid.getWorldMap(worldObj).getCapacitor()
							.get(this.getRemote_Capacitor_ID()).zCoord
							- this.zCoord;

					if (Math.sqrt(gen_x * gen_x + gen_y * gen_y + gen_z * gen_z) <= transmit) {
					
					} else {
						setRemote_Capacitor_ID(0);
						
					}
				} else {
					setRemote_Capacitor_ID(0);
				
					if (!init) {
						this.setInventorySlotContents(2, new ItemStack(ModularForceFieldSystem.MFFSitemcardempty));
					}
				}
			}
		 else {
			setRemote_Capacitor_ID(0);
			}
		}else{
			setRemote_Capacitor_ID(0);
		}
			


		if (getStackInSlot(4) != null) {
			if (getStackInSlot(4).getItem() == ModularForceFieldSystem.MFFSItemSecLinkCard) {
				if (SecStation_ID != NBTTagCompoundHelper.getTAGfromItemstack(
						getStackInSlot(4)).getInteger("Secstation_ID")) {
					SecStation_ID = NBTTagCompoundHelper.getTAGfromItemstack(
							getStackInSlot(4)).getInteger("Secstation_ID");
				}
				if (SecStation_ID == 0) {
					dropplugins(4,this);
				}
				if(Linkgrid.getWorldMap(worldObj)
				.getSecStation().get(this.getSecStation_ID())!=null)
				{
				setLinkedSecStation(true);
				}
				else
				{
				setLinkedSecStation(false);
				if (!init) {
					this.setInventorySlotContents(4, new ItemStack(ModularForceFieldSystem.MFFSitemcardempty));
				}
				}
			} else {
			    	SecStation_ID = 0;
				    setLinkedSecStation(false);
					if (getStackInSlot(4).getItem() != ModularForceFieldSystem.MFFSitemcardempty) {
						dropplugins(4,this);
					}
			}
		} else {
			SecStation_ID = 0;
			setLinkedSecStation(false);
		}

		temp_transmitrange *= (stacksize + 1);

		if (this.getTransmitRange() != temp_transmitrange) {
			this.setTransmitrange(temp_transmitrange);
		}
		if (this.getMaxForcePower() != temp_maxforcepower) {
			this.setMaxforcepower(temp_maxforcepower);
		}
		if (this.getForcePower() > this.maxforcepower) {
			this.setForcePower(maxforcepower);
		}
	}

	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a,this);
		}
	}

	public void addtogrid() {
		
		if (Capacitor_ID == 0) {
			Capacitor_ID = Linkgrid.getWorldMap(worldObj)
					.newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getCapacitor().put(Capacitor_ID, this);
		
		registerChunkLoading();

	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getCapacitor().remove(getCapacitor_ID());
		dropplugins();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		SwitchTyp = nbttagcompound.getInteger("SwitchTyp");
		OnOffSwitch = nbttagcompound.getBoolean("OnOffSwitch");
		forcePower = nbttagcompound.getInteger("forcepower");
		maxforcepower = nbttagcompound.getInteger("maxforcepower");
		transmitrange = nbttagcompound.getInteger("transmitrange");
		Capacitor_ID = nbttagcompound.getInteger("Capacitor_ID");
		Powerlinkmode = nbttagcompound.getInteger("Powerlinkmode");
	
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < inventory.length) {
				inventory[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("SwitchTyp", SwitchTyp);
		nbttagcompound.setBoolean("OnOffSwitch", OnOffSwitch);
		nbttagcompound.setInteger("forcepower", forcePower);
		nbttagcompound.setInteger("maxforcepower", maxforcepower);
		nbttagcompound.setInteger("transmitrange", transmitrange);
		nbttagcompound.setInteger("Capacitor_ID", Capacitor_ID);
		nbttagcompound.setInteger("Powerlinkmode", Powerlinkmode);
	

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	public void Energylost(int fpcost) {
		if (this.getForcePower() >= 0) {
			this.setForcePower(this.getForcePower() - fpcost);
		}
		if (this.getForcePower() < 0) {
			this.setForcePower(0);
		}
	}

	public void updateEntity() {
		if (worldObj.isRemote == false) {
			
			if (this.isCreate()) {
				addtogrid();
                checkslots(true);
                setCreate(false);
            }
			

			boolean powerdirekt = worldObj.isBlockGettingPowered(xCoord,
					yCoord, zCoord);
			boolean powerindrekt = worldObj.isBlockIndirectlyGettingPowered(
					xCoord, yCoord, zCoord);

			if(this.getswitchtyp()==0)
			{
		    this.setOnOffSwitch((powerdirekt || powerindrekt));
			}

			if (getOnOffSwitch()) {
				if (isActive() != true) {
					setActive(true);
				}
			} else {
				if (isActive() != false) {
					setActive(false);
				}
			}

			if (this.getTicker() == 10) {
				
				if(this.getLinketProjektor() != (short) Linkgrid.getWorldMap(worldObj).condevisec(getCapacitor_ID(), xCoord, yCoord, zCoord,getTransmitRange()))
				setLinketprojektor((short) Linkgrid.getWorldMap(worldObj).condevisec(getCapacitor_ID(), xCoord, yCoord, zCoord,getTransmitRange()));
				
				
				if(this.getCapacity() != ((getForcePower()/1000)*100)/(getMaxForcePower()/1000))
				   setCapacity(((getForcePower()/1000)*100)/(getMaxForcePower()/1000));
				
		
				checkslots(false);
				if(isActive())
				{
				powertransfer();
				}
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));


		}else {
			
			
			
			if(Capacitor_ID==0)
			{
			
				if (this.getTicker() >= 20 + random.nextInt(20)) {
					
					NetworkHandlerClient.requestInitialData(this,true);
					this.setTicker((short) 0);
				}
				
				this.setTicker((short) (this.getTicker() + 1));
			}
		} 
	}
	
	private void powertransfer()
	{
		
		if(getRemote_Capacitor_ID()!= 0)
		{
	    TileEntityCapacitor RemoteCap = Linkgrid.getWorldMap(worldObj).getCapacitor().get(getRemote_Capacitor_ID());	
	    
	    if(RemoteCap != null)
	    {	    	
	    	
	      int maxtrasferrate = this.getMaxForcePower() / 120;
	      int forceenergyspace = RemoteCap.getMaxForcePower() - RemoteCap.getForcePower();
	      	    	
		switch(this.getPowerlinkmode())
		{
		case 0:
		if(getCapacity() >= 90 && RemoteCap.getCapacity() != 100)
		{
			
		    if(forceenergyspace > maxtrasferrate)
		    {
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
                this.setForcePower(this.getForcePower() - maxtrasferrate);		    
		    }else{
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
                this.setForcePower(this.getForcePower() - forceenergyspace);	
		    }
			
		}
		break;
		case 1:
		if(RemoteCap.getCapacity() < this.getCapacity())	
		{
			int balancevaue = this.getForcePower()- RemoteCap.getForcePower();
			
		    if(balancevaue > maxtrasferrate)
		    {
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
                this.setForcePower(this.getForcePower() - maxtrasferrate);		    
		    }else{
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + balancevaue);
                this.setForcePower(this.getForcePower() - balancevaue);	
		    }
			
		}
		break;		
		case 2:
		if(getForcePower() > 0 && RemoteCap.getCapacity() != 100)
		{

		  if(this.getForcePower() > maxtrasferrate)
		  {
			    if(forceenergyspace > maxtrasferrate)
			    {
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
	                this.setForcePower(this.getForcePower() - maxtrasferrate);		    
			    }else{
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
	                this.setForcePower(this.getForcePower() - forceenergyspace);	
			    }
                
		  }else{
			  
			    if(forceenergyspace > this.getForcePower())
			    {
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + this.getForcePower());
	                this.setForcePower(this.getForcePower() - this.getForcePower());		    
			    }else{
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
	                this.setForcePower(this.getForcePower() - forceenergyspace);	
			    }
			  
			  
		  }

		}		
		break;
		}	
		}
		}
	}

	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	public int getInventoryStackLimit() {
		return 9;
	}

	public ItemStack decrStackSize(int i, int j) {
		if (inventory[i] != null) {
			if (inventory[i].stackSize <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0) {
				inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	public String getInvName() {
		return "Generator";
	}

	public void closeChest() {
	}

	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}



	@Override
	public void openChest() {
	}
	
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		} else {
			return entityplayer.getDistance((double) xCoord + 0.5D,
					(double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
		}
	}
	


	public ItemStack[] getContents() {
		return inventory;
	}

	public void setMaxStackSize(int arg0) {
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public void onNetworkHandlerEvent(int event) {
		switch(event)
		{
		case 0:
			if(this.getswitchtyp() == 0)
			{
				this.setswitchtyp(1);
			}else{
				this.setswitchtyp(0);
			}
		break;
		
		case 1:
			if(this.getPowerlinkmode() != 2)
			{
				this.setPowerlinkmode(this.getPowerlinkmode() +1);
			}else{
				this.setPowerlinkmode(0);
			}
		break;

		}
	}


	@Override
	public void onNetworkHandlerUpdate(String field) {
		
	
		
		if (field.equals("side")) {
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
		if (field.equals("active")) {
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
		if (field.equals("linketprojektor")) {
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
		if (field.equals("transmitrange")) {
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
		if (field.equals("capacity")) {
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
		}
		
	}


	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("active");
		NetworkedFields.add("side");
		NetworkedFields.add("SwitchTyp");
		NetworkedFields.add("linketprojektor");
		NetworkedFields.add("transmitrange");
		NetworkedFields.add("capacity");
		NetworkedFields.add("Capacitor_ID");
		
		return NetworkedFields;
	}


	@Override
	public void onEMPPulse(int magnitude){
		 if(ModularForceFieldSystem.influencedbyothermods)
		 {
		this.setForcePower(this.getForcePower() / 100 * Math.min(Math.max(magnitude, 0), 100));
		 }
	}
	
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		
		switch(Slot)
		{
		case 0:
			if(par1ItemStack.getItem() instanceof  ItemCapacitorUpgradeCapacity)
			return true;
		break;
		case 1:
			if(par1ItemStack.getItem() instanceof  ItemCapacitorUpgradeRange)
			return true;
		break;	
		case 2:
			if(par1ItemStack.getItem() instanceof  IForceEnergyItems || par1ItemStack.getItem() instanceof  ItemCardPowerLink)
			return true;
		break;	
		case 4:
			if(par1ItemStack.getItem() instanceof  ItemCardSecurityLink)
			return true;
		break;	

		}
		
		return false;
	}
	

	

	
	

	
}

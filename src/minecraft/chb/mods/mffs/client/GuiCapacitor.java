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

package chb.mods.mffs.client;


import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;

import org.lwjgl.opengl.GL11;

import chb.mods.mffs.common.ContainerCapacitor;
import chb.mods.mffs.common.TileEntityCapacitor;
import chb.mods.mffs.network.NetworkHandlerClient;

public class GuiCapacitor extends GuiContainer {
	private TileEntityCapacitor Core;

	public GuiCapacitor(EntityPlayer player,
			TileEntityCapacitor tileentity) {
		super(new ContainerCapacitor(player, tileentity));
		Core = tileentity;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int textur = mc.renderEngine
				.getTexture("/chb/mods/mffs/sprites/GuiCapacitor.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
		int i1 = (79 * Core.getCapacity() / 100);
		drawTexturedModalRect(w + 8, k + 71, 176, 0, i1+1, 79);
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Force Energy Capacitor", 5, 5, 0x404040);
		fontRenderer.drawString("Force Energy", 15, 50, 0x404040);
		fontRenderer.drawString(
				(new StringBuilder()).append(" ").append(Core.getForcePower())
						.toString(), 30, 60, 0x404040);

		fontRenderer.drawString("transmit range:", 10, 20, 0x404040);
		fontRenderer.drawString(
				(new StringBuilder()).append(" ")
						.append(Core.getTransmitRange()).toString(), 90, 20,
				0x404040);
		fontRenderer.drawString("linked device:", 10, 35, 0x404040);
		fontRenderer.drawString(
				(new StringBuilder()).append(" ")
						.append(Core.getLinketProjektor()).toString(), 90, 35,
				0x404040);
	}

	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(Core, guibutton.id);
	}

	public void initGui() {
		controlList.add(new GuiGraphicButton(0, (width / 2) + 27, (height / 2) - 60,Core,1));

		controlList.add(new GuiGraphicButton(1, (width / 2) + 38, (height / 2) - 35,Core,2));

		super.initGui();
	}
}

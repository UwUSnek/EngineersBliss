package com.snek.engineersbliss.client.screens.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;




final class BlockListWidget extends AbstractSelectionList<BlockListWidget.Entry> {
	BlockListWidget(Minecraft client, int width, int height, int top, int itemHeight) {
		super(client, width, height, top, itemHeight);
        BuiltInRegistries.BLOCK.forEach(block -> this.addEntry(new Entry(block)));
	}

	@Override
	public int addEntry(Entry entry) {
		return super.addEntry(entry);
	}

	void clear() {
		this.clearEntries();
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput arg) {
		// TODO seems to be possibly accessibility related
	}

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        // draw header above list
        int headerY = this.getY() - 12;
        int rowLeft = this.getRowLeft();
        int rowWidth = this.getRowWidth();

        graphics.text(minecraft.font, Component.literal("Block"), rowLeft + 20, headerY, 0xFFAAAAAA);
        graphics.text(minecraft.font, Component.literal("Enable"), rowLeft + rowWidth - 80, headerY, 0xFFAAAAAA);
        graphics.text(minecraft.font, Component.literal("Isolate"), rowLeft + rowWidth - 40, headerY, 0xFFAAAAAA);
    }

    @Override
    public int getRowWidth() {
        return this.width - 20;
    }





	class Entry extends AbstractSelectionList.Entry<Entry> {
        private final Block block;
        private final Checkbox enableBox;
        private final Checkbox isolateBox;

        public Entry(Block block) {
            this.block = block;
            this.enableBox  = Checkbox.builder(Component.empty(), BlockListWidget.this.minecraft.font).pos(0, 0).build();
            this.isolateBox = Checkbox.builder(Component.empty(), BlockListWidget.this.minecraft.font).pos(0, 0).build();
        }


        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int rowWidth = BlockListWidget.this.getRowWidth();


            // Block icon
            if(block.asItem() == Items.AIR) {
                //TODO add "?" image for missing item forms
            }
            else {
                ItemStack stack = new ItemStack(block);
                graphics.item(
                    stack,
                    this.getContentX(), this.getContentYMiddle() - 8
                );
            }


            // Block name
            graphics.text(
                BlockListWidget.this.minecraft.font,
                block.getName(),
                this.getContentX() + 20, this.getContentYMiddle() - 4,
                0xFFFFFFFF
            );


            // Checkboxes
            enableBox.setX(this.getX() + rowWidth - 80);
            enableBox.setY(this.getContentYMiddle() - 5);
            isolateBox.setX(this.getX() + rowWidth - 40);
            isolateBox.setY(this.getContentYMiddle() - 5);

            enableBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);
            isolateBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
            if (enableBox.mouseClicked(event, doubleClick)) return true;
            if (isolateBox.mouseClicked(event, doubleClick)) return true;
            return super.mouseClicked(event, doubleClick);
        }
	}
}
package com.simibubi.create.networking;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketNbt {

	public ItemStack stack;
	public int slot;

	public PacketNbt(ItemStack stack) {
		this(stack, -1);
	}
	
	public PacketNbt(ItemStack stack, int slot) {
		this.stack = stack;
		this.slot = slot;
	}

	public PacketNbt(PacketBuffer buffer) {
		stack = buffer.readItemStack();
		slot = buffer.readInt();
	}

	public void toBytes(PacketBuffer buffer) {
		buffer.writeItemStack(stack);
		buffer.writeInt(slot);
	}

	public void handle(Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayerEntity player = context.get().getSender();
			
			if (slot == -1) {
				ItemStack heldItem = player.getHeldItemMainhand();
				if (heldItem.getItem() == stack.getItem()) {
					heldItem.setTag(stack.getTag());
				}
				return;
			}
			
			ItemStack heldInSlot = player.inventory.getStackInSlot(slot);
			if (heldInSlot.getItem() == stack.getItem()) {
				heldInSlot.setTag(stack.getTag());
			}
			
		});
	}

}

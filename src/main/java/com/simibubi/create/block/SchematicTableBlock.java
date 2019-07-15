package com.simibubi.create.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SchematicTableBlock extends HorizontalBlock {

	public SchematicTableBlock() {
		super(Properties.from(Blocks.OAK_PLANKS));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING);
		super.fillStateContainer(builder);
	}
	
	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		
		if (worldIn.isRemote) {
			return true;
		} else {
			INamedContainerProvider inamedcontainerprovider = (INamedContainerProvider) worldIn.getTileEntity(pos);
			if (inamedcontainerprovider != null) {
				player.openContainer(inamedcontainerprovider);
			}

			return true;
		}
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SchematicTableTileEntity();
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (worldIn.getTileEntity(pos) == null)
			return;

		SchematicTableTileEntity te = (SchematicTableTileEntity) worldIn.getTileEntity(pos);
		if (!te.inputStack.isEmpty())
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.inputStack);
		if (!te.outputStack.isEmpty())
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.outputStack);

	}

}

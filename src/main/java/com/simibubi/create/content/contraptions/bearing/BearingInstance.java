package com.simibubi.create.content.contraptions.bearing;

import org.joml.Quaternionf;

import com.jozufozu.flywheel.api.event.RenderStage;
import com.jozufozu.flywheel.api.visual.DynamicVisual;
import com.jozufozu.flywheel.api.visual.VisualFrameContext;
import com.jozufozu.flywheel.api.visualization.VisualizationContext;
import com.jozufozu.flywheel.lib.instance.InstanceTypes;
import com.jozufozu.flywheel.lib.instance.OrientedInstance;
import com.jozufozu.flywheel.lib.model.Models;
import com.jozufozu.flywheel.lib.model.baked.PartialModel;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.BackHalfShaftInstance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BearingInstance<B extends KineticBlockEntity & IBearingBlockEntity> extends BackHalfShaftInstance<B> implements DynamicVisual {
	final OrientedInstance topInstance;

	final Axis rotationAxis;
	final Quaternionf blockOrientation;

	public BearingInstance(VisualizationContext materialManager, B blockEntity) {
		super(materialManager, blockEntity);

		Direction facing = blockState.getValue(BlockStateProperties.FACING);
		rotationAxis = Axis.of(Direction.get(Direction.AxisDirection.POSITIVE, axis).step());

		blockOrientation = getBlockStateOrientation(facing);

		PartialModel top =
				blockEntity.isWoodenTop() ? AllPartialModels.BEARING_TOP_WOODEN : AllPartialModels.BEARING_TOP;

		topInstance = instancerProvider.instancerr(InstanceTypes.ORIENTED, Models.partial(top), RenderStage.AFTER_BLOCK_ENTITIES)
				.createInstance();

		topInstance.setPosition(getVisualPosition()).setRotation(blockOrientation);
	}

	@Override
	public void beginFrame(VisualFrameContext ctx) {
		float interpolatedAngle = blockEntity.getInterpolatedAngle(AnimationTickHolder.getPartialTicks() - 1);
		Quaternionf rot = rotationAxis.rotationDegrees(interpolatedAngle);

		rot.mul(blockOrientation);

		topInstance.setRotation(rot);
	}

	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, topInstance);
	}

	@Override
	protected void _delete() {
		super._delete();
		topInstance.delete();
	}

	static Quaternionf getBlockStateOrientation(Direction facing) {
		Quaternionf orientation;

		if (facing.getAxis().isHorizontal()) {
			orientation = Axis.YP.rotationDegrees(AngleHelper.horizontalAngle(facing.getOpposite()));
		} else {
			orientation = new Quaternionf();
		}

		orientation.mul(Axis.XP.rotationDegrees(-90 - AngleHelper.verticalAngle(facing)));
		return orientation;
	}
}
